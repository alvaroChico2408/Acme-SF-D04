<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.codeAudit-static"/>
		</th>
		<td>
			<acme:print value="${numStaticCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.codeAudit-dynamic"/>
		</th>
		<td>
			<acme:print value="${numDynamicCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.averageAuditRecords"/>
		</th>
		<td>
			<acme:print value="${averageAuditRecords}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.deviationAuditRecords"/>
		</th>
		<td>
			<acme:print value="${deviationAuditRecords}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.minNumAuditRecords"/>
		</th>
		<td>
			<acme:print value="${minNumAuditRecords}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.maxNumAuditRecords"/>
		</th>
		<td>
			<acme:print value="${maxNumAuditRecords}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.averageAuditRecordsPeriodLength"/>
		</th>
		<td>
			<acme:print value="${averageAuditRecordsPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.deviationAuditRecordsPeriodLength"/>
		</th>
		<td>
			<acme:print value="${deviationAuditRecordsPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.minAuditRecordsPeriodLength"/>
		</th>
		<td>
			<acme:print value="${minAuditRecordsPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditorDashboard.form.label.maxAuditRecordsPeriodLength"/>
		</th>
		<td>
			<acme:print value="${maxAuditRecordsPeriodLength}"/>
		</td>
	</tr>
</table>