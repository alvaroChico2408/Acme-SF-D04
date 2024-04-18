
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findSponsorshipByCode(object.getCode()).orElse(null);
			super.state(existing == null || existing.equals(object), "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("durationInitial"))
			super.state(MomentHelper.isAfter(object.getDurationInitial(), object.getMoment()), "durationInitial", "sponsor.sponsorship.form.error.pastDurationInitial");

		if (!super.getBuffer().getErrors().hasErrors("durationFinal")) {
			Date minimumDuration;
			Date start = object.getDurationInitial();

			minimumDuration = MomentHelper.deltaFromMoment(start, 1, ChronoUnit.MONTHS);
			super.state(MomentHelper.isAfter(object.getDurationFinal(), minimumDuration), "durationFinal", "sponsor.sponsorship.form.error.durationFinalTooClose");
		}

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() >= 0, "amount", "sponsor.sponsorship.form.error.positiveAmount");
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
		dataset.put("projectCode", object.getProject().getCode());

		super.getResponse().addData(dataset);
	}

}
