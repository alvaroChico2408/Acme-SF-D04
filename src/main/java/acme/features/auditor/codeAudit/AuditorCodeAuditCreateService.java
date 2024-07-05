
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

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new CodeAudit();
		object.setPublished(false);
		object.setAuditor(auditor);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		Project project;
		int projectId;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		object.setProject(project);

		super.bind(object, "code", "executionDate", "type", "correctiveActions", "link");
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

		if (!this.getBuffer().getErrors().hasErrors("correctiveActions")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getCorrectiveActions()), "correctiveAction", "auditor.codeAudit.form.error.spam");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setPublished(false);
		object.setAuditor(auditor);

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		SelectChoices typeChoices;
		SelectChoices projectChoices;
		Collection<Project> publishedProjects;

		publishedProjects = this.repository.findAllPublishedProjects();
		typeChoices = SelectChoices.from(AuditType.class, object.getType());
		projectChoices = new SelectChoices();

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

		super.getResponse().addData(dataset);
	}
}
