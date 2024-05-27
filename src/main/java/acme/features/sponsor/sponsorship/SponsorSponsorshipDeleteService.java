
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			status = !sponsorship.isPublished() && super.getRequest().getPrincipal().hasRole(sponsor) //
				&& sponsor.getId() == sponsorRequestId;
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

		super.bind(object, "code", "durationInitial", "durationFinal", "amount", "type", "email", "link", "project");
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
		if (!invoices.isEmpty())
			this.repository.deleteAll(invoices);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		/**
		 * assert object != null;
		 * 
		 * Dataset dataset;
		 * 
		 * final SelectChoices choices = new SelectChoices();
		 * Collection<Project> projects;
		 * projects = this.repository.findPublishedProjects();
		 * SelectChoices types = SelectChoices.from(Type.class, object.getType());
		 * 
		 * for (final Project c : projects)
		 * if (object.getProject() != null && object.getProject().getId() == c.getId())
		 * choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), true);
		 * else
		 * choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), false);
		 * 
		 * dataset = super.unbind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published", "project");
		 * dataset.put("sponsorUsername", object.getSponsor().getUserAccount().getUsername());
		 * dataset.put("project", choices.getSelected().getKey());
		 * dataset.put("projects", choices);
		 * dataset.put("types", types);
		 * super.getResponse().addData(dataset);
		 **/
	}

}
