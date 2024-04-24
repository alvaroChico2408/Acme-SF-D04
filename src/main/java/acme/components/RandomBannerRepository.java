
package acme.components;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface RandomBannerRepository extends AbstractRepository {

	@Query("select b from Banner b where b.displayStartDate <= :date  and b.displayEndDate > :date")
	Collection<Banner> findActiveBanners(Date date);

}
