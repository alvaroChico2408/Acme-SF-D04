
package acme.features.any.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;

@Service
public class AnyProjectShowService extends AbstractService<Any, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findProjectById(projectId);
		status = project != null && project.isPublished();

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
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "$abstract", "fatalErrors", "cost", "link", "published");
		dataset.put("manager", object.getManager().getUserAccount().getUsername());

		super.getResponse().addData(dataset);
	}

}
