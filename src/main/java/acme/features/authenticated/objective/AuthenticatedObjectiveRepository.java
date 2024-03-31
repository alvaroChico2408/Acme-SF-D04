
package acme.features.authenticated.objective;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.objectives.Objective;

@Repository
public interface AuthenticatedObjectiveRepository extends AbstractRepository {

	@Query("select o from Objective o")
	Collection<Objective> findAllObjectives();

	@Query("select o from Objective o where o.id = :objectiveId")
	Objective findObjectiveById(int objectiveId);

}
