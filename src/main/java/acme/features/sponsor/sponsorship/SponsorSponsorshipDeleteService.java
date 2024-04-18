
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorRequestId;
		Sponsor sponsor;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId).orElse(null);
		sponsor = sponsorship == null ? null : sponsorship.getSponsor();
		sponsorRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (sponsor != null)
			status = !sponsorship.isPublished() && super.getRequest().getPrincipal().hasRole(sponsor) && sponsor.getId() == sponsorRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("id", int.class);
		object = this.repository.findSponsorshipById(sponsorshipId).orElse(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		super.bind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published");
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		Collection<Invoice> invoices;

		invoices = this.repository.findInvoicesBySponsorshipId(object.getId());
		this.repository.deleteAll(invoices);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published");
		dataset.put("sponsorUsername", object.getSponsor().getUserAccount().getUsername());
		dataset.put("projectCode", object.getProject().getCode());

		super.getResponse().addData(dataset);
	}

}
