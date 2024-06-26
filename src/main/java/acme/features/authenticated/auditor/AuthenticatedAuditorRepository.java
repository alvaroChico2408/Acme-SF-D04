
package acme.features.authenticated.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Auditor;

@Repository
public interface AuthenticatedAuditorRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :userAccountId")
	UserAccount findOneUserAccountById(int userAccountId);

	@Query("select a from Auditor a where a.userAccount.id = :userAccountId")
	Auditor findAuditorByuserAccountId(int userAccountId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration findSystemConfiguration();

}
