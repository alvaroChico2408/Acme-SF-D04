
package acme.features.administrator.systemConfiguration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfiguration.SystemConfiguration;

@Repository
public interface AdministratorSystemConfigurationRepository extends AbstractRepository {

	//SystemConfiguration
	@Query("select s from SystemConfiguration s")
	SystemConfiguration findCurrentSystemConfiguration();

}
