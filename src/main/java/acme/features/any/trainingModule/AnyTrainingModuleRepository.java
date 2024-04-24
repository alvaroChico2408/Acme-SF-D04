
package acme.features.any.trainingModule;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.trainingModule.TrainingModule;

@Repository
public interface AnyTrainingModuleRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.published = true")
	Collection<TrainingModule> findAllPublishedTrainingModules();

	@Query("select tm from TrainingModule tm where tm.id = :trainingModuleId")
	Optional<TrainingModule> findTrainingModuleById(int trainingModuleId);

	@Query("select p from Project p where p.published = true")
	Collection<Project> findPublishedProjects();

}
