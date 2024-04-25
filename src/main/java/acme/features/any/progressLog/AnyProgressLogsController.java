
package acme.features.any.progressLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.contract.ProgressLog;

@Controller
public class AnyProgressLogsController extends AbstractController<Any, ProgressLog> {

	@Autowired
	protected AnyProgressLogsListService	listService;

	@Autowired
	protected AnyProgressLogsShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
