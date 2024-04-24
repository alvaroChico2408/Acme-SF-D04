<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.training-modules-with-update-moment"/>
		</th>
		<td>
			<acme:print value="${numTrainingModulesWithUpdateMoment}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.training-sessions-with-link"/>
		</th>
		<td>
			<acme:print value="${numTrainingSessionsWithLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.average-time-of-training-module"/>
		</th>
		<td>
			<acme:print value="${averageTimeOfTrainingModule}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.deviation-time-of-training-module"/>
		</th>
		<td>
			<acme:print value="${deviationTimeOfTrainingModule}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.min-time-of-training-module"/>
		</th>
		<td>
			<acme:print value="${minTimeOfTrainingModule}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developerDashboard.form.label.min-time-of-training-module"/>
		</th>
		<td>
			<acme:print value="${maxTimeOfTrainingModule}"/>
		</td>
	</tr>		
</table>

<jstl:choose>
	<jstl:when test="${averageTimeOfTrainingModule != null && deviationTimeOfTrainingModule != null && minTimeOfTrainingModule != null && maxTimeOfTrainingModule != null}">

		<h3><acme:message code="developer.developerDashboard.form.label.trainingModules.information"/></h3>
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
								<jstl:out value="${averageTimeOfTrainingModule}"/>, 
								<jstl:out value="${deviationTimeOfTrainingModule}"/>, 
								<jstl:out value="${minTimeOfTrainingModule}"/>,
								<jstl:out value="${maxTimeOfTrainingModule}"/>
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

<acme:return/>