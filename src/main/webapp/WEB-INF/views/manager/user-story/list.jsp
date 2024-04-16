<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.userStory.list.label.title" path="title"/>
	<acme:list-column code="manager.userStory.list.label.estimatedCost" path="estimatedCost"/>
	<acme:list-column code="manager.userStory.list.label.priority" path="priority"/>
	<acme:list-column code="manager.userStory.list.label.published" path="published"/>
</acme:list>

<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="manager.user-story.list.button.create" action="/manager/user-story/create"/>
</jstl:if>	