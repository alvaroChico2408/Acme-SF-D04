
package acme.features.client.progresslog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contract.ProgressLog;
import acme.roles.Client;

@Controller
public class ClientProgressLogController extends AbstractController<Client, ProgressLog> {

	@Autowired
	protected ClientProgressLogListService		listService;

	@Autowired
	protected ClientProgressLogShowService		showService;

	@Autowired
	protected ClientProgressLogCreateService	createService;

	@Autowired
	protected ClientProgressLogUpdateService	updateService;

	@Autowired
	protected ClientProgressLogPublishService	publishService;

	@Autowired
	protected ClientProgressLogDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
