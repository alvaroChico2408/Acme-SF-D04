
package acme.features.any.codeAudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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

		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));
		dataset = super.unbind(object, "code", "executionDate", "correctiveActions", "published", "link");
		dataset.put("auditor", object.getAuditor().getUserAccount().getUsername());
		dataset.put("mark", mark == null ? null : mark.getMark());
		dataset.put("projectTitle", object.getProject().getTitle());
		dataset.put("projectCode", object.getProject().getCode());
		dataset.put("type", object.getType());

		super.getResponse().addData(dataset);

	}
}
