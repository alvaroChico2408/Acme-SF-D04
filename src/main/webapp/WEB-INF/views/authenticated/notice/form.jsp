<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="authenticated.notice.form.label.instantiationMoment" path="instantiationMoment"/>
	<acme:input-textbox code="authenticated.notice.form.label.title" path="title"/>
	<acme:input-textbox code="authenticated.notice.form.label.author" path="author"/>
	<acme:input-textarea code="authenticated.notice.form.label.message" path="message"/>
	<acme:input-email code="authenticated.notice.form.label.email" path="email"/>
	<acme:input-url code="authenticated.notice.form.label.link" path="link"/>
</acme:form>