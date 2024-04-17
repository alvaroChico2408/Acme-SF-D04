<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.moment" path="moment"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.durationInitial" path="durationInitial"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.durationFinal" path="durationFinal"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.type" path="type"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.email" path="email"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link"/>
	<acme:input-checkbox code="sponsor.sponsorship.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.projectCode" path="projectCode" readonly= "true"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.sponsorUsername" path="sponsorUsername" readonly = "true"/>
	
	
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show'}">
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.project.form.button.create" action="/sponsor/sponsorship/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>