
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	// Serialisation identifier ----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^R-[0-9]{3}$", message = "{validation.risk.reference}")
	private String				reference;

	@NotNull
	private RiskType			type;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				identificationDate;

	@Digits(fraction = 2, integer = 3)
	@Range(min = 0, max = 100)
	private double				impact;

	@Digits(fraction = 2, integer = 1)
	@Range(min = 0, max = 1)
	private double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	@Length(max = 255)
	private String				link;

	// Derived attributes ----------------------- ------------------------------


	@Transient
	private double value() {
		return this.impact * this.probability;
	}

	// Relationships ----------------------------------------------------------

}
