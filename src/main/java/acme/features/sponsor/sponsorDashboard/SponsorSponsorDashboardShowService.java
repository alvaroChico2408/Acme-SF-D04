
package acme.features.sponsor.sponsorDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
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
		SponsorDashboard dashboard;
		Collection<Sponsorship> sponsorships = this.repository.findPublishedSponsorshipsBySponsorId(sponsorId);
		List<Integer> ids = sponsorships.stream().map(Sponsorship::getId).collect(Collectors.toList());
		List<Invoice> invoices = new ArrayList<>();
		for (Integer id : ids) {
			Collection<Invoice> invoiceEachSponsorship = this.repository.findInvoicesBySponsorshipId(id);
			invoices.addAll(invoiceEachSponsorship);
		}
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

		invoicesWithTaxLessEqual21 = (int) invoices.stream().filter(invoice -> invoice.getTax() <= 0.21).count();
		sponsorshipsWithLink = this.repository.findNumberPublishedSponsorshipsWithLink(sponsorId);

		if (numSponsorship >= 2) {
			averageAmountSponsorships = this.repository.findAverageAmountPublishedSponsorships(sponsorId);
			deviationAmountSponsorships = this.repository.findDeviationAmountPublishedSponsorships(sponsorId);
		} else {
			averageAmountSponsorships = 25.;
			deviationAmountSponsorships = 25.;
		}

		if (numSponsorship >= 1) {
			minimunAmountSponsorships = this.repository.findMinAmountPublishedSponsorships(sponsorId);
			maximumAmountSponsorships = this.repository.findMaxAmountPublishedSponsorships(sponsorId);
		} else {
			minimunAmountSponsorships = 25.;
			maximumAmountSponsorships = 25.;
		}

		if (invoices.size() >= 2) {
			averageQuantityInvoices = invoices.stream().mapToDouble(invoice -> invoice.getQuantity().getAmount()).average().orElse(0.0);
			deviationQuantityInvoices = 25.;
		} else {
			averageQuantityInvoices = 25.;
			deviationQuantityInvoices = 25.;
		}

		if (invoices.size() >= 1) {
			minimunQuantityInvoices = invoices.stream().map(invoice -> invoice.getQuantity().getAmount()).min(Double::compareTo).orElse(0.0);
			maximumQuantityInvoices = invoices.stream().map(invoice -> invoice.getQuantity().getAmount()).max(Double::compareTo).orElse(0.0);
		} else {
			minimunQuantityInvoices = 25.;
			maximumQuantityInvoices = 25.;
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
