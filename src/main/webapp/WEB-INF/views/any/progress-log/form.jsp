<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.progress-logs.form.label.recordId" path="recordId"  readonly = "true"/>	
	<acme:input-textbox code="any.progress-logs.form.label.completeness" path="completeness"  readonly = "true"/>	
	<acme:input-textbox code="any.progress-logs.form.label.comment" path="comment"  readonly = "true"/>	
	<acme:input-textbox code="any.progress-logs.form.label.registrationMoment" path="registrationMoment"  readonly = "true"/>	
	<acme:input-textbox code="any.progress-logs.form.label.responsiblePerson" path="responsiblePerson"  readonly = "true"/>	
	<acme:input-checkbox code="any.prorgress-logs.form.label.published" path="published" readonly = "true"/>
</acme:form>