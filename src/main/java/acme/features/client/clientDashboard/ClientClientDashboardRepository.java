
package acme.features.client.clientDashboard;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness <=25 and pl.published=true")
	Optional<Integer> findNumOfprogressLogsLess25(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness <= 50 and 25 <= pl.completeness and pl.published=true")
	Optional<Integer> findNumOfProgressLogsWithRate25To50(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.completeness <= 75 and 50 <= pl.completeness and pl.published=true")
	Optional<Integer> findNumOfProgressLogsWithRate50To75(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and 75 <= pl.completeness and pl.published=true")
	Optional<Integer> findNumOfProgressLogsWithRateOver75(int clientId);

	@Query("select avg(c.budget) from Contract c where c.client.id = :clientId and c.published=true")
	Optional<Double> findAverageContractBudget(int clientId);

	@Query("select max(c.budget) from Contract c where c.client.id = :clientId and c.published=true")
	Optional<Double> findMaxContractBudget(int clientId);

	@Query("select min(c.budget) from Contract c where c.client.id = :clientId and c.published=true")
	Optional<Double> findMinContractBudget(int clientId);

	@Query("select stddev(c.budget) from Contract c where c.client.id = :clientId and c.published=true")
	Optional<Double> findDeviationContractBudget(int clientId);

	@Query("select count(c) from Contract c where (c.client.id = :clientId and c.published = true)")
	int findNumContract(int clientId);

}
