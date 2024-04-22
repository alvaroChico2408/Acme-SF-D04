
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainingModule.TrainingModule;
import acme.entities.trainingSession.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.developer.id = :developerId")
	Collection<TrainingModule> findTrainingModulesByDeveloperId(int developerId);

	@Query("select tm from TrainingModule tm where tm.id = :trainingModuleId")
	TrainingModule findTrainingModuleById(int trainingModuleId);

	@Query("select d from Developer d where d.id = :developerId")
	Developer findDeveloperById(int developerId);

	@Query("select tm from TrainingModule tm where tm.code = :code")
	TrainingModule findTrainingModuleByCode(String code);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :trainingModuleId")
	Collection<TrainingSession> findTrainingSessionsByTrainingModule(int trainingModuleId);
}
