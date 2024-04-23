
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		CodeAudit codeAudit;

		masterId = super.getRequest().getData("masterId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		status = codeAudit != null && super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

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

	@Override
	public void unbind(final Collection<AuditRecord> objects) {
		assert objects != null;

		int masterId;
		CodeAudit codeAudit;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		showCreate = !codeAudit.isPublished() && super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
