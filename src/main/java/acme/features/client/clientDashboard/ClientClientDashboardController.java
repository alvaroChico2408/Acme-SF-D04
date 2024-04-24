
package acme.features.client.clientDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Controller
public class ClientClientDashboardController extends AbstractController<Client, ClientDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientClientDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
