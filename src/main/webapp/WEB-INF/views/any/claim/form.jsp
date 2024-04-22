<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.claim.form.label.code" path="code" readonly = "${readonly}"/>
	<acme:input-textbox code="any.claim.form.label.heading" path="heading" readonly = "${readonly}"/>
	<acme:input-moment code="any.claim.form.label.instantiationMoment" path="instantiationMoment" readonly = "true"/>
	<acme:input-textarea code="any.claim.form.label.description" path="description" readonly = "${readonly}"/>
	<acme:input-textarea code="any.claim.form.label.department" path="department" readonly = "${readonly}"/>
	<acme:input-email code="any.claim.form.label.email" path="email" readonly = "${readonly}"/>
	<acme:input-url code="any.claim.form.label.link" path="link" readonly = "${readonly}"/>
	
	<jstl:if test="${_command == 'create' && !readonly}">
		<acme:input-checkbox code="any.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="any.claim.form.button.create" action="/any/claim/create"/>
	</jstl:if>
</acme:form>