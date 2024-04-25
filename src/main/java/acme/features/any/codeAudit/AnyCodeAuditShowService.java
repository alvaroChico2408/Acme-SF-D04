
package acme.features.any.codeAudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditType;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;

@Service
public class AnyCodeAuditShowService extends AbstractService<Any, CodeAudit> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit object;

		codeAuditId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(codeAuditId);
		status = object != null && object.isPublished();

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

		Mark mark;
		Dataset dataset;
		SelectChoices typeChoices;

		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));
		typeChoices = SelectChoices.from(AuditType.class, object.getType());
		dataset = super.unbind(object, "code", "executionDate", "type", "correctiveActions", "published", "link");
		dataset.put("auditor", object.getAuditor().getUserAccount().getUsername());
		dataset.put("mark", mark == null ? null : mark.getMark());
		dataset.put("projectTitle", object.getProject().getTitle());
		dataset.put("projectCode", object.getProject().getCode());
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);

	}
}
