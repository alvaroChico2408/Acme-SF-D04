
package acme.features.developer.developerDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int developerId;
		DeveloperDashboard dashboard;
		Integer numTrainingModules;
		Integer numTrainingModulesWithUpdateMoment;
		Integer numTrainingSessionsWithLink;
		Double averageTimeOfTrainingModule;
		Double deviationTimeOfTrainingModule;
		Integer minTimeOfTrainingModule;
		Integer maxTimeOfTrainingModule;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		numTrainingModules = this.repository.findNumTrainingModules(developerId).orElse(null);
		numTrainingModulesWithUpdateMoment = this.repository.findNumTrainingModulesWithUpdateMoment(developerId).orElse(null);
		numTrainingSessionsWithLink = this.repository.findNumTrainingSessionsWithLink(developerId).orElse(null);
		if (numTrainingModules >= 2) {
			averageTimeOfTrainingModule = this.repository.findAverageTimeOfTrainingModule(developerId).orElse(null);
			deviationTimeOfTrainingModule = this.repository.findDeviationTimeOfTrainingModule(developerId).orElse(null);
		} else {
			averageTimeOfTrainingModule = null;
			deviationTimeOfTrainingModule = null;
		}
		minTimeOfTrainingModule = this.repository.findMinTimeOfTrainingModule(developerId).orElse(null);
		maxTimeOfTrainingModule = this.repository.findMaxTimeOfTrainingModule(developerId).orElse(null);

		dashboard = new DeveloperDashboard();
		dashboard.setNumTrainingModulesWithUpdateMoment(numTrainingModulesWithUpdateMoment);
		dashboard.setNumTrainingSessionsWithLink(numTrainingSessionsWithLink);
		dashboard.setAverageTimeOfTrainingModule(averageTimeOfTrainingModule);
		dashboard.setDeviationTimeOfTrainingModule(deviationTimeOfTrainingModule);
		dashboard.setMinTimeOfTrainingModule(minTimeOfTrainingModule);
		dashboard.setMaxTimeOfTrainingModule(maxTimeOfTrainingModule);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "numTrainingModulesWithUpdateMoment", "numTrainingSessionsWithLink", "averageTimeOfTrainingModule", "deviationTimeOfTrainingModule", "deviationTimeOfTrainingModule", "minTimeOfTrainingModule",
			"maxTimeOfTrainingModule");

		super.getResponse().addData(dataset);
	}
}
