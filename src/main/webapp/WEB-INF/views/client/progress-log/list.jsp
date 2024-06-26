
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.progress-logs.list.label.recordId" path="recordId"  width="40%"/>
	<acme:list-column code="client.progress-logs.list.label.completeness" path="completeness" width="40%" />
	<acme:list-column code="client.progress-logs.list.label.responsiblePerson" path="responsiblePerson" width="40%" />
</acme:list>


<acme:button code="client.progress-logs.list.create" action="/client/progress-log/create?masterId=${masterId}"/>
