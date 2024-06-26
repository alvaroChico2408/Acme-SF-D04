
package acme.features.client.progresslog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;
import spam.SpamFilter;

@Service
public class ClientProgressLogCreateService extends AbstractService<Client, ProgressLog> {

	@Autowired
	protected ClientProgressLogRepository	repository;

	@Autowired
	protected AuxiliarService				auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Contract contract;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);
		contract = this.repository.findContractById(masterId);
		status = contract != null && contract.isPublished() && contract.getClient().getUserAccount().getUsername().equals(super.getRequest().getPrincipal().getUsername()) && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		Client client;
		int masterId;
		Contract contract;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);
		contract = this.repository.findContractById(masterId);
		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId()).orElse(null);
		object = new ProgressLog();
		object.setPublished(false);
		object.setClient(client);
		object.setContract(contract);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");

		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog existing;
			existing = this.repository.findProgressLogsByRecordId(object.getRecordId());
			super.state(existing == null, "recordId", "client.progressLogs.form.error.recordId");
		}

		if (!super.getBuffer().getErrors().hasErrors("registrationMoment")) {
			Date maxDate = new Date(4102441199000L); // 2099/12/31 23:59
			super.state(MomentHelper.isBeforeOrEqual(object.getRegistrationMoment(), maxDate), "registrationMoment", "client.progressLogs.form.error.moment");
		}
		if (!super.getBuffer().getErrors().hasErrors("registrationMoment"))
			super.state(MomentHelper.isAfterOrEqual(object.getRegistrationMoment(), object.getContract().getInstantiationMoment()), "registrationMoment", "client.progressLogs.form.error.moment2");

		SystemConfiguration sc = this.repository.findSystemConfiguration();
		SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

		if (!super.getBuffer().getErrors().hasErrors("comment"))
			super.state(!spam.isSpam(object.getComment()), "comment", "client.progressLogs.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("responsiblePerson"))
			super.state(!spam.isSpam(object.getResponsiblePerson()), "responsiblePerson", "client.progressLogs.form.error.spam");

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

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "published");
		dataset.put("contractCode", object.getContract().getCode());

		super.getResponse().addData(dataset);
	}

}
