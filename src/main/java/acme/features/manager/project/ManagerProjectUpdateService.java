
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;
import spam.SpamFilter;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

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

		String description;

		description = super.getRequest().getData("$abstract", String.class);

		super.bind(object, "code", "title", "fatalErrors", "cost", "link");
		object.setAbstract(description);
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "manager.project.form.error.duplicated");
		}

		if (!this.getBuffer().getErrors().hasErrors("title")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getTitle()), "title", "manager.project.form.error.spam");
		}

		if (!this.getBuffer().getErrors().hasErrors("$abstract")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getAbstract()), "$abstract", "manager.project.form.error.spam");
		}
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "$abstract", "fatalErrors", "cost", "link", "published");
		dataset.put("manager", object.getManager().getUserAccount().getUsername());

		super.getResponse().addData(dataset);
	}

}
