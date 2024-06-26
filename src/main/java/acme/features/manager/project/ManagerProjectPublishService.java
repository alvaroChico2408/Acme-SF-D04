
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;
import spam.SpamFilter;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	repository;

	private String						errorSpamMessage	= "manager.project.form.error.spam";

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int managerRequestId;
		Manager manager;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findProjectById(projectId);
		manager = project == null ? null : project.getManager();
		managerRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (manager != null)
			status = !project.isPublished() && super.getRequest().getPrincipal().hasRole(manager) //
				&& manager.getId() == managerRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int projectId;

		projectId = super.getRequest().getData("id", int.class);
		object = this.repository.findProjectById(projectId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractProject", "fatalErrors", "cost", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "manager.project.form.error.duplicated");
		}

		Collection<UserStory> userStories = this.repository.findManyUserStoriesByProjectId(object.getId());
		super.state(!userStories.isEmpty(), "*", "manager.project.form.error.nouserstory");

		if (!userStories.isEmpty()) {
			int numUserStoryPublish = userStories.stream().filter(UserStory::isPublished).toList().size();
			boolean allUserStoriesPublish = userStories.size() == numUserStoryPublish;
			super.state(allUserStoriesPublish, "*", "manager.project.form.error.userstorynotpublish");
		}

		super.state(!object.isFatalErrors(), "fatalErrors", "manager.project.form.error.fatalErrors");

		if (!this.getBuffer().getErrors().hasErrors("title")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getTitle()), "title", this.errorSpamMessage);
		}

		if (!this.getBuffer().getErrors().hasErrors("abstractProject")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getAbstractProject()), "abstractProject", this.errorSpamMessage);
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstractProject", "fatalErrors", "cost", "link", "published");
		dataset.put("manager", object.getManager().getUserAccount().getUsername());

		super.getResponse().addData(dataset);
	}

}
