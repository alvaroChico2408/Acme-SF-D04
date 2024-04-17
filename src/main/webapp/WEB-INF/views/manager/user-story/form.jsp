<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="manager.userStory.form.label.title" path="title"/>
	<acme:input-textarea code="manager.userStory.form.label.description" path="description"/>
	<acme:input-integer code="manager.userStory.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textarea code="manager.userStory.form.label.acceptanceCriteria" path="acceptanceCriteria"/>
	<acme:input-select code="manager.userStory.form.label.priority" path="priority" choices="${priorities}"/>
	<acme:input-url code="manager.userStory.form.label.link" path="link"/>
	<acme:input-checkbox code="manager.userStory.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="manager.userStory.form.label.managerUsername" path="manager" readonly = "true"/>
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update') && published == false}">
			<acme:submit code="manager.userStory.form.button.delete" action="/manager/user-story/delete"/>
			<acme:submit code="manager.userStory.form.button.update" action="/manager/user-story/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.userStory.form.button.create" action="/manager/user-story/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>