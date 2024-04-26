
package acme.features.administrator.banner;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;
import acme.entities.systemConfiguration.SystemConfiguration;

@Repository
public interface AdministratorBannerRepository extends AbstractRepository {

	@Query("select b from Banner b")
	Collection<Banner> findAllBanners();

	@Query("select b from Banner b where b.id = :bannerId")
	Banner findOneBannerByBannerId(int bannerId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration findSystemConfiguration();

}
