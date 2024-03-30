<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="manager.userStory.form.label.title" path="title"/>
	<acme:input-textarea code="manager.userStory.form.label.description" path="description"/>
	<acme:input-integer code="manager.userStory.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textarea code="manager.userStory.form.label.acceptanceCriteria" path="acceptanceCriteria"/>
	<acme:input-textbox code="manager.userStory.form.label.priority" path="priority"/>
	<acme:input-url code="manager.userStory.form.label.link" path="link"/>
	<acme:input-checkbox code="manager.userStory.form.label.published" path="published"/>
	<acme:input-textbox code="manager.userStory.form.label.managerUsername" path="manager"/>
</acme:form>