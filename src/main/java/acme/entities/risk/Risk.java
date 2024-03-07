
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
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
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date				identificationDate;

	@Positive
	private double				impact;

	@PositiveOrZero
	private double				probability; // Preguntar si esta bien y como se acota

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
