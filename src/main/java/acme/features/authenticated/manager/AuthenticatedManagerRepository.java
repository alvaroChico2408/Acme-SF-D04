
package acme.features.authenticated.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :userAccountId")
	UserAccount findOneUserAccountById(int userAccountId);

	@Query("select m from Manager m where m.userAccount.id = :userAccountId")
	Manager findOneManagerByUserAccountId(int userAccountId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration findSystemConfiguration();

}
