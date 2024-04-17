<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:choose>	 
	<jstl:when test="${acme:anyOf(_command, 'create|delete') && published == true}">
		<h1>
			<acme:message code="manager.associatedWith.form.userStory.cant.info"/>
		</h1>
	</jstl:when>
	<jstl:when test="${acme:anyOf(_command, 'create') && published == false}">
		<h1>
			<acme:message code="manager.associatedWith.form.userStory.create.info"/>
		</h1>
	</jstl:when>	
	<jstl:when test="${acme:anyOf(_command, 'delete') && published == false}">
		<h1>
			<acme:message code="manager.associatedWith.form.userStory.delete.info"/>
		</h1>
	</jstl:when>	
</jstl:choose>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.associatedWith.form.project.code"/>
		</th>
		<td>
			<acme:print value="${project.getCode()}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.associatedWith.form.project.title"/>
		</th>
		<td>
			<acme:print value="${project.getTitle()}"/>
		</td>
	</tr>
</table>

<acme:form>
	<acme:input-select code="manager.associatedWith.form.label.userStory" path="userStory" choices="${userStories}"/>	
	
	<jstl:choose>	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.associatedWith.form.button.add" action="/manager/associated-with/create?projectId=${projectId}"/>
		</jstl:when> 	
	</jstl:choose>
	<jstl:choose>	
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="manager.associatedWith.form.button.remove" action="/manager/associated-with/delete?projectId=${projectId}"/>
		</jstl:when> 	
	</jstl:choose>
</acme:form>
