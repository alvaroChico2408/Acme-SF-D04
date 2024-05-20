<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.sponsorship.list.label.code" path="code" width="16%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.moment" path="moment" width="16%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.durationInitial" path="durationInitial" width="16%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.durationFinal" path="durationFinal" width="16%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.amount" path="amount" width="16%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.type" path="type" width="16%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="sponsor.sponsorship.list.button.create" action="/sponsor/sponsorship/create"/>
</jstl:if>	