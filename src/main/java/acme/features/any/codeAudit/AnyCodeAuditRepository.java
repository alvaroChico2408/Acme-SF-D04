
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;

@Repository
public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca where ca.published = 1")
	Collection<CodeAudit> findAllCodeAuditsPublished();

	@Query("select ar.mark from AuditRecord ar where ar.published = 1 and ar.codeAudit.id = :codeAuditId")
	Collection<Mark> findManyMarksByCodeAuditId(int codeAuditId);

	@Query("select ca from CodeAudit ca where ca.id = :codeAuditId")
	CodeAudit findOneCodeAuditById(int codeAuditId);

}
