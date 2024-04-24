
package acme.features.auditor.auditorDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.AuditType;
import acme.forms.AuditorDashboard;
import acme.roles.Auditor;

@Service
public class AuditorAuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int auditorId;
		AuditorDashboard dashboard;
		int numStaticCodeAudits;
		int numDynamicCodeAudits;
		Double averageAuditRecords;
		Double deviationAuditRecords;
		Integer minNumAuditRecords;
		Integer maxNumAuditRecords;
		Double averageAuditRecordsPeriodLength;
		Double deviationAuditRecordsPeriodLength;
		Integer minAuditRecordsPeriodLength;
		Integer maxAuditRecordsPeriodLength;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		numStaticCodeAudits = this.repository.findNumCodeAuditsByTypeAndAuditorId(auditorId, AuditType.Static);
		numDynamicCodeAudits = this.repository.findNumCodeAuditsByTypeAndAuditorId(auditorId, AuditType.Dynamic);
		if (numStaticCodeAudits + numDynamicCodeAudits >= 2) {
			deviationAuditRecords = this.repository.findDeviationOfAuditRecordsPerCodeAuditsByAuditorId(auditorId);
			deviationAuditRecordsPeriodLength = this.repository.findDeviationTimePerAuditRecordByAuditorId(auditorId);
		} else {
			deviationAuditRecords = null;
			deviationAuditRecordsPeriodLength = null;
		}
		if (numStaticCodeAudits + numDynamicCodeAudits >= 1) {
			averageAuditRecords = this.repository.findAverageNumOfAuditRecordsPerCodeAudiByAuditorId(auditorId);
			minNumAuditRecords = this.repository.findMinNumOfAuditRecordsPerCodeAuditsByAuditorId(auditorId);
			maxNumAuditRecords = this.repository.findMaxNumOfAuditRecordsPerCodeAuditsByAuditorId(auditorId);
			averageAuditRecordsPeriodLength = this.repository.findAverageTimePerAuditRecordByAuditorId(auditorId);
			minAuditRecordsPeriodLength = this.repository.findMinTimePerAuditRecordByAuditorId(auditorId);
			maxAuditRecordsPeriodLength = this.repository.findMaxTimePerAuditRecordByAuditorId(auditorId);
		} else {
			averageAuditRecords = null;
			minNumAuditRecords = null;
			maxNumAuditRecords = null;
			averageAuditRecordsPeriodLength = null;
			minAuditRecordsPeriodLength = null;
			maxAuditRecordsPeriodLength = null;
		}

		dashboard = new AuditorDashboard();
		dashboard.setNumStaticCodeAudits(numStaticCodeAudits);
		dashboard.setNumDynamicCodeAudits(numDynamicCodeAudits);
		dashboard.setAverageAuditRecords(averageAuditRecords);
		dashboard.setDeviationAuditRecords(deviationAuditRecords);
		dashboard.setMinNumAuditRecords(minNumAuditRecords);
		dashboard.setMaxNumAuditRecords(maxNumAuditRecords);
		dashboard.setAverageAuditRecordsPeriodLength(averageAuditRecordsPeriodLength);
		dashboard.setDeviationAuditRecordsPeriodLength(deviationAuditRecordsPeriodLength);
		dashboard.setMinAuditRecordsPeriodLength(minAuditRecordsPeriodLength);
		dashboard.setMaxAuditRecordsPeriodLength(maxAuditRecordsPeriodLength);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "numStaticCodeAudits", "numDynamicCodeAudits", "averageAuditRecords", "deviationAuditRecords", "minNumAuditRecords", "maxNumAuditRecords", "averageAuditRecordsPeriodLength", "deviationAuditRecordsPeriodLength",
			"minAuditRecordsPeriodLength", "maxAuditRecordsPeriodLength");

		super.getResponse().addData(dataset);
	}
}
