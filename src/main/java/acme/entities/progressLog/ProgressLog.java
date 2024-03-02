
package acme.entities.progressLog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

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

	@Pattern(regexp = "PG-[A-Z]{1,2}-[0-9]{4}")
	private String				recordId;

	@NotNull
	@Min(1)
	private Integer				completeness;

	@NotBlank
	@Length(max = 100)
	private String				comment;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@NotNull
	@NotBlank
	@Length(max = 75)
	private String				responsiblePerson;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	/*
	 * QUE COÑO ES ESTO KIQUE
	 * 
	 * @NotNull
	 * 
	 * @Valid
	 * 
	 * @ManyToOne(optional = false)
	 * private Contract contract;
	 */
}
