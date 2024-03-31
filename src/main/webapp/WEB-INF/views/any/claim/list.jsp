<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.claim.list.label.code" path="code" width="15%"/>
	<acme:list-column code="any.claim.list.label.heading" path="heading" width="65%"/>
	<acme:list-column code="any.claim.list.label.instantiationMoment" path="instantiationMoment" width="20%"/>
</acme:list>