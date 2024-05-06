
package acme.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.entities.banner.Banner;

@Service
public class RandomBannerService {

	@Autowired
	RandomBannerRepository repository;


	public Banner getRandomBanner() {
		final Date now = MomentHelper.getCurrentMoment();
		Banner res;
		final Collection<Banner> banners = this.repository.findActiveBanners(now);
		final List<Banner> listOfBanners = new ArrayList<Banner>();
		listOfBanners.addAll(banners);
		if (banners.isEmpty())
			res = null;
		else {
			final int index = RandomHelper.nextInt(listOfBanners.size());
			res = listOfBanners.get(index);
		}
		return res;
	}
}
