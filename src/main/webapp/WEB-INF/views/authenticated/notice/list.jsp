<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.notice.list.label.instantiationMoment" path="instantiationMoment" width="33%"/>
	<acme:list-column code="authenticated.notice.list.label.title" path="title" width="33%"/>
	<acme:list-column code="authenticated.notice.list.label.author" path="author" width="33%"/>
</acme:list>

<acme:button code="authenticated.notice.list.button.create" action="/authenticated/notice/create"/>

