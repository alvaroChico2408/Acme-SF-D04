
package acme.features.authenticated.client;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;

@Repository
public interface AuthenticatedClientRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :userAccountId")
	UserAccount findOneUserAccountById(int userAccountId);

	@Query("select c from Client c where c.userAccount.id = :userAccountId")
	Client findOneClientByUserAccountId(int userAccountId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration findSystemConfiguration();

	@Query("select cl from Client cl where cl.identification = :identification")
	Client findOneClientByIdentification(String identification);
}
