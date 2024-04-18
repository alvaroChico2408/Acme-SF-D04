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
	<acme:input-checkbox code="manager.project.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="manager.project.form.label.managerUsername" path="manager" readonly = "true"/>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:button code="manager.project.form.button.userStories" action="/manager/user-story/list?projectId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish') && published == false}">
			<acme:button code="manager.project.form.button.userStories" action="/manager/user-story/list?projectId=${id}"/>
			<acme:submit code="manager.project.form.button.delete" action="/manager/project/delete"/>
			<acme:submit code="manager.project.form.button.update" action="/manager/project/update"/>
			<acme:submit code="manager.project.form.button.publish" action="/manager/project/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.project.form.button.create" action="/manager/project/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>