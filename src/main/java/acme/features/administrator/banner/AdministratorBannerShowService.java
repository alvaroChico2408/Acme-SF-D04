
package acme.features.administrator.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerShowService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Banner banner;
		int id;

		id = super.getRequest().getData("id", int.class);
		banner = this.repository.findOneBannerByBannerId(id);

		super.getBuffer().addData(banner);
	}

	@Override
	public void unbind(final Banner object) {

		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "displayStartDate", "displayEndDate", "slogan", "picture", "link");

		super.getResponse().addData(dataset);

	}

}
