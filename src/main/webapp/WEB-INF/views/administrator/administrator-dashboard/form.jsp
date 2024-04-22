<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.numManagerPrincipals"/>
		</th>
		<td>
			<acme:print value="${numManagerPrincipals}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.numClientPrincipals"/>
		</th>
		<td>
			<acme:print value="${numClientPrincipals}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.numDeveloperPrincipals"/>
		</th>
		<td>
			<acme:print value="${numDeveloperPrincipals}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.numSponsorPrincipals"/>
		</th>
		<td>
			<acme:print value="${numSponsorPrincipals}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.numAuditorPrincipals"/>
		</th>
		<td>
			<acme:print value="${numAuditorPrincipals}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.ratioNoticesWithEmailAndLink"/>
		</th>
		<td>
			<acme:print value="${ratioNoticesWithEmailAndLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.ratioCriticalObjectives"/>
		</th>
		<td>
			<acme:print value="${ratioCriticalObjectives}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.ratioNonCriticalObjectives"/>
		</th>
		<td>
			<acme:print value="${ratioNonCriticalObjectives}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.averageRisksValues"/>
		</th>
		<td>
			<acme:print value="${averageRisksValues}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.minRisksValues"/>
		</th>
		<td>
			<acme:print value="${minRisksValues}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.maxRisksValues"/>
		</th>
		<td>
			<acme:print value="${maxRisksValues}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.standardDeviationRisksValues"/>
		</th>
		<td>
			<acme:print value="${standardDeviationRisksValues}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.averagePostedClaimsLast10Weeks"/>
		</th>
		<td>
			<acme:print value="${averagePostedClaimsLast10Weeks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.minPostedClaimsLast10Weeks"/>
		</th>
		<td>
			<acme:print value="${minPostedClaimsLast10Weeks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.maxPostedClaimsLast10Weeks"/>
		</th>
		<td>
			<acme:print value="${maxPostedClaimsLast10Weeks}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administratorDashboard.form.label.standardDeviationPostedClaimsLast10Weeks"/>
		</th>
		<td>
			<acme:print value="${standardDeviationPostedClaimsLast10Weeks}"/>
		</td>
	</tr>
</table>

<jstl:choose>
	<jstl:when test="${averageRisksValues != null && standardDeviationRisksValues != null && minRisksValues != null && maxRisksValues != null }">

		<h3><acme:message code="administrator.administratorDashboard.form.label.risks.information"/></h3>
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
								<jstl:out value="${averageRisksValues}"/>, 
								<jstl:out value="${standardDeviationRisksValues}"/>, 
								<jstl:out value="${minRisksValues}"/>,
								<jstl:out value="${maxRisksValues}"/>
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
										suggestedMax : 100.0
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
	<jstl:when test="${averagePostedClaimsLast10Weeks != null && standardDeviationPostedClaimsLast10Weeks != null && minPostedClaimsLast10Weeks != null && maxPostedClaimsLast10Weeks != null}">
		<h3>
			<acme:message code="sponsor.sponsorDashboard.form.label.claims.information" />
		</h3>
		<div>
			<canvas id="canvas1"></canvas>
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
								<jstl:out value="${averagePostedClaimsLast10Weeks}"/>, 
								<jstl:out value="${standardDeviationPostedClaimsLast10Weeks}"/>, 
								<jstl:out value="${minPostedClaimsLast10Weeks}"/>,
								<jstl:out value="${maxPostedClaimsLast10Weeks}"/>
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
									suggestedMax : 100.0
								}
							}
						]
					},
					legend : {
						display : false
					}
				};
	
				var canvas, context;
				canvas = document.getElementById("canvas1");
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


<acme:return/>