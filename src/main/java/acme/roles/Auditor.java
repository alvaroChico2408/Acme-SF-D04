
package acme.roles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Auditor extends AbstractRole {

	// Serialisation identifier ----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	private String				firm;

	@Column(unique = true)
	@NotBlank
	@Length(max = 25)
	private String				professionalId;

	@NotBlank
	@Length(max = 100)
	private String				certifications;

	@URL
	@Length(max = 255)
	private String				link;

}
