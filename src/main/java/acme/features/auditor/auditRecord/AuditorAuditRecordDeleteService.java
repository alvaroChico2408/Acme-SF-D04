
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		AuditRecord auditRecord;

		id = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findOneAuditRecordById(id);
		status = auditRecord != null && !auditRecord.isPublished() && super.getRequest().getPrincipal().hasRole(auditRecord.getCodeAudit().getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		AuditRecord object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}
	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "startDate", "endDate", "mark", "link", "published");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		//
		//		SelectChoices choices;
		//		Dataset dataset;
		//		CodeAudit codeAudit;
		//
		//		choices = SelectChoices.from(Mark.class, object.getMark());
		//		codeAudit = object.getCodeAudit();
		//
		//		dataset = super.unbind(object, "code", "startDate", "endDate", "link", "published");
		//		dataset.put("masterId", object.getCodeAudit().getId());
		//		dataset.put("codeAuditCode", codeAudit.getCode());
		//		dataset.put("mark", choices.getSelected().getKey());
		//		dataset.put("marks", choices);
		//
		//		super.getResponse().addData(dataset);
	}
}
