<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly = "true"> 
	<acme:input-textbox code="authenticated.risk.form.label.reference" path="reference" readonly = "true"/>
	<acme:input-textbox code="authenticated.risk.form.label.type" path="type" readonly = "true"/>
	<acme:input-textbox code="authenticated.risk.form.label.identificationDate" path="identificationDate" readonly = "true"/>
	<acme:input-textbox code="authenticated.risk.form.label.impact" path="impact" readonly = "true"/>
	<acme:input-textbox code="authenticated.risk.form.label.probability" path="probability" readonly = "true"/>
	<acme:input-textarea code="authenticated.risk.form.label.description" path="description" readonly = "true"/>
	<acme:input-url code="authenticated.risk.form.label.link" path="link" readonly = "true"/>
	<acme:input-textbox code="authenticated.risk.form.label.value" path="value" readonly = "true"/>
</acme:form>