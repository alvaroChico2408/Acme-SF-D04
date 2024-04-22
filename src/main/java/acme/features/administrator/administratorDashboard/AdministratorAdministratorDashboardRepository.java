
package acme.features.administrator.administratorDashboard;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

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

	@Query("SELECT count(c) FROM Claim c WHERE c.instantiationMoment >= :tenWeeksAgo")
	Integer findNumClaimsLast10Weeks(Date tenWeeksAgo);

	@Query("select count(c) from Claim c where c.instantiationMoment >= :tenWeeksAgo")
	Double findAveragePostedClaimsLast10Weeks(Date tenWeeksAgo);

	@Query("select min(c) from Claim c where c.instantiationMoment >= :tenWeeksAgo")
	Double findMinPostedClaimsLast10Weeks(Date tenWeeksAgo);

	@Query("select max(c) from Claim c where c.instantiationMoment >= :tenWeeksAgo")
	Double findMaxPostedClaimsLast10Weeks(Date tenWeeksAgo);

	@Query("select STDDEV(c) from Claim c where c.instantiationMoment >= :tenWeeksAgo")
	Double findStandardDeviationPostedClaimsLast10Weeks(Date tenWeeksAgo);
}
