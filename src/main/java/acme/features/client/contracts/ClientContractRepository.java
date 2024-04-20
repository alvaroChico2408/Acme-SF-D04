
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

	@Query("select c from Contract c where c.client.id = :clientId")
	Collection<Contract> findManyContractsByClientId(int clientId);

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select c from Client c where c.id = :id")
	Client findOneClientById(int id);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select pl from ProgressLog pl")
	Collection<ProgressLog> findAllProgressLog();

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogByContractId(int contractId);

	@Query("select c from Contract c where c.code = :code")
	Contract findOneContractByCode(String code);

	@Query("select c.project from Contract c where c.client.id = :clientId")
	Collection<Project> findManyProjectsByClientId(int clientId);

}
