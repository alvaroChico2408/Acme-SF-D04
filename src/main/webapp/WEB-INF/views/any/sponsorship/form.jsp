<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code" placeholder="X-000 or XX-000 or XXX-000" readonly="true"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.moment" path="moment" readonly="true"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.durationInitial" path="durationInitial" readonly="true"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.durationFinal" path="durationFinal" readonly="true"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount" readonly="true"/>
	<acme:input-select code="sponsor.sponsorship.form.label.type" path="type" choices="${types}" readonly="true"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.email" path="email" readonly="true"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link" readonly="true"/>
	<acme:input-checkbox code="sponsor.sponsorship.form.label.published" path="published" readonly = "true"/>
	<acme:input-select code="sponsor.sponsorship.form.label.project" path="project" choices="${projects}" readonly="true"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.sponsorUsername" path="sponsorUsername" readonly="true" />
</acme:form>