<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.developer.trainingModule.form.label.code" path="code" readonly="true"/>
	<acme:input-moment code="any.developer.trainingModule.form.label.creationMoment" path="creationMoment" readonly="true"/>
	<acme:input-textarea code="any.developer.trainingModule.form.label.details" path="details" readonly="true"/>
	<acme:input-select code="any.developer.trainingModule.form.label.difficultyLevel" path="difficultyLevel" choices="${difficulties}" readonly="true"/>
	<acme:input-moment code="any.developer.trainingModule.form.label.updateMoment" path="updateMoment" readonly="true"/>
	<acme:input-url code="any.developer.trainingModule.form.label.link" path="link" readonly="true"/>
	<acme:input-integer code="any.developer.trainingModule.form.label.totalTime" path="totalTime" readonly="true"/>
	<acme:input-checkbox code="any.developer.trainingModule.form.label.published" path="published" readonly = "true"/>
	<acme:input-textbox code="any.developer.trainingModule.form.label.developerUsername" path="developer" readonly = "true"/>
	<acme:input-select code="any.developer.trainingModule.form.label.projects" path="project" choices="${projects}" readonly="true"/>
</acme:form>