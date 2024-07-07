
package acme.entities.trainingSession;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.trainingModule.TrainingModule;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "training_module_id"), @Index(columnList = "code")
})

public class TrainingSession extends AbstractEntity {

	// Serialisation identifier ----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^TS-[A-Z]{1,3}-[0-9]{3}$", message = "{validation.trainingsession.code}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startPeriod;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endPeriod;

	@NotBlank
	@Length(max = 75)
	private String				location;

	@NotBlank
	@Length(max = 75)
	private String				instructor;

	@NotBlank
	@Email
	@Length(max = 255)
	private String				contactEmail;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				published;

	// Derived attributes ------------------------------------------------------

	// Relationships -----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private TrainingModule		trainingModule;

}
