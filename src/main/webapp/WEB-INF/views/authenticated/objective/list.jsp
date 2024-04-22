<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.objective.list.label.title" path="title" width="60%"/>
	<acme:list-column code="authenticated.objective.list.label.priority" path="priority" width="10%"/>
	<acme:list-column code="authenticated.objective.list.label.status" path="status" width="10%"/>
	<acme:list-column code="authenticated.objective.list.label.instantiationMoment" path="instantiationMoment" width="20%"/>
</acme:list>