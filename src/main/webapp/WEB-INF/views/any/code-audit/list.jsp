<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list>
	<acme:list-column code="any.codeAudit.list.label.code" path="code"/>
	<acme:list-column code="any.codeAudit.list.label.executionDate" path="executionDate"/>
	<acme:list-column code="any.codeAudit.list.label.type" path="type"/>
	<acme:list-column code="any.codeAudit.list.label.mark" path="mark"/>
</acme:list>