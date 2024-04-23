<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list>
	<acme:list-column code="auditor.codeAudit.list.label.code" path="code"/>
	<acme:list-column code="auditor.codeAudit.list.label.executionDate" path="executionDate"/>
	<acme:list-column code="auditor.codeAudit.list.label.type" path="type"/>
</acme:list>
<acme:button code="auditor.codeAudit.list.button.create" action="/auditor/code-audit/create"/>