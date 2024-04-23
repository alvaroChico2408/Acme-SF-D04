
package acme.features.client.contracts;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.userAccount.id = :id")
	Collection<Contract> findContractsByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select p from Project p where p.id in (select id from Contract c where c.project.id = :id)")
	Project findProjectByContract(int id);

	@Query("select p from Project p where p.published = true")
	Collection<Project> findPublishedProjects();

	@Query("select p from Project p where p.code = :code")
	Project findprojectByCode(String code);

	@Query("select c from Contract c where c.code = :code")
	Contract findContractByCode(String code);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findProgressLogsByContract(int id);

	@Query("select c from Client c where c.id = :id")
	Client findOneClientById(int id);

	@Query("select c from Contract c where c.project.id = :id")
	Collection<Contract> findContractsFromProject(int id);

}
