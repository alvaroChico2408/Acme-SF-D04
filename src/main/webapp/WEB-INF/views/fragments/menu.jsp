<%--
- menu.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.published-projects" action="/any/project/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.claims-list" action="/any/claim/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.published-sponsorship" action="/any/sponsorship/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.published-codeAudit" action="/any/code-audit/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-jaime" action="https://sevillafc.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-enrique" action="https://www.tboi.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-dani" action="https://www.pokerstars.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-alvaro" action="https://www.exteriores.gob.es/Embajadas/paris/es/Paginas/index.aspx"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-ibai" action="https://www.monsterhunter.com"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated.published-projects" action="/any/project/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.claims-list" action="/any/claim/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.published-sponsorship" action="/any/sponsorship/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.published-codeAudit" action="/any/code-audit/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.objectives-list" action="/authenticated/objective/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.training-modules-list" action="/any/training-module/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.risk-list" action="/authenticated/risk/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.notices-list" action="/authenticated/notice/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.objective" action="/administrator/objective/create"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.banner" action="/administrator/banner/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.system-configuration" action="/administrator/system-configuration/show" access="isAuthenticated()"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.dashboard" action="/administrator/administrator-dashboard/show"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.risk" action="/administrator/risk/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.client" access="hasRole('Client')">
			<acme:menu-suboption code="master.menu.client.contract" action="/client/contract/list"/>
			<acme:menu-suboption code="master.menu.client.progress-log" action="/client/progress-log/list-all"/>
			<acme:menu-suboption code="master.menu.client.client-dashboard" action="/client/client-dashboard/show"/>		
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRole('Manager')">			
			<acme:menu-suboption code="master.menu.manager.my-projects" action="/manager/project/list-mine"/>
			<acme:menu-suboption code="master.menu.manager.my-userStories" action="/manager/user-story/list-mine"/>	
			<acme:menu-suboption code="master.menu.manager.dashboard" action="/manager/manager-dashboard/show"/>	
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">			
			<acme:menu-suboption code="master.menu.auditor.my-codeAudits" action="/auditor/code-audit/list-mine"/>	
			<acme:menu-suboption code="master.menu.auditor.dashboard" action="/auditor/auditor-dashboard/show"/>	
		</acme:menu-option>
  
		<acme:menu-option code="master.menu.sponsor" access="hasRole('Sponsor')">			
			<acme:menu-suboption code="master.menu.sponsor.my-sponsorships" action="/sponsor/sponsorship/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.sponsor.dashboard" action="/sponsor/sponsor-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.developer" access="hasRole('Developer')">			
			<acme:menu-suboption code="master.menu.developer.my-training-modules" action="/developer/training-module/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.developer.dashboard" action="/developer/developer-dashboard/show"/>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/anonymous/system/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager" action="/authenticated/manager/update" access="hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-sponsor" action="/authenticated/sponsor/create" access="!hasRole('Sponsor')"/>
			<acme:menu-suboption code="master.menu.user-account.sponsor" action="/authenticated/sponsor/update" access="hasRole('Sponsor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-developer" action="/authenticated/developer/create" access="!hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.developer" action="/authenticated/developer/update" access="hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-auditor" action="/authenticated/auditor/create" access="!hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.auditor" action="/authenticated/auditor/update" access="hasRole('Auditor')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/authenticated/system/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

