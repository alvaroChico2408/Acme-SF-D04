
package acme.features.administrator.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.risk.Risk;
import acme.entities.risk.RiskType;
import acme.entities.systemConfiguration.SystemConfiguration;
import spam.SpamFilter;

@Service
public class AdministratorRiskCreateService extends AbstractService<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Risk object;

		object = new Risk();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Risk object) {
		assert object != null;

		super.bind(object, "reference", "type", "identificationDate", "impact", "probability", "description", "link");

	}

	@Override
	public void validate(final Risk object) {
		assert object != null;

		if (!this.getBuffer().getErrors().hasErrors("reference")) {
			Risk risk;

			risk = this.repository.findRiskByReference(object.getReference());
			super.state(risk == null, "reference", "administrator.risk.form.error.duplicated");
		}

		if (!this.getBuffer().getErrors().hasErrors("type"))
			super.state(object.getType() != null, "type", "administrator.risk.form.error.notype");

		if (!this.getBuffer().getErrors().hasErrors("impact"))
			super.state(object.getImpact() >= 0 || object.getImpact() <= 100, "impact", "administrator.risk.form.error.impact");

		if (!this.getBuffer().getErrors().hasErrors("probability"))
			super.state(object.getProbability() >= 0 || object.getProbability() <= 1, "probability", "administrator.risk.form.error.probability");

		if (!this.getBuffer().getErrors().hasErrors("description") && object.getDescription() != null) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getDescription()), "description", "administrator.risk.form.error.spam");
			super.state(object.getDescription().length() <= 100, "description", "administrator.risk.form.error.description");
		}

		if (!this.getBuffer().getErrors().hasErrors("link") && object.getDescription() != null)
			super.state(object.getDescription().length() <= 255, "link", "administrator.risk.form.error.link");

	}

	@Override
	public void perform(final Risk object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(RiskType.class, object.getType());
		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "description", "link");
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("$value", object.value());

		super.getResponse().addData(dataset);
	}
}
