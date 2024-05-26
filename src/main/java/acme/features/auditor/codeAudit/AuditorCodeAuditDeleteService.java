
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		CodeAudit codeAudit;
		Auditor auditor;

		masterId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		auditor = codeAudit == null ? null : codeAudit.getAuditor();
		status = super.getRequest().getPrincipal().hasRole(auditor) && !codeAudit.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link", "projectCode");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		Collection<AuditRecord> auditRecords;

		auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());

		this.repository.deleteAll(auditRecords);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		//		Mark mark;
		//		Dataset dataset;
		//		SelectChoices typeChoices;
		//
		//		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));
		//		typeChoices = SelectChoices.from(AuditType.class, object.getType());
		//		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "published", "link");
		//		dataset.put("auditor", object.getAuditor().getUserAccount().getUsername());
		//		dataset.put("mark", mark == null ? null : mark.getMark());
		//		dataset.put("projectTitle", object.getProject().getTitle());
		//		dataset.put("projectCode", object.getProject().getCode());
		//		dataset.put("type", typeChoices.getSelected().getKey());
		//		dataset.put("types", typeChoices);
		//
		//		super.getResponse().addData(dataset);

	}

}
