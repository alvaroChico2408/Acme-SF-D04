
package acme.features.developer.trainingSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainingSession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Developer developer;
		int developerRequestId;
		int trainingSessionId;
		TrainingSession trainingSession;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);
		developer = trainingSession == null ? null : trainingSession.getTrainingModule().getDeveloper();
		developerRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (developer != null)
			status = super.getRequest().getPrincipal().hasRole(developer) && developer.getId() == developerRequestId;
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
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link", "published");
		dataset.put("trainingModuleCode", object.getTrainingModule().getCode());

		super.getResponse().addData(dataset);
	}

}
