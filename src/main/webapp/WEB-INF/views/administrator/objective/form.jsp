<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.objective.form.label.title" path="title"/>
	<acme:input-textarea code="administrator.objective.form.label.description" path="description"/>
	<acme:input-select code="administrator.objective.form.label.priority" path="priority" choices="${priorities}"/>
	<acme:input-checkbox code="administrator.objective.form.label.status" path="status"/>
	<acme:input-moment code="administrator.objective.form.label.executionPeriodStart" path="executionPeriodStart"/>
	<acme:input-moment code="administrator.objective.form.label.executionPeriodFinish" path="executionPeriodFinish"/>
	<acme:input-url code="administrator.objective.form.label.link" path="link"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:input-checkbox code="administrator.objective.form.label.confirmation" path="confirmation"/>
		<acme:submit code="administrator.objective.form.button.create" action="/administrator/objective/create"/>
	</jstl:if>
</acme:form>