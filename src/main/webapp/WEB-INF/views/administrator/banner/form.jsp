<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="administrator.banner.form.label.instantiationMoment" path="instantiationMoment"/>
	<acme:input-moment code="administrator.banner.form.label.displayStartDate" path="displayStartDate"/>
	<acme:input-moment code="administrator.banner.form.label.displayEndDate" path="displayEndDate"/>
	<acme:input-url code="administrator.banner.form.label.picture" path="picture"/>
	<acme:input-textbox code="administrator.banner.form.label.slogan" path="slogan"/>
	<acme:input-url code="administrator.banner.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete|update')}">
			<acme:submit code="administrator.banner.form.button.delete" action="/administrator/banner/delete"/>
			<acme:submit code="administrator.banner.form.button.update" action="/administrator/banner/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.banner.form.button.create" action="/administrator/banner/create"/>
		</jstl:when>
	</jstl:choose>	
</acme:form>