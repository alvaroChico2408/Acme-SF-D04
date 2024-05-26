
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
import acme.entities.projects.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Auditor;
import spam.SpamFilter;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository	repository;

	private Date						lowestMoment	= Date.from(Instant.parse("1999-12-31T23:00:00Z"));

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;
		Date executionDate;

		executionDate = MomentHelper.getCurrentMoment();
		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new CodeAudit();
		object.setPublished(false);
		object.setAuditor(auditor);
		object.setExecutionDate(executionDate);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		Project project;

		project = this.repository.findOneProjectByCode(super.getRequest().getData("projectCode", String.class));

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link", "projectCode");
		object.setProject(project);
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;

			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null, "code", "auditor.codeAudit.form.error.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {
			Date executionDate = object.getExecutionDate();

			super.state(MomentHelper.isAfterOrEqual(executionDate, this.lowestMoment), "executionDate", "auditor.codeAudit.form.error.executionDateError");
		}
		if (!super.getBuffer().getErrors().hasErrors("projectCode")) {
			Project project = this.repository.findOneProjectByCode(super.getRequest().getData("projectCode", String.class));

			super.state(project != null, "projectCode", "auditor.codeAudit.form.error.projectNotFound");
			if (project != null)
				super.state(project.isPublished(), "projectCode", "auditor.codeAudit.form.error.projectCodeError");
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

		Project project;
		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		project = this.repository.findOneProjectByCode(super.getRequest().getData("projectCode", String.class));

		assert project != null;

		object.setProject(project);
		object.setPublished(false);
		object.setAuditor(auditor);

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(AuditType.class, object.getType());
		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "published", "link");
		dataset.put("auditor", object.getAuditor().getUserAccount().getUsername());
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);
	}
}
