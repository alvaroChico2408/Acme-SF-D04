
package acme.features.any.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;

@Repository
public interface AnyContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.published = true")
	Collection<Contract> findPublishedContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findProgressLogsByContract(int id);
}
