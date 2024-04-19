
package acme.features.manager.managerDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Priority;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId;
		ManagerDashboard dashboard;
		int numUserStories;
		int numProjects;
		int numMustUserStories;
		int numShouldUserStories;
		int numCouldUserStories;
		int numWontUserStories;
		Double averageEstimatedCostUserStories;
		Double deviationEstimatedCostUserStories;
		Integer minEstimatedCostUserStories;
		Integer maxEstimatedCostUserStories;
		Double averageCostProjects;
		Double deviationCostProjects;
		Integer minCostProjects;
		Integer maxCostProjects;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		numUserStories = this.repository.findNumUserStoriesStatics(managerId);
		numProjects = this.repository.findNumProjectStatics(managerId);
		numMustUserStories = this.repository.findNumUserStoriesByPriority(managerId, Priority.MUST);
		numShouldUserStories = this.repository.findNumUserStoriesByPriority(managerId, Priority.SHOULD);
		numCouldUserStories = this.repository.findNumUserStoriesByPriority(managerId, Priority.COULD);
		numWontUserStories = this.repository.findNumUserStoriesByPriority(managerId, Priority.WONT);
		if (numUserStories >= 2) {
			averageEstimatedCostUserStories = this.repository.findAverageEstimatedCostUserStories(managerId).orElse(null);
			deviationEstimatedCostUserStories = this.repository.findDeviationEstimatedCostUserStories(managerId).orElse(null);
		} else {
			averageEstimatedCostUserStories = null;
			deviationEstimatedCostUserStories = null;
		}
		minEstimatedCostUserStories = this.repository.findMinEstimatedCostUserStories(managerId).orElse(null);
		maxEstimatedCostUserStories = this.repository.findMaxEstimatedCostUserStories(managerId).orElse(null);
		if (numProjects >= 2) {
			averageCostProjects = this.repository.findAverageCostProjects(managerId).orElse(null);
			deviationCostProjects = this.repository.findDeviationCostProjects(managerId).orElse(null);
		} else {
			averageCostProjects = null;
			deviationCostProjects = null;
		}
		minCostProjects = this.repository.findMinCostProjects(managerId).orElse(null);
		maxCostProjects = this.repository.findMaxCostProjects(managerId).orElse(null);

		dashboard = new ManagerDashboard();
		dashboard.setNumMustUserStories(numMustUserStories);
		dashboard.setNumShouldUserStories(numShouldUserStories);
		dashboard.setNumCouldUserStories(numCouldUserStories);
		dashboard.setNumWontUserStories(numWontUserStories);
		dashboard.setAverageEstimatedCostUserStories(averageEstimatedCostUserStories);
		dashboard.setDeviationEstimatedCostUserStories(deviationEstimatedCostUserStories);
		dashboard.setMinEstimatedCostUserStories(minEstimatedCostUserStories);
		dashboard.setMaxEstimatedCostUserStories(maxEstimatedCostUserStories);
		dashboard.setAverageCostProjects(averageCostProjects);
		dashboard.setDeviationCostProjects(deviationCostProjects);
		dashboard.setMinCostProjects(minCostProjects);
		dashboard.setMaxCostProjects(maxCostProjects);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "numMustUserStories", "numShouldUserStories", "numCouldUserStories", //
			"numWontUserStories", "averageEstimatedCostUserStories", "deviationEstimatedCostUserStories", //
			"minEstimatedCostUserStories", "maxEstimatedCostUserStories", "averageCostProjects", //
			"deviationCostProjects", "minCostProjects", "maxCostProjects");

		super.getResponse().addData(dataset);
	}

}
