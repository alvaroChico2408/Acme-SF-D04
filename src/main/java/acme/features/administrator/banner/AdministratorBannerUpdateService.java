
package acme.features.administrator.banner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;
import acme.entities.systemConfiguration.SystemConfiguration;
import spam.SpamFilter;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository	repository;

	private Date							lowestMoment	= Date.from(Instant.parse("1999-12-31T23:00:00Z"));
	private Date							topestMoment	= Date.from(Instant.parse("2200-12-31T23:59:59Z"));

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner object;
		int bannerId;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		bannerId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneBannerByBannerId(bannerId);
		object.setInstantiationMoment(instantiationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "displayStartDate", "displayEndDate", "slogan", "picture", "link");

	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		if (!this.getBuffer().getErrors().hasErrors("displayStartDate")) {
			Date displayStartDate = object.getDisplayStartDate();
			Date instantiationMoment = object.getInstantiationMoment();
			Date minimumDuration = MomentHelper.deltaFromMoment(displayStartDate, 1, ChronoUnit.WEEKS);

			super.state(MomentHelper.isAfter(displayStartDate, instantiationMoment), "displayStartDate", "administrator.banner.form.error.displayStartDate");
			super.state(MomentHelper.isAfterOrEqual(displayStartDate, this.lowestMoment) && MomentHelper.isBeforeOrEqual(minimumDuration, this.topestMoment), "displayStartDate", "administrator.banner.form.error.badDiplayStartDate");
		}
		if (!this.getBuffer().getErrors().hasErrors("displayEndDate")) {
			Date displayStartDate = object.getDisplayStartDate();
			Date displayEndDate = object.getDisplayEndDate();
			Date minimumDuration = MomentHelper.deltaFromMoment(displayStartDate, 1, ChronoUnit.WEEKS);

			super.state(MomentHelper.isAfterOrEqual(displayEndDate, minimumDuration), "displayEndDate", "administrator.banner.form.error.notTimeEnough");
			super.state(MomentHelper.isBeforeOrEqual(displayEndDate, this.topestMoment), "displayEndDate", "administrator.banner.form.error.badDiplayEndDate");
		}

		if (!this.getBuffer().getErrors().hasErrors("slogan")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();

			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());
			super.state(!spam.isSpam(object.getSlogan()), "slogan", "administrator.banner.form.error.spam");
		}
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(instantiationMoment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "displayStartDate", "displayEndDate", "slogan", "picture", "link");

		super.getResponse().addData(dataset);

	}

}
