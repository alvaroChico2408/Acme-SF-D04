<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.userStory.list.label.title" path="title" width="65%"/>
	<acme:list-column code="manager.userStory.list.label.estimatedCost" path="estimatedCost" width="10%"/>
	<acme:list-column code="manager.userStory.list.label.published" path="published" width="15%"/>
</acme:list>