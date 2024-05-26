
package acme.features.developer.trainingModule;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.entities.trainingModule.Difficulty;
import acme.entities.trainingModule.TrainingModule;
import acme.roles.Developer;
import spam.SpamFilter;

@Service
public class DeveloperTrainingModuleUpdateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected DeveloperTrainingModuleRepository repository;

	// AbstractService interface -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int developerRequestId;
		Developer developer;
		int developerId;
		TrainingModule trainingModule;

		developerId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(developerId);
		developer = trainingModule == null ? null : trainingModule.getDeveloper();
		developerRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (developer != null)
			status = !trainingModule.isPublished() && super.getRequest().getPrincipal().hasRole(developer) //
				&& developer.getId() == developerRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		Project project;
		int projectId;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);

		object.setProject(project);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule tm;
			tm = this.repository.findTrainingModuleByCode(object.getCode());

			super.state(tm == null || tm.equals(object), "code", "developer.trainingModule.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("difficultyLevel"))
			super.state(object.getDifficultyLevel() != null, "difficultyLevel", "developer.trainingModule.form.error.difficultyLevel");

		if (!super.getBuffer().getErrors().hasErrors("details")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(object.getDetails().length() <= 100, "details", "developer.trainingModule.form.error.details");
			super.state(!spam.isSpam(object.getDetails()), "details", "developer.trainingModule.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("creationMoment"))
			super.state(object.getCreationMoment() != null, "creationMoment", "developer.trainingModule.form.error.noCreationMoment");

		if (!super.getBuffer().getErrors().hasErrors("creationMoment") && object.getCreationMoment() != null)
			super.state(MomentHelper.isAfter(object.getCreationMoment(), Date.valueOf(LocalDate.of(2000, 1, 1))), "creationMoment", "developer.trainingModule.form.error.creationMoment");

		if (!super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(object.getUpdateMoment() != null, "updateMoment", "developer.trainingModule.form.error.noUpdateMoment");

		if (!super.getBuffer().getErrors().hasErrors("updateMoment") && object.getUpdateMoment() != null && object.getCreationMoment() != null)
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
		SelectChoices projectChoices = new SelectChoices();
		Collection<Project> projects;
		projects = this.repository.findPublishedProjects();
		Dataset dataset;

		for (final Project c : projects)
			if (object.getProject() != null && object.getProject().getId() == c.getId())
				projectChoices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), true);
			else
				projectChoices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), false);

		choices = SelectChoices.from(Difficulty.class, object.getDifficultyLevel());

		dataset = super.unbind(object, "code", "creationMoment", "details", "updateMoment", "link", "totalTime", "published");
		dataset.put("developer", object.getDeveloper().getUserAccount().getUsername());
		dataset.put("difficultyLevel", choices.getSelected().getKey());
		dataset.put("difficulties", choices);
		dataset.put("projects", projectChoices);

		super.getResponse().addData(dataset);
	}
}
