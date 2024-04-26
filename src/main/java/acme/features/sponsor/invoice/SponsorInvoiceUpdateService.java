
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository	repository;

	@Autowired
	private AuxiliarService				auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorRequestId;
		Sponsor sponsor;
		int invoiceId;
		Invoice invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findInvoiceById(invoiceId).orElse(null);
		sponsor = invoice == null ? null : invoice.getSponsorship().getSponsor();
		sponsorRequestId = super.getRequest().getPrincipal().getActiveRoleId();
		if (sponsor != null)
			status = !invoice.isPublished() && super.getRequest().getPrincipal().hasRole(sponsor) && sponsor.getId() == sponsorRequestId;
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
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "dueDate", "quantity", "tax", "link");
	}

	@Override
	public void validate(final Invoice object) {
		// Code ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existing;

			existing = this.repository.findInvoiceByCode(object.getCode()).orElse(null);
			super.state(existing == null || existing.equals(object), "code", "sponsor.invoice.form.error.duplicated");
		}

		// Due date  ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date maxDate = new Date(4102441199000L); // 2099/12/31 23:59:59
			Date minDate = new Date(946681200000L); // 2000/01/01 00:00:00
			super.state(MomentHelper.isAfterOrEqual(object.getDueDate(), minDate) && MomentHelper.isBeforeOrEqual(object.getDueDate(), maxDate), "dueDate", "sponsor.invoice.form.error.minMaxDueDate");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate"))
			super.state(MomentHelper.isAfter(object.getDueDate(), object.getRegistrationTime()), "dueDate", "sponsor.invoice.form.error.pastDueDate");

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date minimumDuration;
			Date start = object.getRegistrationTime();

			minimumDuration = MomentHelper.deltaFromMoment(start, 1, ChronoUnit.MONTHS);
			super.state(MomentHelper.isAfter(object.getDueDate(), minimumDuration), "dueDate", "sponsor.invoice.form.error.dueDateTooClose");
		}

		// Quantity  ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("quantity"))
			super.state(object.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.minQuantity");

		if (!super.getBuffer().getErrors().hasErrors("quantity"))
			super.state(object.getQuantity().getAmount() <= 10000000, "quantity", "sponsor.invoice.form.error.maxQuantity");

		if (!super.getBuffer().getErrors().hasErrors("quatity") && object.getQuantity() != null)
			super.state(object.getQuantity().getCurrency().trim().toLowerCase().equals(object.getSponsorship().getAmount().getCurrency().trim().toLowerCase()), "quantity", "sponsor.invoice.form.error.invalidCurrency");

		// totalAmount  ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("totalAmount") && object.getQuantity() != null)
			super.state(object.totalAmount().getAmount() <= object.getSponsorship().getAmount().getAmount(), "*", "sponsor.invoice.form.error.totalAmountTooHigh");
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "published");
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorshipCode", object.getSponsorship().getCode());
		dataset.put("money", this.auxiliarService.changeCurrency(object.totalAmount()));

		super.getResponse().addData(dataset);
	}

}
