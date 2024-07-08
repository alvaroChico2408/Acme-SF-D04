
package acme.features.auditor.auditRecord;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository	repository;

	private Date							lowestMoment	= Date.from(Instant.parse("1999-12-31T23:00:00Z"));
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

		super.bind(object, "code", "startDate", "endDate", "mark", "link");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord existing;

			existing = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "auditor.auditRecord.form.error.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("startDate")) {
			Date startDate = object.getStartDate();
			Date minimumEnd = MomentHelper.deltaFromMoment(startDate, 1, ChronoUnit.HOURS);

			super.state(MomentHelper.isAfterOrEqual(startDate, this.lowestMoment), "startDate", "auditor.auditRecord.form.error.startDateError");
			super.state(MomentHelper.isAfterOrEqual(startDate, object.getCodeAudit().getExecutionDate()), "startDate", "auditor.auditrecord.form.error.startDateBeforeExecutionDate");
			super.state(MomentHelper.isBeforeOrEqual(minimumEnd, MomentHelper.getCurrentMoment()), "startDate", "auditor.auditRecord.form.error.startDateErrorTooLate");
		}
		if (!super.getBuffer().getErrors().hasErrors("endDate")) {
			Date endDate = object.getEndDate();
			Date minimumEnd1 = MomentHelper.deltaFromMoment(object.getCodeAudit().getExecutionDate(), 1, ChronoUnit.HOURS);

			super.state(MomentHelper.isAfterOrEqual(endDate, minimumEnd1), "endDate", "auditor.auditRecord.form.error.tooEarly");

			if (object.getStartDate() != null) {
				Date startDate = object.getStartDate();
				Date minimumEnd2 = MomentHelper.deltaFromMoment(startDate, 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(endDate, minimumEnd2), "endDate", "auditor.auditRecord.form.error.notTimeEnough");
			}

		}

	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;
		CodeAudit codeAudit;

		choices = SelectChoices.from(Mark.class, object.getMark());
		codeAudit = object.getCodeAudit();

		dataset = super.unbind(object, "code", "startDate", "endDate", "link", "published");
		dataset.put("masterId", object.getCodeAudit().getId());
		dataset.put("codeAuditCode", codeAudit.getCode());
		dataset.put("mark", choices.getSelected().getKey());
		dataset.put("marks", choices);

		super.getResponse().addData(dataset);
	}
}
