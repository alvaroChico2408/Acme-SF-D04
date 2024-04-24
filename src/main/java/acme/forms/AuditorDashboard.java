
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							numStaticCodeAudits;
	int							numDynamicCodeAudits;
	Double						averageAuditRecords;
	Double						deviationAuditRecords;
	Integer						minNumAuditRecords;
	Integer						maxNumAuditRecords;
	Double						averageAuditRecordsPeriodLength;
	Double						deviationAuditRecordsPeriodLength;
	Integer						minAuditRecordsPeriodLength;
	Integer						maxAuditRecordsPeriodLength;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
