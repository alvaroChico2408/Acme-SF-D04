<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.risk.list.label.reference" path="reference" width="33%"/>
	<acme:list-column code="authenticated.risk.list.label.type" path="type" width="33%"/>
	<acme:list-column code="authenticated.risk.list.label.value" path="value" width="33%"/>
</acme:list>