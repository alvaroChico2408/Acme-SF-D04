
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditType;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

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
		status = super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		CodeAudit object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

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
