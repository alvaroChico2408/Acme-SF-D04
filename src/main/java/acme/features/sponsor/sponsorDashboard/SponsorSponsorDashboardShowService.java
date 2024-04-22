
package acme.features.sponsor.sponsorDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		int numSponsorship = this.repository.findNumberPublishedSponsorships(sponsorId);
		int numInvoices = this.repository.findNumberPublishedInvoices(sponsorId);
		SponsorDashboard dashboard;
		int invoicesWithTaxLessEqual21;
		int sponsorshipsWithLink;
		Double averageAmountSponsorships;
		Double deviationAmountSponsorships;
		Double minimunAmountSponsorships;
		Double maximumAmountSponsorships;
		Double averageQuantityInvoices;
		Double deviationQuantityInvoices;
		Double minimunQuantityInvoices;
		Double maximumQuantityInvoices;

		invoicesWithTaxLessEqual21 = this.repository.findNumOfInvoicesWithTax21less(sponsorId);
		sponsorshipsWithLink = this.repository.findNumberPublishedSponsorshipsWithLink(sponsorId);

		if (numSponsorship >= 2) {
			averageAmountSponsorships = this.repository.findAverageAmountPublishedSponsorships(sponsorId);
			deviationAmountSponsorships = this.repository.findDeviationAmountPublishedSponsorships(sponsorId);
		} else {
			averageAmountSponsorships = null;
			deviationAmountSponsorships = null;
		}

		if (numSponsorship >= 1) {
			minimunAmountSponsorships = this.repository.findMinAmountPublishedSponsorships(sponsorId);
			maximumAmountSponsorships = this.repository.findMaxAmountPublishedSponsorships(sponsorId);
		} else {
			minimunAmountSponsorships = null;
			maximumAmountSponsorships = null;
		}

		if (numInvoices >= 2) {
			averageQuantityInvoices = this.repository.findAverageQuantityPublishedInvoices(sponsorId);
			deviationQuantityInvoices = this.repository.findDeviationQuantityPublishedInvoices(sponsorId);
		} else {
			averageQuantityInvoices = null;
			deviationQuantityInvoices = null;
		}

		if (numInvoices >= 1) {
			minimunQuantityInvoices = this.repository.findMinQuantityPublishedInvoices(sponsorId);
			maximumQuantityInvoices = this.repository.findMaxQuantityPublishedInvoices(sponsorId);
		} else {
			minimunQuantityInvoices = null;
			maximumQuantityInvoices = null;
		}

		dashboard = new SponsorDashboard();
		dashboard.setInvoicesWithTaxLessEqual21(invoicesWithTaxLessEqual21);
		dashboard.setSponsorshipsWithLink(sponsorshipsWithLink);
		dashboard.setAverageAmountSponsorships(averageAmountSponsorships);
		dashboard.setDeviationAmountSponsorships(deviationAmountSponsorships);
		dashboard.setMinimunAmountSponsorships(minimunAmountSponsorships);
		dashboard.setMaximumAmountSponsorships(maximumAmountSponsorships);
		dashboard.setAverageQuantityInvoices(averageQuantityInvoices);
		dashboard.setDeviationQuantityInvoices(deviationQuantityInvoices);
		dashboard.setMinimunQuantityInvoices(minimunQuantityInvoices);
		dashboard.setMaximumQuantityInvoices(maximumQuantityInvoices);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "invoicesWithTaxLessEqual21", "sponsorshipsWithLink", "averageAmountSponsorships", //
			"deviationAmountSponsorships", "minimunAmountSponsorships", "maximumAmountSponsorships", "averageQuantityInvoices", //
			"deviationQuantityInvoices", "minimunQuantityInvoices", "maximumQuantityInvoices");

		super.getResponse().addData(dataset);
	}

}
