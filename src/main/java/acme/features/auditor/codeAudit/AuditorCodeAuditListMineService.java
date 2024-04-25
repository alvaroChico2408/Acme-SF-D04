
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditListMineService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<CodeAudit> codeAudits;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		codeAudits = this.repository.findManyCodeAuditsByAuditorId(principal.getActiveRoleId());

		super.getBuffer().addData(codeAudits);
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
