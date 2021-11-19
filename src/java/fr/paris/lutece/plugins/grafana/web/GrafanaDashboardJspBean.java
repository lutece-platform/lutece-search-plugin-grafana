/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grafana.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grafana.business.Dashboard;
import fr.paris.lutece.plugins.grafana.service.DashboardService;
import fr.paris.lutece.plugins.grafana.service.NoGrafanaServerException;
import fr.paris.lutece.plugins.grafana.service.NoGrafanaDashboardException;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * GrafanaDashboard JSP Bean abstract class for JSP Bean
 */
@Controller( controllerJsp = "GrafanaDashboard.jsp", controllerPath = "jsp/admin/plugins/grafana/", right = GrafanaDashboardJspBean.RIGHT_GRAFANADASHBOARD )
public class GrafanaDashboardJspBean extends MVCAdminJspBean
{
    // Right
    public static final String RIGHT_GRAFANADASHBOARD = "GRAFANA_MANAGEMENT";

    // Uid
    private static final long serialVersionUID = -8829869449480096316L;

    // Templates
    private static final String TEMPLATE_DASHBOARD = "/admin/plugins/grafana/grafana_dashboard.html";
    private static final String TEMPLATE_DASHBOARD_LIST = "/admin/plugins/grafana/grafana_dashboard_list.html";
    private static final String TEMPLATE_NO_DASHBOARD = "/admin/plugins/grafana/no_dashboard.html";
    private static final String TEMPLATE_SERVER_ERROR = "/admin/plugins/grafana/grafana_error.html";

    // Views
    private static final String VIEW_DASHBOARD = "dashboard";
    private static final String VIEW_DASHBOARD_LIST = "dashboardList";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_DASHBOARD = "grafana.adminFeature.GrafanaDashboard.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_DASHBOARD_LIST = "grafana.adminFeature.GrafanaDashboard.list.pageTitle";

    // Freemarker
    private static final String MARK_GRAFANA_SERVER_URL = "grafana_server_url";
    private static final String MARK_GRAFANA_SERVER_USER_LOGIN = "grafana_server_user_login";
    private static final String MARK_GRAFANA_SERVER_USER_PWD = "grafana_server_user_pwd";
    private static final String MARK_ELASTICDATA_FORMS_MANAGEMENT = "right_elasticdata_forms_management";
    private static final String MARK_ELASTICDATA_MANAGEMENT = "right_elasticdata_management";
    private static final String MARK_DASHBOARDS_LIST = "dashboards_list";
    private static final String MARK_DASHBOARD = "dashboard";
    private static final String MARK_ERROR_MESSAGE = "error_message";
    private static final String PARAMETER_TAB = "tab";

    // right
    private static final String RIGHT_ELASTICDATA_FORMS_MANAGEMENT = "ELASTICDATA_FORMS_MANAGEMENT";
    private static final String RIGHT_ELASTICDATA_MANAGEMENT = "ELASTICDATA_MANAGEMENT";

    /**
     * Returns the content of the page grafana.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_DASHBOARD )
    public String getGrafanaDashboard( HttpServletRequest request )
    {
        try
        {
            List<Dashboard> listDashboards = DashboardService.getInstance( ).getDashboards( );
            listDashboards = DashboardService.getInstance( ).filterDashboardListRBAC( listDashboards, request );
            String strIdDashboard = request.getParameter( PARAMETER_TAB );
            Optional<Dashboard> dashboard = listDashboards.stream( ).filter( o -> o.getIdGrafanaDashboard( ).equals( strIdDashboard ) ).findFirst( );

            if ( dashboard.isPresent( ) )
            {
                Map<String, Object> model = getModel( );
                model.put( MARK_DASHBOARD, dashboard.get( ) );
                model.put( MARK_GRAFANA_SERVER_URL, DashboardService.getInstance( ).getGrafanaServerUrl( ) );

                if ( DashboardService.getInstance( ).isGrafanaServerUserShow( ) )
                {
                    model.put( MARK_GRAFANA_SERVER_USER_LOGIN, DashboardService.getInstance( ).getGrafanaServerUserLogin( ) );
                    model.put( MARK_GRAFANA_SERVER_USER_PWD, DashboardService.getInstance( ).getGrafanaServerUserPassword( ) );
                }

                AdminUser currentUser = AdminUserService.getAdminUser( request );
                Collection<Right> rightList = AdminUserHome.getRightsListForUser( currentUser.getUserId( ) ).values( );

                if ( rightList.stream( ).anyMatch( i -> i.getId( ).equals( RIGHT_ELASTICDATA_FORMS_MANAGEMENT ) ) )
                {
                    model.put( MARK_ELASTICDATA_FORMS_MANAGEMENT, true );
                }

                if ( rightList.stream( ).anyMatch( i -> i.getId( ).equals( RIGHT_ELASTICDATA_MANAGEMENT ) ) )
                {
                    model.put( MARK_ELASTICDATA_MANAGEMENT, true );
                }

                return getPage( PROPERTY_PAGE_TITLE_DASHBOARD, TEMPLATE_DASHBOARD, model );
            }
            else
            {
                Map<String, Object> model = getModel( );
                model.put( MARK_GRAFANA_SERVER_URL, DashboardService.getInstance( ).getGrafanaServerUrl( ) );
                return getPage( PROPERTY_PAGE_TITLE_DASHBOARD, TEMPLATE_NO_DASHBOARD, model );
            }
        }
        catch( NoGrafanaDashboardException ex )
        {
            Map<String, Object> model = getModel( );
            model.put( MARK_GRAFANA_SERVER_URL, DashboardService.getInstance( ).getGrafanaServerUrl( ) );
            return getPage( PROPERTY_PAGE_TITLE_DASHBOARD, TEMPLATE_NO_DASHBOARD, model );
        }
        catch( NoGrafanaServerException ex )
        {
            Map<String, Object> model = getModel( );
            model.put( MARK_ERROR_MESSAGE, ex.getMessage( ) );
            return getPage( PROPERTY_PAGE_TITLE_DASHBOARD, TEMPLATE_SERVER_ERROR, model );
        }
    }

    /**
     * Returns the content of the page grafana.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_DASHBOARD_LIST, defaultView = true )
    public String getGrafanaDashboardList( HttpServletRequest request )
    {
        try
        {
            List<Dashboard> listDashboards = DashboardService.getInstance( ).getDashboards( );
            listDashboards = DashboardService.getInstance( ).filterDashboardListRBAC( listDashboards, request );
            if ( !listDashboards.isEmpty( ) )
            {
                Map<String, Object> model = getModel( );
                model.put( MARK_DASHBOARDS_LIST, listDashboards );
                return getPage( PROPERTY_PAGE_TITLE_DASHBOARD_LIST, TEMPLATE_DASHBOARD_LIST, model );
            }
            else
            {
                Map<String, Object> model = getModel( );
                model.put( MARK_GRAFANA_SERVER_URL, DashboardService.getInstance( ).getGrafanaServerUrl( ) );
                return getPage( PROPERTY_PAGE_TITLE_DASHBOARD_LIST, TEMPLATE_NO_DASHBOARD, model );
            }
        }
        catch( NoGrafanaDashboardException ex )
        {
            Map<String, Object> model = getModel( );
            model.put( MARK_GRAFANA_SERVER_URL, DashboardService.getInstance( ).getGrafanaServerUrl( ) );
            return getPage( PROPERTY_PAGE_TITLE_DASHBOARD_LIST, TEMPLATE_NO_DASHBOARD, model );
        }
        catch( NoGrafanaServerException ex )
        {
            Map<String, Object> model = getModel( );
            model.put( MARK_ERROR_MESSAGE, ex.getMessage( ) );
            return getPage( PROPERTY_PAGE_TITLE_DASHBOARD, TEMPLATE_SERVER_ERROR, model );
        }
    }
}
