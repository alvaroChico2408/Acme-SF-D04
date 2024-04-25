<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list>
	<acme:list-column code="administrator.risk.list.label.reference" path="reference" width="33%"/>
	<acme:list-column code="administrator.risk.list.label.type" path="type" width="33%"/>
	<acme:list-column code="administrator.risk.list.label.value" path="$value" width="33%"/>
</acme:list>
<acme:button code="administrator.risk.list.button.create" action="/administrator/risk/create"/>