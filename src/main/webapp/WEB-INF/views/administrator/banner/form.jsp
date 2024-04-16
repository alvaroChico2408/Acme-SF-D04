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
</acme:form>