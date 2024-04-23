
package acme.features.auditor.auditRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.codeAudit.AuditRecord;
import acme.roles.Auditor;

@Controller
public class AuditorAuditRecordController extends AbstractController<Auditor, AuditRecord> {

	@Autowired
	AuditorAuditRecordListService		listService;

	@Autowired
	AuditorAuditRecordShowService		showService;

	@Autowired
	AuditorAuditRecordCreateService		createService;

	@Autowired
	AuditorAuditRecordUpdateService		updateService;

	@Autowired
	AuditorAuditRecordDeleteService		deleteService;

	@Autowired
	AuditorAuditRecordPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
