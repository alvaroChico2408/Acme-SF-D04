
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainingModule.TrainingModule;
import acme.entities.trainingSession.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findTrainingSessionsByTrainingModuleId(int trainingModuleId);

	@Query("select ts from TrainingSession ts where ts.id = :trainingSessionId")
	TrainingSession findTrainingSessionById(int trainingSessionId);

	@Query("select tm from TrainingModule tm where tm.id = :trainingModuleId")
	TrainingModule findTrainingModuleById(int trainingModuleId);

	@Query("select ts from TrainingSession ts where ts.code = :code")
	TrainingSession findTrainingSessionByCode(String code);

	@Query("select tm from TrainingModule tm where tm.code = :code")
	TrainingModule findTrainingModuleByCode(String code);
}
