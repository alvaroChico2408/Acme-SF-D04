
package acme.features.client.progresslog;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.roles.Client;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select pl from ProgressLog pl where pl.contract.client.id = :id")
	Collection<ProgressLog> findProgressLogsByClientId(int id);

	@Query("select c from Contract c where c.code = :code")
	Contract findContractByCode(String code);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findProgressLogsByContract(int id);

	@Query("select pl from ProgressLog pl where pl.recordId = :id")
	ProgressLog findProgressLogsByRecordId(String id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogsById(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select c from Contract c where c.client.id = :id")
	Collection<Contract> findContractsByClient(int id);

	@Query("select c from Client c where c.id = :id")
	Optional<Client> findClientById(int id);
}
