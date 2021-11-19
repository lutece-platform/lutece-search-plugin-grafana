<jsp:useBean id="grafanadashboard" scope="session" class="fr.paris.lutece.plugins.grafana.web.GrafanaDashboardJspBean" />
<% String strContent = grafanadashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
