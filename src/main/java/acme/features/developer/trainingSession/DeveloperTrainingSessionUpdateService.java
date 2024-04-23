
package acme.features.developer.trainingSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.trainingSession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int developerRequestId;
		Developer developer;
		int trainingSessionId;
		TrainingSession trainingSession;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);
		developer = trainingSession == null ? null : trainingSession.getTrainingModule().getDeveloper();
		developerRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (developer != null)
			status = !trainingSession.isPublished() && super.getRequest().getPrincipal().hasRole(developer) && developer.getId() == developerRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int trainingSessionId;

		trainingSessionId = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingSessionById(trainingSessionId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link", "published");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession ts;

			ts = this.repository.findTrainingSessionByCode(object.getCode());
			super.state(ts == null || ts.equals(object), "code", "developer.trainingSession.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startPeriod"))
			super.state(object.getStartPeriod() != null, "startPeriod", "developer.trainingSession.form.error.startPeriod");

		if (!super.getBuffer().getErrors().hasErrors("endPeriod") && object.getEndPeriod() != null)
			super.state(MomentHelper.isAfter(object.getEndPeriod(), object.getStartPeriod()), "endPeriod", "developer.trainingSession.form.error.endPeriodAfter");

		if (!super.getBuffer().getErrors().hasErrors("endPeriod"))
			super.state(object.getEndPeriod() != null, "endPeriod", "developer.trainingSession.form.error.noEndPeriod");

		if (!super.getBuffer().getErrors().hasErrors("location"))
			super.state(object.getLocation().length() <= 75, "location", "developer.trainingSession.form.error.location");

		if (!super.getBuffer().getErrors().hasErrors("instructor"))
			super.state(object.getInstructor().length() <= 75, "instructor", "developer.trainingSession.form.error.instructor");

		if (!super.getBuffer().getErrors().hasErrors("contactEmail"))
			super.state(object.getContactEmail().length() <= 255, "contactEmail", "developer.trainingSession.form.error.contactEmail");

		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(object.getLink().length() <= 255, "link", "developer.trainingSession.form.error.link");

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link", "published");
		dataset.put("trainingModuleCode", object.getTrainingModule().getCode());

		super.getResponse().addData(dataset);
	}

}
