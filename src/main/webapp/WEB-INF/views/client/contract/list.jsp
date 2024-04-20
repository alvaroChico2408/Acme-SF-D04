<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.contract.list.label.code" path="code" width="10%"/>
	<acme:list-column code="client.contract.list.label.instantiationMoment" path="instantiationMoment" width="10%"/>
	<acme:list-column code="client.contract.list.label.providerName" path="providerName" width="80%"/>
	<acme:list-column code="client.contract.list.label.customerName" path="customerName" width="80%"/>
	<acme:list-column code="client.contract.list.label.goals" path="goals" width="80%"/>
	<acme:list-column code="client.contract.list.label.budget" path="budget" width="80%"/>
</acme:list>

<acme:button code="client.contract.list.button.create" action="/client/contract/create"/>	
	