
package acme.entities.trainingModule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.roles.Developer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {

	@Index(columnList = "developer_id"), @Index(columnList = "project_id"), @Index(columnList = "code"), @Index(columnList = "published"), @Index(columnList = "developer_id, published"), @Index(columnList = "updateMoment, developer_id, published")
})

public class TrainingModule extends AbstractEntity {

	// Serialisation identifier ----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$", message = "{validation.trainingmodule.code}")
	private String				code;

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	@NotBlank
	@Length(max = 100)
	private String				details;

	@NotNull
	private Difficulty			difficultyLevel;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateMoment;

	@URL
	@Length(max = 255)
	private String				link;

	@Min(0)
	private int					totalTime;

	private boolean				published;

	// Derived attributes ----------------------- ------------------------------

	// Relationships -----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project				project;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Developer			developer;
}
