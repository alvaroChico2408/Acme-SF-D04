
package acme.features.any.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;

@Service
public class AnyAuditRecordShowService extends AbstractService<Any, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		AuditRecord object;

		auditRecordId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(auditRecordId);

		status = object != null && object.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		CodeAudit codeAudit;

		codeAudit = object.getCodeAudit();

		dataset = super.unbind(object, "code", "startDate", "endDate", "link", "published");
		dataset.put("codeAuditCode", codeAudit.getCode());
		dataset.put("mark", object.getMark().getMark());

		super.getResponse().addData(dataset);
	}
}
