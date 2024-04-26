
package acme.features.auditor.codeAudit;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditType;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Auditor;
import spam.SpamFilter;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository	repository;

	private Date						lowestMoment	= Date.from(Instant.parse("1999-12-31T23:00:00Z"));

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
		status = super.getRequest().getPrincipal().hasRole(auditor);

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

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {
			Date executionDate = object.getExecutionDate();

			super.state(MomentHelper.isAfter(executionDate, this.lowestMoment), "executionDate", "auditor.codeAudit.form.error.executionDateError");
		}
		if (!this.getBuffer().getErrors().hasErrors("slogan")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getCorrectiveActions()), "correctiveAction", "auditor.codeAudit.form.error.spam");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		SelectChoices choices;
		int auditorId;
		Dataset dataset;
		Mark mark;

		choices = SelectChoices.from(AuditType.class, object.getType());
		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));

		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "published", "link");
		dataset.put("auditor", this.repository.findOneAuditorById(auditorId).getAuthorityName());
		dataset.put("mark", mark == null ? null : mark.getMark());
		dataset.put("projectCode", object.getProject().getCode());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
