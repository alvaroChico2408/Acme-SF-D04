<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.project.form.label.code" path="code" readonly = "true"/>
	<acme:input-textbox code="any.project.form.label.title" path="title" readonly = "true"/>
	<acme:input-textarea code="any.project.form.label.abstract" path="$abstract" readonly = "true"/>
	<acme:input-checkbox code="any.project.form.label.fatalErrors" path="fatalErrors" readonly = "true"/>
	<acme:input-integer code="any.project.form.label.cost" path="cost" readonly = "true"/>
	<acme:input-url code="any.project.form.label.link" path="link" readonly = "true"/>
	<acme:input-checkbox code="any.project.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="any.project.form.label.managerUsername" path="manager" readonly = "true"/>
</acme:form>