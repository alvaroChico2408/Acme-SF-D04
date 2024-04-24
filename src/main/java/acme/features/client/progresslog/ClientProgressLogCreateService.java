
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogCreateService extends AbstractService<Client, ProgressLog> {

	@Autowired
	protected ClientProgressLogRepository	repository;

	@Autowired
	protected AuxiliarService				auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ProgressLog object;
		Client client;
		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId()).orElse(null);
		object = new ProgressLog();
		object.setPublished(false);
		object.setClient(client);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "contract");
	}

	@Override
	public void validate(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog existing;
			existing = this.repository.findProgressLogsByRecordId(object.getRecordId());
			super.state(existing == null, "code", "client.progressLogs.form.error.recordId");
		}

		if (!super.getBuffer().getErrors().hasErrors("registrationMoment"))
			super.state(MomentHelper.isBefore(object.getRegistrationMoment(), MomentHelper.getCurrentMoment()), "instantiationMoment", "client.progressLogs.form.error.registrationMoment");

	}

	@Override
	public void perform(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "contract", "client", "published");

		final SelectChoices choices = new SelectChoices();
		Collection<Contract> contracts;
		int id = super.getRequest().getPrincipal().getActiveRoleId();
		contracts = this.repository.findPublishedContractsByClient(id);
		for (final Contract c : contracts)
			choices.add(Integer.toString(c.getId()), c.getCode(), false);
		dataset.put("contractsList", choices);
		super.getResponse().addData(dataset);
	}

}
