<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.contract.form.label.code" path="code" readonly="true"/>	
	<acme:input-textbox code="any.contract.form.label.providerName" path="providerName" readonly="true"/>	
	<acme:input-textbox code="any.contract.form.label.customerName" path="customerName" readonly="true"/>	
	<acme:input-textbox code="any.contract.form.label.goals" path="goals" readonly="true"/>	
	<acme:input-textbox code="any.contract.form.label.budget" path="budget" readonly="true"/>	
	<acme:input-money code="any.contract.form.label.money" path="money" readonly="true"/>
	<acme:input-checkbox code="any.contract.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="client.contract.form.label.project" path="projectTitle" readonly="true"/>
	<jstl:choose>
		<jstl:when test="${hasProgressLogs}">
			<acme:button code="any.contract.progressLogs" action="/any/progress-log/list?masterId=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>