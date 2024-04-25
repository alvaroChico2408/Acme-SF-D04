
package acme.features.any.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;

@Service
public class AnyAuditRecordListService extends AbstractService<Any, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		CodeAudit object;

		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findOneCodeAuditById(masterId);
		status = object != null && object.isPublished();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<AuditRecord> auditRecords;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(masterId);

		super.getBuffer().addData(auditRecords);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startDate", "endDate", "mark");

		super.getResponse().addData(dataset);

	}
}
