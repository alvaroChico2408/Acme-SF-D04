<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
<acme:list-column code="sponsor.invoice.list.label.code" path="code" width="25%"/>
<acme:list-column code="sponsor.invoice.list.label.registrationTime" path="registrationTime" width="25%"/>
<acme:list-column code="sponsor.invoice.list.label.dueDate" path="dueDate" width="25%"/>
<acme:list-column code="sponsor.invoice.list.label.totalAmount" path="totalAmount" width="25%"/>

</acme:list>