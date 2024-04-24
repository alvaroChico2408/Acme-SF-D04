
package acme.features.sponsor.invoice;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.id = :sponsorshipId")
	Optional<Sponsorship> findSponsorshipById(int sponsorshipId);

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select i from Invoice i where i.id = :id")
	Optional<Invoice> findInvoiceById(int id);

	@Query("select i from Invoice i where i.code = :code")
	Optional<Invoice> findInvoiceByCode(String code);

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId and i.published = true")
	Collection<Invoice> findPublishedInvoicesBySponsorshipId(int sponsorshipId);

}
