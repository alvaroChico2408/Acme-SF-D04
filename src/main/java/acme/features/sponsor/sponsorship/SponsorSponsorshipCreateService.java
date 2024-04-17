
package acme.features.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsorship object;
		Sponsor sponsor;
		Project project;

		sponsor = this.repository.findSponsorById(super.getRequest().getPrincipal().getActiveRoleId()).orElse(null);
		project = this.repository.findProjectByCode("JLB-4567").orElse(null);
		object = new Sponsorship();
		object.setPublished(false);
		object.setSponsor(sponsor);
		object.setMoment(MomentHelper.getCurrentMoment());
		object.setProject(project);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findSponsorshipByCode(object.getCode()).orElse(null);
			super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published");
		dataset.put("sponsorUsername", object.getSponsor().getUserAccount().getUsername());

		super.getResponse().addData(dataset);
	}

}
