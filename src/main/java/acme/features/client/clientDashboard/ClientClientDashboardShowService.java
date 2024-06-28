
package acme.features.client.clientDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientClientDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int clientId;
		ClientDashboard dashboard;
		int numContract;
		int numProgressLogs;
		Integer numProgressLogsUnder25;
		Integer numProgressLogsBetween25and50;
		Integer numProgressLogsBetween50and75;
		Integer numProgressLogsAbove75;
		Double averageBudget;
		Double deviationBudget;
		Double minimumBudget;
		Double maximumBudget;

		clientId = super.getRequest().getPrincipal().getActiveRoleId();
		numContract = this.repository.findNumContract(clientId);
		numProgressLogs = this.repository.findNumProgressLogs(clientId);

		if (numProgressLogs >= 1) {
			numProgressLogsUnder25 = this.repository.findNumOfprogressLogsLess25(clientId).orElse(null);
			numProgressLogsBetween25and50 = this.repository.findNumOfProgressLogsWithRate25To50(clientId).orElse(null);
			numProgressLogsBetween50and75 = this.repository.findNumOfProgressLogsWithRate50To75(clientId).orElse(null);
			numProgressLogsAbove75 = this.repository.findNumOfProgressLogsWithRateOver75(clientId).orElse(null);
		} else {
			numProgressLogsUnder25 = null;
			numProgressLogsBetween25and50 = null;
			numProgressLogsBetween50and75 = null;
			numProgressLogsAbove75 = null;
		}

		if (numContract >= 2) {
			averageBudget = this.repository.findAverageContractBudget(clientId).orElse(null);
			deviationBudget = this.repository.findDeviationContractBudget(clientId).orElse(null);
		} else {
			averageBudget = null;
			deviationBudget = null;
		}

		minimumBudget = this.repository.findMinContractBudget(clientId).orElse(null);
		maximumBudget = this.repository.findMaxContractBudget(clientId).orElse(null);

		dashboard = new ClientDashboard();
		dashboard.setNumProgressLogsUnder25(numProgressLogsUnder25);
		dashboard.setNumProgressLogsBetween25and50(numProgressLogsBetween25and50);
		dashboard.setNumProgressLogsBetween50and75(numProgressLogsBetween50and75);
		dashboard.setNumProgressLogsAbove75(numProgressLogsAbove75);
		dashboard.setAverageBudget(averageBudget);
		dashboard.setDeviationBudget(deviationBudget);
		dashboard.setMinimumBudget(minimumBudget);
		dashboard.setMaximumBudget(maximumBudget);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "numProgressLogsUnder25", "numProgressLogsBetween25and50", "numProgressLogsBetween50and75", "numProgressLogsAbove75", "averageBudget", "deviationBudget", "minimumBudget", "maximumBudget");

		super.getResponse().addData(dataset);
	}

}
