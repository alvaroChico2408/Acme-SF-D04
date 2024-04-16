<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list>
	<acme:list-column code="administrator.banner.list.label.instantiationMoment" path="instantiationMoment"/>
	<acme:list-column code="administrator.banner.list.label.displayStartDate" path="displayStartDate"/>
	<acme:list-column code="administrator.banner.list.label.displayEndDate" path="displayEndDate"/>
	<acme:list-column code="administrator.banner.list.label.slogan" path="slogan"/>
</acme:list>