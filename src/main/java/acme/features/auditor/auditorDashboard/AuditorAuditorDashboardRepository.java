
package acme.features.auditor.auditorDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudit.AuditType;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditorDashboardRepository extends AbstractRepository {

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);

	@Query("select count(ca) from CodeAudit ca where ca.type = :type and ca.auditor.id = :auditorId and ca.published = 1")
	int findNumCodeAuditsByTypeAndAuditorId(int auditorId, AuditType type);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :auditorId and ca.published = 1")
	Double findAverageNumOfAuditRecordsPerCodeAudiByAuditorId(int auditorId);

	@Query("select stddev((select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id)) from CodeAudit ca where ca.auditor.id = :auditorId and ca.published = 1")
	Double findDeviationOfAuditRecordsPerCodeAuditsByAuditorId(int auditorId);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :auditorId and ca.published = 1")
	Integer findMinNumOfAuditRecordsPerCodeAuditsByAuditorId(int auditorId);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :auditorId and ca.published = 1")
	Integer findMaxNumOfAuditRecordsPerCodeAuditsByAuditorId(int auditorId);

	@Query("select avg(time_to_sec(timediff(ar.endDate,ar.startDate))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.published = 1")
	Double findAverageTimePerAuditRecordByAuditorId(int auditorId);

	@Query("select stddev(time_to_sec(timediff(ar.endDate,ar.startDate))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.published = 1")
	Double findDeviationTimePerAuditRecordByAuditorId(int auditorId);

	@Query("select min(time_to_sec(timediff(ar.endDate,ar.startDate))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.published = 1")
	Integer findMinTimePerAuditRecordByAuditorId(int auditorId);

	@Query("select max(time_to_sec(timediff(ar.endDate,ar.startDate))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId and ar.codeAudit.published = 1")
	Integer findMaxTimePerAuditRecordByAuditorId(int auditorId);
}
