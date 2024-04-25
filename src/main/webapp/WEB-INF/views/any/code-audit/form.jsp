<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code" readonly = "true"/>
	<acme:input-moment code="auditor.codeAudit.form.label.executionDate" path="executionDate" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.type" path="type" readonly = "true"/>
	<acme:input-textarea code="auditor.codeAudit.form.label.correctiveActions" path="correctiveActions" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.mark" path="mark" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.projectTitle" path="projectTitle" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.projectCode" path="projectCode" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.link" path="link" readonly = "true"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.auditor" path="auditor" readonly = "true"/>
	<acme:button code="auditor.codeAudit.form.label.auditRecord" action="/any/audit-record/list?masterId=${id}"/>
</acme:form>