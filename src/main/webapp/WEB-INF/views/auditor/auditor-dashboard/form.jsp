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

<jstl:choose>
	<jstl:when test="${numStaticCodeAudits != 0 || numDynamicCodeAudits != 0 }">
		<h3><acme:message code="auditor.auditorDashboard.form.label.codeAudit.type.information"/></h3>
		<div>
			<canvas id="canvas"></canvas>
		</div>
				<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
							"STATIC", "DYNAMIC"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${numStaticCodeAudits}"/>, 
								<jstl:out value="${numDynamicCodeAudits}"/>,
							],
							backgroundColor: [
								'rgb(30, 190, 104)',
						    	'rgb(44, 172, 240)',
						    ]
						}
					]
				};
	
				var canvas, context;
				canvas = document.getElementById("canvas");
				context = canvas.getContext("2d");
				new Chart(context, {
					type : "doughnut",
					data : data,
				});
			});
		</script>
	</jstl:when>
</jstl:choose>

<jstl:choose>
	<jstl:when test="${averageAuditRecords != null && deviationAuditRecords != null && minNumAuditRecords != null && maxNumAuditRecords != null}">

		<h3><acme:message code="auditor.auditorDashboard.form.label.codeAudit.auditRecord.information"/></h3>
		<div>
			<canvas id="canvas0"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
						"AVERAGE", "DEVIATION", "MIN","MAX"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${averageAuditRecords}"/>, 
								<jstl:out value="${deviationAuditRecords}"/>, 
								<jstl:out value="${minNumAuditRecords}"/>,
								<jstl:out value="${maxNumAuditRecords}"/>
							],
							backgroundColor: [
								'rgb(30, 190, 104)',
						    	'rgb(44, 172, 240)',
						    	'rgb(245, 215, 91)',
						      	'rgb(220, 180, 248)'
						    ]
						}
					]
				};	
				
				var options = {
						scales : {
							yAxes : [
								{
									ticks : {
										suggestedMin : 0.0,
										suggestedMax : 40.0
									}
									}
							]
						},
						legend : {
							display : false
						}
					};
				
				var canvas, context;
				canvas = document.getElementById("canvas0");
				context = canvas.getContext("2d");
				new Chart(context, {
					type : "bar",
					data : data,
					options : options
				});
			});
		</script>
	</jstl:when>
</jstl:choose>

<jstl:choose>
	<jstl:when test="${averageAuditRecordsPeriodLength != null && deviationAuditRecordsPeriodLength != null && minAuditRecordsPeriodLength != null && maxAuditRecordsPeriodLength != null}">

		<h3><acme:message code="auditor.auditorDashboard.form.label.codeAudit.auditRecord.information"/></h3>
		<div>
			<canvas id="canvas0"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
						"AVERAGE", "DEVIATION", "MIN","MAX"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${averageAuditRecordsPeriodLength}"/>, 
								<jstl:out value="${deviationAuditRecordsPeriodLength}"/>, 
								<jstl:out value="${minAuditRecordsPeriodLength}"/>,
								<jstl:out value="${maxAuditRecordsPeriodLength}"/>
							],
							backgroundColor: [
								'rgb(30, 190, 104)',
						    	'rgb(44, 172, 240)',
						    	'rgb(245, 215, 91)',
						      	'rgb(220, 180, 248)'
						    ]
						}
					]
				};	
				
				var options = {
						scales : {
							yAxes : [
								{
									ticks : {
										suggestedMin : 0.0,
										suggestedMax : 1000.0
									}
									}
							]
						},
						legend : {
							display : false
						}
					};
				
				var canvas, context;
				canvas = document.getElementById("canvas0");
				context = canvas.getContext("2d");
				new Chart(context, {
					type : "bar",
					data : data,
					options : options
				});
			});
		</script>
	</jstl:when>
</jstl:choose>