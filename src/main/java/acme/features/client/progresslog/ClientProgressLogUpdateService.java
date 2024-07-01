
package acme.features.client.progresslog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;
import spam.SpamFilter;

@Service
public class ClientProgressLogUpdateService extends AbstractService<Client, ProgressLog> {

	@Autowired
	protected ClientProgressLogRepository	repository;

	@Autowired
	protected AuxiliarService				auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		Contract contract;
		ProgressLog progressLog;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractByProgressLogId(progressLogId);
		progressLog = this.repository.findProgressLogsById(progressLogId);
		status = contract != null && contract.isPublished() && !progressLog.isPublished() && super.getRequest().getPrincipal().hasRole(contract.getClient())
			&& contract.getClient().getUserAccount().getUsername().equals(super.getRequest().getPrincipal().getUsername());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		object = new ProgressLog();
		object.setPublished(false);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		ProgressLog object2;
		int id;
		id = super.getRequest().getData("id", int.class);
		object2 = this.repository.findProgressLogsById(id);
		object.setContract(object2.getContract());
		object.setRegistrationMoment(object2.getRegistrationMoment());
		Client client;
		client = this.repository.findClientById(object.getContract().getClient().getId()).orElse(null);
		object.setClient(client);
		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");

	}

	@Override
	public void validate(final ProgressLog object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog existing;
			existing = this.repository.findProgressLogsByRecordId(object.getRecordId());
			final ProgressLog progressLog2 = object.getRecordId().equals("") || object.getRecordId() == null ? null : this.repository.findProgressLogsById(object.getId());
			super.state(existing == null || progressLog2.equals(existing), "recordId", "client.progressLogs.form.error.recordId");
		}

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
