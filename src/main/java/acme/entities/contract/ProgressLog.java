
package acme.entities.contract;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProgressLog extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@NotBlank

	@Pattern(regexp = "^PG-[A-Z]{1,2}-[0-9]{4}$", message = "{validation.progresslog.code}")
	private String				recordId;

	@NotNull
	@Digits(fraction = 2, integer = 3)
	@Range(min = 0, max = 100)
	private double				completeness;

	@NotBlank
	@Length(max = 100)
	private String				comment;

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@NotNull
	@NotBlank
	@Length(max = 75)
	private String				responsiblePerson;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Contract			contract;

}
