
package acme.features.any.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.ProgressLog;

@Repository
public interface AnyProgressLogsRepository extends AbstractRepository {

	@Query("select pl from ProgressLog pl where pl.published = true and pl.contract.id = :id")
	Collection<ProgressLog> findPublishedProgressLogsByContract(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogsById(int id);
}
