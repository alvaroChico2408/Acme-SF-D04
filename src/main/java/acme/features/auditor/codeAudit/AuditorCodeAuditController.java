
package acme.features.auditor.codeAudit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.codeAudit.CodeAudit;
import acme.roles.Auditor;

@Controller
public class AuditorCodeAuditController extends AbstractController<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditListMineService	listMineService;

	@Autowired
	private AuditorCodeAuditShowService		showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
	}
}
