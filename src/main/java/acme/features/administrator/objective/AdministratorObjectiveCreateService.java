
package acme.features.administrator.objective;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.objectives.Objective;
import acme.entities.objectives.Priority;

@Service
public class AdministratorObjectiveCreateService extends AbstractService<Administrator, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorObjectiveRepository	repository;

	private Date								lowestMoment	= Date.from(Instant.parse("1999-12-31T23:00:00Z"));
	private Date								topestMoment	= Date.from(Instant.parse("2200-12-31T23:59:59Z"));

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Objective object;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		object = new Objective();
		object.setInstantiationMoment(instantiationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Objective object) {
		assert object != null;

		super.bind(object, "title", "description", "priority", "status", "executionPeriodStart", //
			"executionPeriodFinish", "link");
	}

	@Override
	public void validate(final Objective object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

		if (!super.getBuffer().getErrors().hasErrors("executionPeriodStart")) {
			Date executionPeriodStart = object.getExecutionPeriodStart();
			Date instantiationMoment = object.getInstantiationMoment();

			super.state(MomentHelper.isAfter(executionPeriodStart, instantiationMoment), "executionPeriodStart", //
				"administrator.objective.form.error.executionPeriodStart");
			super.state(MomentHelper.isAfterOrEqual(executionPeriodStart, this.lowestMoment) && MomentHelper.isBeforeOrEqual(executionPeriodStart, this.topestMoment), //
				"executionPeriodStart", "administrator.objective.form.error.badExecutionPeriodStart");
		}

		if (!super.getBuffer().getErrors().hasErrors("executionPeriodFinish")) {
			Date executionPeriodFinish = object.getExecutionPeriodFinish();
			Date executionPeriodStart = object.getExecutionPeriodStart();
			Date instantiationMoment = object.getInstantiationMoment();

			super.state(MomentHelper.isAfter(executionPeriodFinish, instantiationMoment) && MomentHelper.isAfter(executionPeriodFinish, executionPeriodStart), //
				"executionPeriodFinish", "administrator.objective.form.error.executionPeriodFinish");
			super.state(MomentHelper.isAfterOrEqual(executionPeriodFinish, this.lowestMoment) && MomentHelper.isBeforeOrEqual(executionPeriodFinish, this.topestMoment), //
				"executionPeriodFinish", "administrator.objective.form.error.badExecutionPeriodFinish");
		}

		if (!super.getBuffer().getErrors().hasErrors("executionPeriodStart") && !super.getBuffer().getErrors().hasErrors("executionPeriodFinish")) {
			Date executionPeriodStart = object.getExecutionPeriodStart();
			Date executionPeriodFinish = object.getExecutionPeriodFinish();

			super.state(this.isPassedOneHourAtLeast(executionPeriodStart, executionPeriodFinish), "executionPeriodFinish", //
				"administrator.objective.form.error.NotTimeEnough");
		}
	}

	private boolean isPassedOneHourAtLeast(final Date date1, final Date date2) {
		boolean res = false;

		long diffInMillies = date2.getTime() - date1.getTime();
		long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (diffInHours >= 1L)
			res = true;

		return res;
	}

	@Override
	public void perform(final Objective object) {
		assert object != null;

		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(instantiationMoment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Priority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "priority", "status", "executionPeriodStart", //
			"executionPeriodFinish", "link");
		dataset.put("confirmation", false);
		dataset.put("priority", choices.getSelected().getKey());
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}

}
