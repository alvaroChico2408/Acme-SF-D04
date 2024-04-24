<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.average-contract-budget"/>
		</th>
		<td>
			<acme:print value="${averageBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.min-contract-budget"/>
		</th>
		<td>
			<acme:print value="${minimumBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.max-contract-budget"/>
		</th>
		<td>
			<acme:print value="${maximumBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.lin-dev-contract-budget"/>
		</th>
		<td>
			<acme:print value="${deviationBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.progress-Log-with-less25"/>
		</th>
		<td>
			<acme:print value="${numProgressLogsUnder25}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.progress-Log-25to50"/>
		</th>
		<td>
			<acme:print value="${numProgressLogsBetween25and50}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.progress-Log-50to75"/>
		</th>
		<td>
			<acme:print value="${numProgressLogsBetween50and75}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.progress-Log-with-over75"/>
		</th>
		<td>
			<acme:print value="${numProgressLogsAbove75}"/>
		</td>
	</tr>	
</table>

<jstl:choose>
	<jstl:when test="${numProgressLogsUnder25 != 0 || numProgressLogsBetween25and50 != 0 || numProgressLogsBetween50and75 != 0 || numProgressLogsAbove75 != 0}">
		<h3><acme:message code="client.clientDashboard.form.label.progresslog.ranges.information"/></h3>
		<div>
			<canvas id="canvas"></canvas>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var data = {
					labels : [
							"<25", "25-50", "50-75", ">75"
					],
					datasets : [
						{
							data : [
								<jstl:out value="${numProgressLogsUnder25}"/>, 
								<jstl:out value="${numProgressLogsBetween25and50}"/>,
								<jstl:out value="${numProgressLogsBetween50and75}"/>,
								<jstl:out value="${numProgressLogsAbove75}"/>,
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



<acme:return/>