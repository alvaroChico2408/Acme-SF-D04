
package acme.features.developer.trainingSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.trainingSession.TrainingSession;
import acme.roles.Developer;

@Controller
public class DeveloperTrainingSessionController extends AbstractController<Developer, TrainingSession> {

	// Internal state -----------------------------------------------

	@Autowired
	private DeveloperTrainingSessionListService		listService;

	@Autowired
	private DeveloperTrainingSessionCreateService	createService;

	@Autowired
	private DeveloperTrainingSessionDeleteService	deleteService;

	@Autowired
	private DeveloperTrainingSessionShowService		showService;

	@Autowired
	private DeveloperTrainingSessionPublishService	publishService;

	@Autowired
	private DeveloperTrainingSessionUpdateService	updateService;

	// Constructors -------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
