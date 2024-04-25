<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.risk.form.label.reference" path="reference"/>
	<acme:input-select code="authenticated.risk.form.label.type" path="type" choices="${types}"/>
	<acme:input-moment code="authenticated.risk.form.label.identificationDate" path="identificationDate"/>
	<acme:input-textbox code="authenticated.risk.form.label.impact" path="impact"/>
	<acme:input-textbox code="authenticated.risk.form.label.probability" path="probability"/>
	<acme:input-textarea code="authenticated.risk.form.label.description" path="description"/>
	<acme:input-url code="authenticated.risk.form.label.link" path="link"/>
	<acme:input-textbox code="authenticated.risk.form.label.value" path="$value" readonly = "true"/>
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish')}">
			<acme:submit code="administrator.risk.form.button.delete" action="/administrator/risk/delete"/>
			<acme:submit code="administrator.risk.form.button.update" action="/administrator/risk/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.risk.form.button.create" action="/administrator/risk/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>