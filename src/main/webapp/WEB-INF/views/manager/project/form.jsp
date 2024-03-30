<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="manager.project.form.label.code" path="code"/>
	<acme:input-textbox code="manager.project.form.label.title" path="title"/>
	<acme:input-textarea code="manager.project.form.label.abstract" path="$abstract"/>
	<acme:input-checkbox code="manager.project.form.label.fatalErrors" path="fatalErrors"/>
	<acme:input-integer code="manager.project.form.label.cost" path="cost"/>
	<acme:input-url code="manager.project.form.label.link" path="link"/>
	<acme:input-checkbox code="manager.project.form.label.published" path="published"/>
	<acme:input-textbox code="manager.project.form.label.managerUsername" path="manager"/>
	
	<acme:button code="manager.project.form.button.userStories" action="/manager/user-story/list?projectId=${id}"/>
</acme:form>