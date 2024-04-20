<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="client.contract.form.label.code" path="code"/>
	<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment"/>	
	<acme:input-textbox code="client.contract.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="client.contract.form.label.customerName" path="customerName"/>
	<acme:input-textbox code="client.contract.form.label.goals" path="goals"/>
	<acme:input-money code="client.contract.form.label.budget" path="budget" />
	<acme:input-select code="cleint.contract.form.label.project" path="project" choices="${projects}"/>
	<acme:input-checkbox code="client.contract.form.label.published" path="published" />

	<jstl:choose>
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:button code="client.contract.form.button.progressLog" action="/client/progress-log/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && published == false}">
		<acme:button code="client.contract.form.button.progressLog" action="/client/progress-log/list?masterId=${id}"/>
			<acme:submit code="client.contract.form.button.update" action="/client/contract/update"/>
			<acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.contract.list.button.create" action="/client/contract/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>