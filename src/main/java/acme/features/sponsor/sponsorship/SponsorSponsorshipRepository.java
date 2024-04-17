
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findSponsorshipBySponsorId(int sponsorId);

	@Query("select s from Sponsorship s where s.id = :sponsorshipId")
	Optional<Sponsorship> findSponsorshipById(int sponsorshipId);

	@Query("select s from Sponsor s where s.id = :id")
	Optional<Sponsor> findSponsorById(int id);

	@Query("select s from Sponsorship s where s.code = :code")
	Optional<Sponsorship> findSponsorshipByCode(String code);

	@Query("select p from Project p where p.code = :code")
	Optional<Project> findProjectByCode(String code);

}
