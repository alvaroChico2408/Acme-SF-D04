
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claim.Claim;
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
		Map<Integer, Long> claimCountByWeek = this.calculateClaimCountByWeek();
		double averageClaimsPosted = claimCountByWeek.values().stream().map(Long::doubleValue).mapToDouble(Double::valueOf).average().orElse(0);
		double variance = claimCountByWeek.values().stream().mapToDouble(count -> Math.pow(count - averageClaimsPosted, 2)).sum() / claimCountByWeek.size();

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
		dashboard.setAveragePostedClaimsLast10Weeks(averageClaimsPosted);
		dashboard.setMinPostedClaimsLast10Weeks(Collections.min(claimCountByWeek.values()).doubleValue());
		dashboard.setMaxPostedClaimsLast10Weeks(Collections.max(claimCountByWeek.values()).doubleValue());
		dashboard.setStandardDeviationPostedClaimsLast10Weeks(Math.sqrt(variance));

		super.getBuffer().addData(dashboard);
	}

	// Claims ---------------------------------------------------------
	private Map<Integer, Long> calculateClaimCountByWeek() {
		Date tenWeeksDelta = MomentHelper.deltaFromCurrentMoment(-10, ChronoUnit.WEEKS);
		List<Claim> recentClaims = this.repository.findClaimsPostedAfter(tenWeeksDelta);

		Map<Integer, Long> claimsCountByWeek = recentClaims.stream().map(c -> (int) MomentHelper.computeDuration(c.getInstantiationMoment(), MomentHelper.getCurrentMoment()).toDays() / 7)
			.collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.counting()));

		for (int i = 0; i < 10; i++)
			claimsCountByWeek.putIfAbsent(i, 0L);

		return claimsCountByWeek;
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
