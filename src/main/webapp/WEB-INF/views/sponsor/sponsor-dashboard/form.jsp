<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.invoicesWithTaxLessEqual21"/>
		</th>
		<td>
			<acme:print value="${invoicesWithTaxLessEqual21}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.sponsorshipsWithLink"/>
		</th>
		<td>
			<acme:print value="${sponsorshipsWithLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.averageAmountSponsorships"/>
		</th>
		<td>
			<acme:print value="${averageAmountSponsorships}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.deviationAmountSponsorships"/>
		</th>
		<td>
			<acme:print value="${deviationAmountSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.minimunAmountSponsorships"/>
		</th>
		<td>
			<acme:print value="${minimunAmountSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.maximumAmountSponsorships"/>
		</th>
		<td>
			<acme:print value="${maximumAmountSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.averageQuantityInvoices"/>
		</th>
		<td>
			<acme:print value="${averageQuantityInvoices}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.deviationQuantityInvoices"/>
		</th>
		<td>
			<acme:print value="${deviationQuantityInvoices}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.minimunQuantityInvoices"/>
		</th>
		<td>
			<acme:print value="${minimunQuantityInvoices}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsorDashboard.form.label.maximumQuantityInvoices"/>
		</th>
		<td>
			<acme:print value="${maximumQuantityInvoices}"/>
		</td>
	</tr>
</table>

<jstl:choose>
	<jstl:when test="${averageAmountSponsorships != null && deviationAmountSponsorships != null && minimunAmountSponsorships != null && maximumAmountSponsorships != null}">

		<h3><acme:message code="sponsor.sponsorDashboard.form.label.sponsorships.information"/></h3>
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
								<jstl:out value="${averageAmountSponsorships}"/>, 
								<jstl:out value="${deviationAmountSponsorships}"/>, 
								<jstl:out value="${minimunAmountSponsorships}"/>,
								<jstl:out value="${maximumAmountSponsorships}"/>
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
	<jstl:when test="${averageQuantityInvoices != null && deviationQuantityInvoices != null && minimunQuantityInvoices != null && maximumQuantityInvoices != null}">
		<h3>
			<acme:message code="sponsor.sponsorDashboard.form.label.invoices.information" />
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
								<jstl:out value="${averageQuantityInvoices}"/>, 
								<jstl:out value="${deviationQuantityInvoices}"/>, 
								<jstl:out value="${minimunQuantityInvoices}"/>,
								<jstl:out value="${maximumQuantityInvoices}"/>
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