
package acme.features.manager.associatedWith;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.AssociatedWith;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerAssociatedWithCreateService extends AbstractService<Manager, AssociatedWith> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssociatedWithRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Manager manager;
		int managerRequestId;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findProjectById(projectId);
		manager = project == null ? null : project.getManager();
		managerRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (manager != null)
			status = !project.isPublished() && super.getRequest().getPrincipal().hasRole(manager) && //
				manager.getId() == managerRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssociatedWith object;
		Project project;
		int projectId;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findProjectById(projectId);
		object = new AssociatedWith();
		object.setProject(project);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AssociatedWith object) {
		assert object != null;

		UserStory userStory;
		int userStoryId;

		userStoryId = super.getRequest().getData("userStory", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);

		object.setUserStory(userStory);
	}

	@Override
	public void validate(final AssociatedWith object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(!object.getProject().isPublished(), "*", "manager.associatedWith.form.error.published");
		if (!super.getBuffer().getErrors().hasErrors("*") && !super.getBuffer().getErrors().hasErrors("userStory")) {
			AssociatedWith existing = this.repository.findAssociationBetweenProjectIdAndUserStoryId(object.getProject().getId(), object.getUserStory().getId());
			super.state(existing == null, "*", "manager.associatedWith.form.error.duplicatedRelation");
		}
		if (!super.getBuffer().getErrors().hasErrors("project") && !super.getBuffer().getErrors().hasErrors("userStory")) {
			Manager projectManager = object.getProject().getManager();
			Manager userStoryManager = object.getUserStory().getManager();
			super.state(projectManager.getId() == userStoryManager.getId(), "*", "manager.associatedWith.form.error.sameManager");
		}
	}

	@Override
	public void perform(final AssociatedWith object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AssociatedWith object) {
		assert object != null;

		Dataset dataset;
		int projectId;
		int managerId;
		Collection<UserStory> userStoriesManager;
		Collection<UserStory> userStoriesAssociated;
		Project project;
		SelectChoices choices;

		dataset = super.unbind(object, "userStory", "project");

		projectId = super.getRequest().getData("projectId", int.class);
		dataset.put("projectId", projectId);

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		userStoriesManager = this.repository.findUserStoriesByManagerId(managerId);
		userStoriesAssociated = this.repository.findManyUserStoriesByProjectId(projectId);
		userStoriesManager.removeAll(userStoriesAssociated);

		project = this.repository.findProjectById(projectId);
		dataset.put("project", project);
		dataset.put("published", project.isPublished());

		choices = new SelectChoices();

		if (object.getUserStory() == null)
			choices.add("0", "---", true);
		else
			choices.add("0", "---", false);

		for (final UserStory us : userStoriesManager)
			if (object.getUserStory() != null && object.getUserStory().getId() == us.getId())
				choices.add( //
					Integer.toString(us.getId()), us.getTitle() + " - " + Integer.toString(us.getEstimatedCost()) + " - " + us.getPriority(), //
					true);
			else
				choices.add( //
					Integer.toString(us.getId()), us.getTitle() + " - " + Integer.toString(us.getEstimatedCost()) + " - " + us.getPriority(), //
					false);

		dataset.put("userStory", choices.getSelected().getKey());
		dataset.put("userStories", choices);

		super.getResponse().addData(dataset);
	}

}
