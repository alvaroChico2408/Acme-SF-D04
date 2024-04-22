
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.forms.AdministratorDashboard;

@Service
public class AdministratorAdministratorDashboardShowService extends AbstractService<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAdministratorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AdministratorDashboard dashboard;
		int numManagerPrincipals;
		int numClientPrincipals;
		int numDeveloperPrincipals;
		int numSponsorPrincipals;
		int numAuditorPrincipals;
		Double ratioNoticesWithEmailAndLink;
		Double ratioCriticalObjectives;
		Double ratioNonCriticalObjectives;
		Double averageRisksValues;
		Double minRisksValues;
		Double maxRisksValues;
		Double standardDeviationRisksValues;
		Double averagePostedClaimsLast10Weeks;
		Double minPostedClaimsLast10Weeks;
		Double maxPostedClaimsLast10Weeks;
		Double standardDeviationPostedClaimsLast10Weeks;

		numManagerPrincipals = this.repository.findNumManagers();
		numClientPrincipals = this.repository.findNumClients();
		numDeveloperPrincipals = this.repository.findNumDevelopers();
		numSponsorPrincipals = this.repository.findNumSponsors();
		numAuditorPrincipals = this.repository.findNumAuditors();

		// Notices ---------------------------------------------------------

		Double numNotices = this.repository.findNumNotices();
		if (numNotices >= 1) {
			Double numNoticesWithEmailAndLink = this.repository.findNumNoticesWithEmailAndLink();
			ratioNoticesWithEmailAndLink = numNoticesWithEmailAndLink / numNotices;
		} else
			ratioNoticesWithEmailAndLink = null;

		// Critical objectives ---------------------------------------------------------

		Double numObjetives = this.repository.findNumObjetives();
		if (numObjetives >= 1) {
			Double numCriticalObjetives = this.repository.findNumCriticalObjetives();
			double numNonCriticalObjetives = numObjetives - numCriticalObjetives;
			ratioCriticalObjectives = (double) (numCriticalObjetives / numObjetives);
			ratioNonCriticalObjectives = (double) (numNonCriticalObjetives / numObjetives);
		} else {
			ratioCriticalObjectives = null;
			ratioNonCriticalObjectives = null;
		}

		// Risks ---------------------------------------------------------

		int numRisks = this.repository.findNumRisks();

		if (numRisks >= 2) {
			averageRisksValues = this.repository.findAverageRisksValues();
			standardDeviationRisksValues = this.repository.findStandardDeviationRisksValues();
			minRisksValues = this.repository.findMinRisksValues();
			maxRisksValues = this.repository.findMaxRisksValues();
		} else if (numRisks == 1) {
			averageRisksValues = null;
			standardDeviationRisksValues = null;
			minRisksValues = this.repository.findMinRisksValues();
			maxRisksValues = this.repository.findMaxRisksValues();
		} else {
			averageRisksValues = null;
			standardDeviationRisksValues = null;
			minRisksValues = null;
			maxRisksValues = null;

		}

		// Claims ---------------------------------------------------------

		Date tenWeeksAgo = MomentHelper.deltaFromCurrentMoment(-10, ChronoUnit.WEEKS);
		int numClaims10Weeks = this.repository.findNumClaimsLast10Weeks(tenWeeksAgo);

		if (numClaims10Weeks >= 2) {
			averagePostedClaimsLast10Weeks = this.repository.findAveragePostedClaimsLast10Weeks(tenWeeksAgo);
			minPostedClaimsLast10Weeks = this.repository.findMinPostedClaimsLast10Weeks(tenWeeksAgo);
			maxPostedClaimsLast10Weeks = this.repository.findMaxPostedClaimsLast10Weeks(tenWeeksAgo);
			standardDeviationPostedClaimsLast10Weeks = this.repository.findStandardDeviationPostedClaimsLast10Weeks(tenWeeksAgo);
		} else if (numClaims10Weeks == 1) {
			averagePostedClaimsLast10Weeks = null;
			minPostedClaimsLast10Weeks = this.repository.findMinPostedClaimsLast10Weeks(tenWeeksAgo);
			maxPostedClaimsLast10Weeks = this.repository.findMaxPostedClaimsLast10Weeks(tenWeeksAgo);
			standardDeviationPostedClaimsLast10Weeks = null;
		} else {
			averagePostedClaimsLast10Weeks = null;
			minPostedClaimsLast10Weeks = null;
			maxPostedClaimsLast10Weeks = null;
			standardDeviationPostedClaimsLast10Weeks = null;
		}

		dashboard = new AdministratorDashboard();
		dashboard.setNumManagerPrincipals(numManagerPrincipals);
		dashboard.setNumClientPrincipals(numClientPrincipals);
		dashboard.setNumDeveloperPrincipals(numDeveloperPrincipals);
		dashboard.setNumSponsorPrincipals(numSponsorPrincipals);
		dashboard.setNumAuditorPrincipals(numAuditorPrincipals);
		dashboard.setRatioNoticesWithEmailAndLink(ratioNoticesWithEmailAndLink);
		dashboard.setRatioCriticalObjectives(ratioCriticalObjectives);
		dashboard.setRatioNonCriticalObjectives(ratioNonCriticalObjectives);
		dashboard.setAverageRisksValues(averageRisksValues);
		dashboard.setMinRisksValues(minRisksValues);
		dashboard.setMaxRisksValues(maxRisksValues);
		dashboard.setStandardDeviationRisksValues(standardDeviationRisksValues);
		dashboard.setAveragePostedClaimsLast10Weeks(averagePostedClaimsLast10Weeks);
		dashboard.setMinPostedClaimsLast10Weeks(minPostedClaimsLast10Weeks);
		dashboard.setMaxPostedClaimsLast10Weeks(maxPostedClaimsLast10Weeks);
		dashboard.setStandardDeviationPostedClaimsLast10Weeks(standardDeviationPostedClaimsLast10Weeks);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "numManagerPrincipals", "numClientPrincipals", "numDeveloperPrincipals", "numSponsorPrincipals", "numAuditorPrincipals" //
			, "ratioNoticesWithEmailAndLink", "ratioCriticalObjectives", "ratioNonCriticalObjectives", "averageRisksValues", "minRisksValues", "maxRisksValues", "standardDeviationRisksValues"//
			, "averagePostedClaimsLast10Weeks", "minPostedClaimsLast10Weeks", "maxPostedClaimsLast10Weeks", "standardDeviationPostedClaimsLast10Weeks");

		super.getResponse().addData(dataset);
	}

}
