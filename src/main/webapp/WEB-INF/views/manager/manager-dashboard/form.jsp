<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.userStory-must"/>
		</th>
		<td>
			<acme:print value="${numMustUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.userStory-should"/>
		</th>
		<td>
			<acme:print value="${numShouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.userStory-could"/>
		</th>
		<td>
			<acme:print value="${numCouldUserStories}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.userStory-wont"/>
		</th>
		<td>
			<acme:print value="${numWontUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.average-userStory-estimatedCost"/>
		</th>
		<td>
			<acme:print value="${averageEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.stddev-userStory-estimatedCost"/>
		</th>
		<td>
			<acme:print value="${deviationEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.min-userStory-estimatedCost"/>
		</th>
		<td>
			<acme:print value="${minEstimatedCostUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.max-userStory-estimatedCost"/>
		</th>
		<td>
			<acme:print value="${maxEstimatedCostUserStories}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.average-project-cost"/>
		</th>
		<td>
			<acme:print value="${averageCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.stddev-project-cost"/>
		</th>
		<td>
			<acme:print value="${deviationCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.min-project-cost"/>
		</th>
		<td>
			<acme:print value="${minCostProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.managerDashboard.form.label.max-project-cost"/>
		</th>
		<td>
			<acme:print value="${maxCostProjects}"/>
		</td>
	</tr>
</table>


<jstl:choose>
	<jstl:when test="${numMustUserStories != 0 || numShouldUserStories != 0 || numCouldUserStories != 0 || numWontUserStories != 0}">
		<h3><acme:message code="manager.managerDashboard.form.label.userStories.priority.information"/></h3>
		<div>
			<canvas id="canvas"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
							"MUST", "SHOULD", "COULD", "WONT"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${numMustUserStories}"/>, 
								<jstl:out value="${numShouldUserStories}"/>,
								<jstl:out value="${numCouldUserStories}"/>,
								<jstl:out value="${numWontUserStories}"/>,
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
	<jstl:when test="${averageEstimatedCostUserStories != null && deviationEstimatedCostUserStories != null && minEstimatedCostUserStories != null && maxEstimatedCostUserStories != null}">

		<h3><acme:message code="manager.managerDashboard.form.label.userStories.information"/></h3>
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
								<jstl:out value="${averageEstimatedCostUserStories}"/>, 
								<jstl:out value="${deviationEstimatedCostUserStories}"/>, 
								<jstl:out value="${minEstimatedCostUserStories}"/>,
								<jstl:out value="${maxEstimatedCostUserStories}"/>
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
										suggestedMax : 10000.0
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
	<jstl:when test="${averageCostProjects != null && deviationCostProjects != null && minCostProjects != null && maxCostProjects != null}">
		<h3>
			<acme:message code="manager.managerDashboard.form.label.projects.information" />
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
								<jstl:out value="${averageCostProjects}"/>, 
								<jstl:out value="${deviationCostProjects}"/>, 
								<jstl:out value="${minCostProjects}"/>,
								<jstl:out value="${maxCostProjects}"/>
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
									suggestedMax : 10000.0
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