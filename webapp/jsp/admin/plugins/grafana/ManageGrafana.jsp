<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managegrafana" scope="session" class="fr.paris.lutece.plugins.grafana.web.ManageGrafanaJspBean" />

<% managegrafana.init( request, managegrafana.RIGHT_MANAGEGRAFANA ); %>
<%= managegrafana.getManageGrafanaHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
