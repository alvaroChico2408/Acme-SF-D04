<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<jstl:choose>
		<jstl:when test="${command == show && published==true}">
			<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code"/>
			<acme:input-moment code="auditor.codeAudit.form.label.executionDate" path="executionDate"/>
			<acme:input-select code="auditor.codeAudit.form.label.type" path="type" choices="${types}"/>
			<acme:input-textarea code="auditor.codeAudit.form.label.correctiveActions" path="correctiveActions"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.mark" path="mark"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.projectTitle" path="projectTitle" readonly = "true"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.projectCode" path="projectCode" readonly = "true"/>
			<acme:input-checkbox code="auditor.codeAudit.form.label.published" path="published" readonly = "true"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.link" path="link"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.auditor" path="auditor" readonly = "true"/>
			<acme:button code="auditor.codeAudit.form.label.auditRecord" action="/auditor/audit-record/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish')&&published==false}">
			<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code"/>
			<acme:input-moment code="auditor.codeAudit.form.label.executionDate" path="executionDate"/>
			<acme:input-select code="auditor.codeAudit.form.label.type" path="type" choices="${types}"/>
			<acme:input-textarea code="auditor.codeAudit.form.label.correctiveActions" path="correctiveActions"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.mark" path="mark"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.projectTitle" path="projectTitle" readonly = "true"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.projectCode" path="projectCode" readonly = "true"/>
			<acme:input-checkbox code="auditor.codeAudit.form.label.published" path="published" readonly = "true"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.link" path="link"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.auditor" path="auditor" readonly = "true"/>
			<acme:button code="auditor.codeAudit.form.label.auditRecord" action="/auditor/audit-record/list?masterId=${id}"/>
			<acme:submit code="auditor.codeAudit.form.label.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeAudit.form.button.publish" action="/auditor/code-audit/publish"/>
			<acme:submit code="auditor.codeAudit.form.button.delete" action="/auditor/code-audit/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code"/>
			<acme:input-moment code="auditor.codeAudit.form.label.executionDate" path="executionDate"/>
			<acme:input-select code="auditor.codeAudit.form.label.type" path="type" choices="${types}"/>
			<acme:input-textarea code="auditor.codeAudit.form.label.correctiveActions" path="correctiveActions"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.projectCode" path="projectCode"/>
			<acme:input-textbox code="auditor.codeAudit.form.label.link" path="link"/>
			<acme:submit code="auditor.codeAudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>