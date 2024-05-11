
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.AssociatedWith;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

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
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<AssociatedWith> associationWithUserStory;

		associationWithUserStory = this.repository.findManyAssociationBetweenProjectAndUserStory(object.getId());
		this.repository.deleteAll(associationWithUserStory);
		this.repository.delete(object);
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
