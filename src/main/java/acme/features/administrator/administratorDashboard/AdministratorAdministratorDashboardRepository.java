
package acme.features.administrator.administratorDashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;

@Repository
public interface AdministratorAdministratorDashboardRepository extends AbstractRepository {

	@Query("select count(m) from Manager m")
	Integer findNumManagers();

	@Query("select count(c) from Client c")
	Integer findNumClients();

	@Query("select count(d) from Developer d")
	Integer findNumDevelopers();

	@Query("select count(s) from Sponsor s")
	Integer findNumSponsors();

	@Query("select count(a) from Auditor a")
	Integer findNumAuditors();

	// Notices ---------------------------------------------------------

	@Query("select count(n) from Notice n")
	Double findNumNotices();

	@Query("select count(n) from Notice n where n.email is not null and n.link is not null")
	Double findNumNoticesWithEmailAndLink();

	// Critical objectives ---------------------------------------------------------

	@Query("select count(o) from Objective o")
	Double findNumObjetives();

	@Query("select count(o) from Objective o where o.status = true")
	Double findNumCriticalObjetives();

	// Risk ---------------------------------------------------------

	@Query("select count(r) from Risk r")
	Integer findNumRisks();

	@Query("select avg(r.impact * r.probability) from Risk r")
	Double findAverageRisksValues();

	@Query("select min(r.impact * r.probability) from Risk r")
	Double findMinRisksValues();

	@Query("select max(r.impact * r.probability) from Risk r")
	Double findMaxRisksValues();

	@Query("select STDDEV(r.impact * r.probability) FROM Risk r")
	Double findStandardDeviationRisksValues();

	// Claims ---------------------------------------------------------

	@Query("select c from Claim c where c.instantiationMoment >= :date")
	List<Claim> findClaimsPostedAfter(Date date);

	@Query(value = "SELECT AVG(weekly_count) AS average_claims_per_week FROM (SELECT COUNT(*) AS weekly_count FROM claim WHERE instantiation_moment >= :date GROUP BY YEARWEEK(instantiation_moment)) AS weekly_counts", nativeQuery = true)
	Double calculateAverageClaimsPerWeekPostedAfter(Date date);

	@Query(value = "SELECT STDDEV(weekly_count) AS average_claims_per_week FROM (SELECT COUNT(*) AS weekly_count FROM claim WHERE instantiation_moment >= :date GROUP BY YEARWEEK(instantiation_moment)) AS weekly_counts", nativeQuery = true)
	Double calculateClaimsPerWeekDeviationPostedAfter(Date date);

	@Query(value = "SELECT MIN(weekly_count) AS average_claims_per_week FROM (SELECT COUNT(*) AS weekly_count FROM claim WHERE instantiation_moment >= :date GROUP BY YEARWEEK(instantiation_moment)) AS weekly_counts", nativeQuery = true)
	Long calculateMinimumClaimsPerWeekPostedAfter(Date date);

	@Query(value = "SELECT MAX(weekly_count) AS average_claims_per_week FROM (SELECT COUNT(*) AS weekly_count FROM claim WHERE instantiation_moment >= :date GROUP BY YEARWEEK(instantiation_moment)) AS weekly_counts", nativeQuery = true)
	Long calculateMaximumClaimsPerWeekPostedAfter(Date date);
}
