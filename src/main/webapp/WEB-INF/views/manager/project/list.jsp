<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.project.list.label.code" path="code" width="10%"/>
	<acme:list-column code="manager.project.list.label.title" path="title" width="65%"/>
	<acme:list-column code="manager.project.list.label.cost" path="cost" width="10%"/>
	<acme:list-column code="manager.project.list.label.published" path="published" width="15%"/>
</acme:list>