
package acme.features.auditor.codeAudit;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.AuditType;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.entities.projects.Project;
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

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;

			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "auditor.codeAudit.form.error.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {
			Date executionDate = object.getExecutionDate();
			Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());

			super.state(MomentHelper.isAfterOrEqual(executionDate, this.lowestMoment), "executionDate", "auditor.codeAudit.form.error.executionDateError");
			for (AuditRecord ar : auditRecords)
				if (MomentHelper.isAfter(executionDate, ar.getStartDate())) {
					super.state(false, "executionDate", "auditor.codeAudit.form.error.executionDateAfterAuditRecordDate");
					break;
				}
		}
		if (!this.getBuffer().getErrors().hasErrors("correctiveActions")) {
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

		Dataset dataset;
		SelectChoices typeChoices;
		SelectChoices projectChoices;
		Mark mark;
		Collection<Project> publishedProjects;

		publishedProjects = this.repository.findAllPublishedProjects();
		typeChoices = SelectChoices.from(AuditType.class, object.getType());
		projectChoices = new SelectChoices();
		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));

		for (Project p : publishedProjects)
			if (object.getProject() != null && object.getProject().getId() == p.getId())
				projectChoices.add(Integer.toString(p.getId()), "Code: " + p.getCode() + " - " + "Title: " + p.getTitle(), true);
			else
				projectChoices.add(Integer.toString(p.getId()), "Code: " + p.getCode() + " - " + "Title: " + p.getTitle(), false);

		dataset = super.unbind(object, "code", "executionDate", "correctiveActions", "published", "link");
		dataset.put("auditor", object.getAuditor().getUserAccount().getUsername());
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("projects", projectChoices);
		dataset.put("mark", mark == null ? null : mark.getMark());

		super.getResponse().addData(dataset);
	}
}
