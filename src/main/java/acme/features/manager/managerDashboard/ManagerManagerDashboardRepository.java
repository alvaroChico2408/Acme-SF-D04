
package acme.features.manager.managerDashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Priority;

@Repository
public interface ManagerManagerDashboardRepository extends AbstractRepository {

	@Query("select count(us) from UserStory us where (us.manager.id = :managerId and us.priority = :priority and us.published = true)")
	int findNumUserStoriesByPriority(int managerId, Priority priority);

	@Query("select avg(us.estimatedCost) from UserStory us where (us.manager.id = :managerId and us.published = true)")
	Optional<Double> findAverageEstimatedCostUserStories(int managerId);

	@Query("select stddev(us.estimatedCost) from UserStory us where (us.manager.id = :managerId and us.published = true)")
	Optional<Double> findDeviationEstimatedCostUserStories(int managerId);

	@Query("select max(us.estimatedCost) from UserStory us where (us.manager.id = :managerId and us.published = true)")
	Optional<Integer> findMaxEstimatedCostUserStories(int managerId);

	@Query("select min(us.estimatedCost) from UserStory us where (us.manager.id = :managerId and us.published = true)")
	Optional<Integer> findMinEstimatedCostUserStories(int managerId);

	@Query("select avg(p.cost) from Project p where (p.manager.id = :managerId and p.published = true)")
	Optional<Double> findAverageCostProjects(int managerId);

	@Query("select stddev(p.cost) from Project p where (p.manager.id = :managerId and p.published = true)")
	Optional<Double> findDeviationCostProjects(int managerId);

	@Query("select max(p.cost) from Project p where (p.manager.id = :managerId and p.published = true)")
	Optional<Integer> findMaxCostProjects(int managerId);

	@Query("select min(p.cost) from Project p where (p.manager.id = :managerId and p.published = true)")
	Optional<Integer> findMinCostProjects(int managerId);

	@Query("select count(us) from UserStory us where (us.manager.id = :managerId and us.published = true)")
	int findNumUserStoriesStatics(int managerId);

	@Query("select count(p) from Project p where (p.manager.id = :managerId and p.published = true)")
	int findNumProjectStatics(int managerId);

}
