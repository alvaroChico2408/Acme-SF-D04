
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoice.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.Type;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

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

		super.bind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published", "project");
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
		Collection<Invoice> publishedInvoices = this.repository.findPublishedInvoicesBySponsorshipId(object.getId());
		Collection<Invoice> invoices = this.repository.findInvoicesBySponsorshipId(object.getId());

		// Code ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findSponsorshipByCode(object.getCode()).orElse(null);
			super.state(existing == null || existing.equals(object), "code", "sponsor.sponsorship.form.error.duplicated");
		}

		// Durations ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("durationInitial"))
			super.state(MomentHelper.isAfter(object.getDurationInitial(), object.getMoment()), "durationInitial", "sponsor.sponsorship.form.error.pastDurationInitial");

		if (!super.getBuffer().getErrors().hasErrors("durationFinal")) {
			Date minimumDuration;
			Date start = object.getDurationInitial();

			minimumDuration = MomentHelper.deltaFromMoment(start, 1, ChronoUnit.MONTHS);
			super.state(MomentHelper.isAfter(object.getDurationFinal(), minimumDuration), "durationFinal", "sponsor.sponsorship.form.error.durationFinalTooClose");
		}

		// Amount ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() >= 0, "amount", "sponsor.sponsorship.form.error.positiveAmount");

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() <= 100000000, "amount", "sponsor.sponsorship.form.error.maxAmount");

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			double totalAmount;
			if (!publishedInvoices.isEmpty())
				totalAmount = publishedInvoices.stream().collect(Collectors.summingDouble(x -> x.totalAmount().getAmount()));
			else
				totalAmount = 0.;
			super.state(object.getAmount().getAmount() >= totalAmount, "amount", "sponsor.sponsorship.form.error.minInvoiceAmountPublished");
		}

		/*
		 * if (!super.getBuffer().getErrors().hasErrors("amount")) {
		 * Sponsorship initial;
		 * 
		 * initial = this.repository.findSponsorshipById(object.getId()).orElse(null);
		 * 
		 * super.state(object.getAmount().getCurrency().trim() == object.getAmount().getCurrency().trim(), "amount", "sponsor.sponsorship.form.error.currencyChanged");
		 * }
		 */

		// Solo Publish ---------------------------------------------------------

		if (!super.getBuffer().getErrors().hasErrors("invoice")) {
			double totalAmount;
			if (!publishedInvoices.isEmpty())
				totalAmount = publishedInvoices.stream().collect(Collectors.summingDouble(x -> x.totalAmount().getAmount()));
			else
				totalAmount = 0.;

			super.state(totalAmount == object.getAmount().getAmount(), "*", "sponsor.sponsorship.form.error.invoicesTotalAmount");
		}

		if (!super.getBuffer().getErrors().hasErrors("invoice"))
			super.state(invoices.size() == publishedInvoices.size(), "*", "sponsor.sponsorship.form.error.invoicesNotPublished");

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;

		final SelectChoices choices = new SelectChoices();
		Collection<Project> projects;
		projects = this.repository.findPublishedProjects();
		SelectChoices types = SelectChoices.from(Type.class, object.getType());

		for (final Project c : projects)
			if (object.getProject() != null && object.getProject().getId() == c.getId())
				choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), true);
			else
				choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), false);

		dataset = super.unbind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published", "project");
		dataset.put("sponsorUsername", object.getSponsor().getUserAccount().getUsername());
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("types", types);
		super.getResponse().addData(dataset);
	}

}
