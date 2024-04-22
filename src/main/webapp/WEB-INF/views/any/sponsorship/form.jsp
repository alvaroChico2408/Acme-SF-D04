<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.sponsorship.form.label.code" path="code" readonly = "true"/>
	<acme:input-moment code="any.sponsorship.form.label.moment" path="moment" readonly = "true"/>
	<acme:input-moment code="any.sponsorship.form.label.durationInitial" path="durationInitial" readonly = "true"/>
	<acme:input-moment code="any.sponsorship.form.label.durationFinal" path="durationFinal" readonly = "true"/>
	<acme:input-double code="any.sponsorship.form.label.amount" path="amount" readonly = "true"/>
	<acme:input-textbox code="any.sponsorship.form.label.type" path="type" readonly = "true"/>
	<acme:input-email code="any.sponsorship.form.label.email" path="email" readonly = "true"/>
	<acme:input-url code="any.sponsorship.form.label.link" path="link" readonly = "true"/>
	<acme:input-checkbox code="any.sponsorship.form.label.published" path="published" readonly = "true"/>
</acme:form>