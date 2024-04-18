
package acme.features.manager.associatedWith;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.AssociatedWith;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerAssociatedWithRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("select us from UserStory us where us.id = :userStoryId")
	UserStory findUserStoryById(int userStoryId);

	@Query("select aw from AssociatedWith aw where (aw.project.id = :projectId and aw.userStory.id = :userStoryId)")
	AssociatedWith findAssociationBetweenProjectIdAndUserStoryId(int projectId, int userStoryId);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select aw.userStory from AssociatedWith aw where aw.project.id = :projectId")
	Collection<UserStory> findManyUserStoriesByProjectId(int projectId);

	@Query("select us from UserStory us where us.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

}
