<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="manager.project.list.label.code" path="code"/>
	<acme:input-textbox code="manager.project.list.label.title" path="title"/>
	<acme:input-textarea code="manager.project.list.label.abstract" path="$abstract"/>
	<acme:input-checkbox code="manager.project.list.label.fatalErrors" path="fatalErrors"/>
	<acme:input-integer code="manager.project.list.label.cost" path="cost"/>
	<acme:input-url code="manager.project.list.label.link" path="link"/>
	<acme:input-checkbox code="manager.project.list.label.published" path="published"/>
	<acme:input-textbox code="manager.project.list.label.managerUsername" path="manager"/>
</acme:form>