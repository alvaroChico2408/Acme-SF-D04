
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.entities.trainingModule.TrainingModule;
import acme.entities.trainingSession.TrainingSession;
import acme.roles.Developer;
import spam.SpamFilter;

@Service
public class DeveloperTrainingSessionCreateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrainingSession object;
		TrainingModule trainingModule;
		int trainingModuleId;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		super.getResponse().addGlobal("trainingModuleId", trainingModuleId);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		object = new TrainingSession();
		object.setPublished(false);
		object.setTrainingModule(trainingModule);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link");
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

		if (!super.getBuffer().getErrors().hasErrors("startPeriod") && object.getStartPeriod() != null) {
			Date minimumDuration;
			Date start = object.getTrainingModule().getCreationMoment();

			minimumDuration = MomentHelper.deltaFromMoment(start, 1, ChronoUnit.WEEKS);
			super.state(MomentHelper.isAfter(object.getStartPeriod(), minimumDuration), "startPeriod", "developer.trainingSession.form.error.startPeriodTooClose");
		}

		if (!super.getBuffer().getErrors().hasErrors("endPeriod") && object.getEndPeriod() != null && object.getStartPeriod() != null) {
			Date start = object.getStartPeriod();
			Date minimumDuration = MomentHelper.deltaFromMoment(start, 1, ChronoUnit.WEEKS);

			super.state(MomentHelper.isAfter(object.getEndPeriod(), object.getStartPeriod()), "endPeriod", "developer.trainingSession.form.error.endPeriodAfter");
			super.state(MomentHelper.isAfter(object.getEndPeriod(), minimumDuration), "endPeriod", "developer.trainingSession.form.error.endPeriodTooShort");
		}

		if (!super.getBuffer().getErrors().hasErrors("endPeriod"))
			super.state(object.getEndPeriod() != null, "endPeriod", "developer.trainingSession.form.error.noEndPeriod");

		if (!super.getBuffer().getErrors().hasErrors("location")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getLocation()), "location", "developer.trainingSession.form.error.spam");
			super.state(object.getLocation().length() <= 75, "location", "developer.trainingSession.form.error.location");
		}

		if (!super.getBuffer().getErrors().hasErrors("instructor")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getInstructor()), "instructor", "developer.trainingSession.form.error.spam");
			super.state(object.getInstructor().length() <= 75, "instructor", "developer.trainingSession.form.error.instructor");
		}

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
