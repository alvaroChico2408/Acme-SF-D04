<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.sponsorship.list.label.code" path="code" width="16%"/>
	<acme:list-column code="any.sponsorship.list.label.moment" path="moment" width="16%"/>
	<acme:list-column code="any.sponsorship.list.label.durationInitial" path="durationInitial" width="16%"/>
	<acme:list-column code="any.sponsorship.list.label.durationFinal" path="durationFinal" width="16%"/>
	<acme:list-column code="any.sponsorship.list.label.amount" path="amount" width="16%"/>
	<acme:list-column code="any.sponsorship.list.label.type" path="type" width="16%"/>
</acme:list>