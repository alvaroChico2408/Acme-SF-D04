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
	<acme:input-checkbox code="sponsor.sponsorship.form.label.published" path="published"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.projectCode" path="projectCode"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.sponsorUsername" path="sponsorUsername"/>
	
	<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>
</acme:form>