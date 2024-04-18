<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.auditRecord.form.label.code" path="code"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.startDate" path="startDate"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.endDate" path="endDate"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.mark" path="mark"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.link" path="link"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.published" path="published"/>
</acme:form>