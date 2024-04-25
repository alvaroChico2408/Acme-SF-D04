
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;

@Service
public class AnyCodeAuditListService extends AbstractService<Any, CodeAudit> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<CodeAudit> objects;

		objects = this.repository.findAllCodeAuditsPublished();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		Mark mark;

		mark = object.getMark(this.repository.findManyMarksByCodeAuditId(object.getId()));

		dataset = super.unbind(object, "code", "executionDate", "type");
		dataset.put("mark", mark == null ? null : mark.getMark());

		super.getResponse().addData(dataset);
	}

}
