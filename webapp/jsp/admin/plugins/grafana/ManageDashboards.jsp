<jsp:useBean id="managegrafanaDashboard" scope="session" class="fr.paris.lutece.plugins.grafana.web.DashboardJspBean" />
<% String strContent = managegrafanaDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
