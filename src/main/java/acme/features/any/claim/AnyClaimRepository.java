
package acme.features.any.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;

@Repository
public interface AnyClaimRepository extends AbstractRepository {

	@Query("select c from Claim c")
	Collection<Claim> findAllClaims();

}
