<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="developer.trainingModule.form.label.code" path="code"/>
	<acme:input-moment code="developer.trainingModule.form.label.creationMoment" path="creationMoment" readonly = "true"/>
	<acme:input-textarea code="developer.trainingModule.form.label.details" path="details"/>
	<acme:input-select code="developer.trainingModule.form.label.difficultyLevel" path="difficultyLevel" choices="${difficulties}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish')}">
			<acme:input-moment code="developer.trainingModule.form.label.updateMoment" path="updateMoment"/>
		</jstl:when>
	</jstl:choose>
	<acme:input-url code="developer.trainingModule.form.label.link" path="link"/>
	<acme:input-integer code="developer.trainingModule.form.label.totalTime" path="totalTime"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish')}">		
			<acme:input-checkbox code="developer.trainingModule.form.label.published" path="published" readonly = "true"/>
		</jstl:when>
	</jstl:choose>
	<acme:input-textbox code="developer.trainingModule.form.label.developerUsername" path="developer" readonly = "true"/>
	<acme:input-select code="developer.trainingModule.form.label.projects" path="project" choices="${projects}"/>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:button code="developer.trainingModule.form.button.trainingSessions" action="/developer/training-session/list?trainingModuleId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update|publish') && published == false}">
			<acme:button code="developer.trainingModule.form.button.trainingSessions" action="/developer/training-session/list?trainingModuleId=${id}"/>
			<acme:submit code="developer.trainingModule.form.button.delete" action="/developer/training-module/delete"/>
			<acme:submit code="developer.trainingModule.form.button.update" action="/developer/training-module/update"/>
			<acme:submit code="developer.trainingModule.form.button.publish" action="/developer/training-module/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.trainingModule.form.button.create" action="/developer/training-module/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>