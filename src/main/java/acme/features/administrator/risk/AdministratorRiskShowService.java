
package acme.features.administrator.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.risk.Risk;
import acme.entities.risk.RiskType;

@Service
public class AdministratorRiskShowService extends AbstractService<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int riskId;
		Risk risk;

		riskId = super.getRequest().getData("id", int.class);
		risk = this.repository.findRiskById(riskId);
		status = risk != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Risk object;
		int riskId;

		riskId = super.getRequest().getData("id", int.class);
		object = this.repository.findRiskById(riskId);

		super.getBuffer().addData(object);
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
