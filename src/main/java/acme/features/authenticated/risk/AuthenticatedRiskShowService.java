
package acme.features.authenticated.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.risk.Risk;

@Service
public class AuthenticatedRiskShowService extends AbstractService<Authenticated, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedRiskRepository repository;

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

		Dataset dataset;

		dataset = super.unbind(object, "reference", "type", "identificationDate", "impact", "probability", "description", "link");
		dataset.put("value", object.value());

		super.getResponse().addData(dataset);
	}

}
