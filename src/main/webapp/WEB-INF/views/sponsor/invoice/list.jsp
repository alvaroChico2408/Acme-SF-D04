<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
<acme:list-column code="sponsor.invoice.list.label.code" path="code" width="33%"/>
<acme:list-column code="sponsor.invoice.list.label.registrationTime" path="registrationTime" width="33%"/>
<acme:list-column code="sponsor.invoice.list.label.dueDate" path="dueDate" width="33%"/>

</acme:list>