
package acme.features.authenticated.objective;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.objectives.Objective;

@Service
public class AuthenticatedObjectiveShowService extends AbstractService<Authenticated, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedObjectiveRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int objectiveId;
		Objective objective;

		objectiveId = super.getRequest().getData("id", int.class);
		objective = this.repository.findObjectiveById(objectiveId);
		status = objective != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Objective object;
		int objectiveId;

		objectiveId = super.getRequest().getData("id", int.class);
		object = this.repository.findObjectiveById(objectiveId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", //
			"status", "executionPeriodStart", "executionPeriodFinish", "link");

		super.getResponse().addData(dataset);
	}

}
