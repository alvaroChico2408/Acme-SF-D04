
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							invoicesWithTaxLessEqual21;
	int							sponsorshipsWithLink;
	Double						averageAmountSponsorships;
	Double						deviationAmountSponsorships;
	Double						minimunAmountSponsorships;
	Double						maximumAmountSponsorships;
	Double						averageQuantityInvoices;
	Double						deviationQuantityInvoices;
	Double						minimunQuantityInvoices;
	Double						maximumQuantityInvoices;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
