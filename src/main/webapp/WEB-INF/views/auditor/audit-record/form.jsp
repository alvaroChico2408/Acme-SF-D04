<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.auditRecord.form.label.code" path="code"/>
	<acme:input-moment code="auditor.auditRecord.form.label.startDate" path="startDate"/>
	<acme:input-moment code="auditor.auditRecord.form.label.endDate" path="endDate"/>
	<acme:input-select code="auditor.auditRecord.form.label.mark" path="mark" choices = "${marks}"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.link" path="link"/>
	<acme:input-checkbox code="auditor.auditRecord.form.label.published" path="published" readonly = "true"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update')&&published==false}">
			<acme:submit code="auditor.auditRecord.form.label.update" action="/auditor/audit-record/update"/>
			<acme:submit code="auditor.auditRecord.form.label.delete" action="/auditor/audit-record/delete"/>
			<acme:submit code="auditor.auditRecord.form.label.publish" action="/auditor/audit-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditRecord.form.label.create" action="/auditor/audit-record/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>