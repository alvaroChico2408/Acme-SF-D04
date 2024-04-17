
package acme.features.manager.associatedWith;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.AssociatedWith;
import acme.roles.Manager;

@Controller
public class ManagerAssociatedWithController extends AbstractController<Manager, AssociatedWith> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssociatedWithCreateService createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}

}
