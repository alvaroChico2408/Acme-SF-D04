<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.executionDate" path="executionDate"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.type" path="type"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.correctiveActions" path="correctiveActions"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.mark" path="mark"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.published" path="published"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.link" path="link"/>

	<acme:button code="auditor.codeAudit.form.label.auditRecord" action="/auditor/audit-record/list?id=${id}"/>
</acme:form>