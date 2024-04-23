
package acme.features.client.contracts;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contract.Contract;
import acme.roles.Client;

@Controller
public class ClientContractController extends AbstractController<Client, Contract> {

	@Autowired
	protected ClientContractListAllService	listAllService;

	@Autowired
	protected ClientContractShowService		showService;

	@Autowired
	protected ClientContractCreateService	createService;

	@Autowired
	protected ClientContractDeleteService	deleteService;

	@Autowired
	protected ClientContractUpdateService	updateService;

	@Autowired
	protected ClientContractPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listAllService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
