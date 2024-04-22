
package acme.features.developer.trainingModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.trainingModule.Difficulty;
import acme.entities.trainingModule.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrainingModule object;
		Developer developer;
		Project project;

		developer = this.repository.findDeveloperById(super.getRequest().getPrincipal().getActiveRoleId());
		project = this.repository.findProjectByCode("JLB-4567").orElse(null);
		object = new TrainingModule();
		object.setPublished(false);
		object.setDeveloper(developer);
		object.setCreationMoment(MomentHelper.getCurrentMoment());
		object.setProject(project);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		String details;

		details = super.getRequest().getData("description", String.class);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment");
		object.setDetails(details);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule tm;

			tm = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(tm == null, "code", "developer.trainingModule.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("creationMoment")) {
			TrainingModule tm;

			tm = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(tm.getDifficultyLevel() == null, "difficultyLevel", "developer.trainingModule.form.error.difficultyLevel");
		}

		if (!super.getBuffer().getErrors().hasErrors("details"))
			super.state(object.getDetails().length() <= 100, "details", "developer.trainingModule.form.error.details");

		if (!super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.trainingModule.form.error.updateMoment");

		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(object.getDetails().length() <= 255, "link", "developer.trainingModule.form.error.link");

		if (!super.getBuffer().getErrors().hasErrors("totalTime"))
			super.state(object.getDetails().length() >= 0, "totalTime", "developer.trainingModule.form.error.totalTime");

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Difficulty.class, object.getDifficultyLevel());

		dataset = super.unbind(object, "code", "creationMoment", "details", "updateMoment", "link", "totalTime", "published");
		dataset.put("developer", object.getDeveloper().getUserAccount().getUsername());
		dataset.put("difficultyLevel", choices.getSelected().getKey());
		dataset.put("difficulties", choices);

		super.getResponse().addData(dataset);
	}

}
