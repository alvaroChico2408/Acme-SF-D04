
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Sponsor sponsor;
		int sponsorRequestId;
		int invoiceId;
		Invoice invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findInvoiceById(invoiceId).orElse(null);
		sponsor = invoice == null ? null : invoice.getSponsorship().getSponsor();
		sponsorRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (sponsor != null)
			status = super.getRequest().getPrincipal().hasRole(sponsor) && sponsor.getId() == sponsorRequestId;
		else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int invoiceId;

		invoiceId = super.getRequest().getData("id", int.class);
		object = this.repository.findInvoiceById(invoiceId).orElse(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "published");
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorshipCode", object.getSponsorship().getCode());

		super.getResponse().addData(dataset);
	}

}
