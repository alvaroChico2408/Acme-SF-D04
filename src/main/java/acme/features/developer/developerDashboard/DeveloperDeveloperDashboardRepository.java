
package acme.features.developer.developerDashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DeveloperDeveloperDashboardRepository extends AbstractRepository {

	@Query("select avg(tm.totalTime) from TrainingModule tm where (tm.developer.id = :developerId and tm.published = true)")
	Optional<Double> findAverageTimeOfTrainingModule(int developerId);

	@Query("select stddev(tm.totalTime) from TrainingModule tm where (tm.developer.id = :developerId and tm.published = true)")
	Optional<Double> findDeviationTimeOfTrainingModule(int developerId);

	@Query("select min(tm.totalTime) from TrainingModule tm where (tm.developer.id = :developerId and tm.published = true)")
	Optional<Integer> findMinTimeOfTrainingModule(int developerId);

	@Query("select max(tm.totalTime) from TrainingModule tm where (tm.developer.id = :developerId and tm.published = true)")
	Optional<Integer> findMaxTimeOfTrainingModule(int developerId);

	@Query("select count(tm) from TrainingModule tm where (tm.developer.id = :developerId and tm.published = true)")
	Optional<Integer> findNumTrainingModules(int developerId);

	@Query("select count(ts) from TrainingSession ts where (ts.link != null and ts.trainingModule.developer.id = :developerId and ts.published = true)")
	Optional<Integer> findNumTrainingSessionsWithLink(int developerId);

	@Query("select count(tm) from TrainingModule tm where (tm.updateMoment != null and tm.developer.id = :developerId and tm.published = true)")
	Optional<Integer> findNumTrainingModulesWithUpdateMoment(int developerId);
}
