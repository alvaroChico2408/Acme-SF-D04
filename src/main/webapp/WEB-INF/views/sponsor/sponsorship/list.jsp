<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.sponsorship.list.label.code" path="code" width="33%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.moment" path="moment" width="33%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.amount" path="amount" width="33%"/>
</acme:list>