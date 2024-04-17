
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

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
			status = super.getRequest().getPrincipal().hasRole(manager) && manager.getId() == managerRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<UserStory> objects;
		int projectId;

		projectId = super.getRequest().getData("projectId", int.class);
		objects = this.repository.findManyUserStoriesByProjectId(projectId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "estimatedCost", "priority", "published");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<UserStory> objects) {
		assert objects != null;

		int projectId;
		Project project;
		final boolean showAdd;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findProjectById(projectId);
		showAdd = !project.isPublished() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().addGlobal("projectId", projectId);
		super.getResponse().addGlobal("showAdd", showAdd);
	}

}
