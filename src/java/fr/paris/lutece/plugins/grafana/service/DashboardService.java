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
package fr.paris.lutece.plugins.grafana.service;

import fr.paris.lutece.plugins.grafana.business.Dashboard;
import fr.paris.lutece.plugins.grafana.business.DashboardHome;
import fr.paris.lutece.plugins.grafana.utils.constants.GrafanaConstants;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.BasicAuthorizationAuthenticator;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * DashboardService
 */
public class DashboardService
{
    private static final String NOT_FOUND = "404";
    private static final String PROPERTY_GRAFANA_SERVER_URL = "grafana.url";
    private static final String GRAFANA_DASHBOARD_LIST_URL = "/api/search?query=%";
    private static final String PROPERTY_GRAFANA_SERVER_LOGIN = "grafana.admin.login";
    private static final String PROPERTY_GRAFANA_SERVER_PWD = "grafana.admin.pwd";
    private static final String PROPERTY_GRAFANA_SERVER_USER_LOGIN = "grafana.user.login";
    private static final String PROPERTY_GRAFANA_SERVER_USER_PWD = "grafana.user.pwd";
    private static final String PROPERTY_GRAFANA_SERVER_SHOW_USER_ACCOUNT = "grafana.user.show";
    private static final String GRAFANA_SERVER_URL = AppPropertiesService.getProperty( PROPERTY_GRAFANA_SERVER_URL );
    private static final String GRAFANA_SERVER_LOGIN = AppPropertiesService.getProperty( PROPERTY_GRAFANA_SERVER_LOGIN );
    private static final String GRAFANA_SERVER_PWD = AppPropertiesService.getProperty( PROPERTY_GRAFANA_SERVER_PWD );
    private static final String GRAFANA_SERVER_USER_LOGIN = AppPropertiesService.getProperty( PROPERTY_GRAFANA_SERVER_USER_LOGIN );
    private static final String GRAFANA_SERVER_USER_PWD = AppPropertiesService.getProperty( PROPERTY_GRAFANA_SERVER_USER_PWD );
    private static final boolean GRAFANA_SERVER_SHOW_USER_ACCOUNT = AppPropertiesService.getPropertyBoolean( PROPERTY_GRAFANA_SERVER_SHOW_USER_ACCOUNT, false );
    private static DashboardService _instance = null;
    private static RequestAuthenticator _authenticator = new BasicAuthorizationAuthenticator( GRAFANA_SERVER_LOGIN, GRAFANA_SERVER_PWD );

    /** Private constructor */
    private DashboardService( )
    {
    }

    public static DashboardService getInstance( )
    {
        if ( _instance == null )
        {
            _instance = new DashboardService( );
        }
        return _instance;
    }

    /**
     * Get the list of all dashboards
     * 
     * @return The list of dashboards
     * @throws NoGrafanaDashboardException
     *             if no Grafana index was found
     * @throws NoGrafanaServerException
     *             if no Grafana server was found
     */
    public List<Dashboard> getDashboards( ) throws NoGrafanaDashboardException, NoGrafanaServerException
    {
        List<Dashboard> listDashboards = new ArrayList<Dashboard>( );
        HttpAccess httpAccess = new HttpAccess( );
        Map<String, String> headers = new HashMap<>( );
        try
        {
            String strJSON;

            if ( StringUtil.isAnyEmpty( GRAFANA_SERVER_LOGIN ) )
            {
                strJSON = httpAccess.doGet( GRAFANA_SERVER_URL + GRAFANA_DASHBOARD_LIST_URL );
            }

            strJSON = httpAccess.doGet( GRAFANA_SERVER_URL + GRAFANA_DASHBOARD_LIST_URL, _authenticator, null, headers );
            List<Dashboard> listAvailableDashboards = getListDashboard( strJSON );
            for ( Dashboard availbaleDashboard : listAvailableDashboards )
            {
                Dashboard dashboard = DashboardHome.findByGrafanaId( availbaleDashboard.getIdGrafanaDashboard( ) );
                if ( dashboard != null )
                {
                    listDashboards.add( dashboard );
                }
                else
                {
                    listDashboards.add( availbaleDashboard );
                }
            }
        }
        catch( HttpAccessException ex )
        {
            if ( ex.getMessage( ).indexOf( NOT_FOUND ) > 0 )
            {
                throw new NoGrafanaDashboardException( ex.getMessage( ) );
            }
            else
            {
                throw new NoGrafanaServerException( ex.getMessage( ) );
            }
        }
        return listDashboards;
    }

    /**
     * Get the list of all dashboard
     * 
     * @param strJSON
     *            The list of dashboard as JSON provided by Elastic
     * @return The list
     */
    public List<Dashboard> getListDashboard( String strJSON )
    {
        List<Dashboard> listDashBoard = new ArrayList<Dashboard>( );

        JSONArray arr = new JSONArray( strJSON );
        for ( int i = 0; i < arr.length( ); i++ )
        {
            JSONObject document = arr.getJSONObject( i );
            if ( document.getString( "type" ).equals( "dash-db" ) )
            {
                Dashboard dashboard = new Dashboard( );
                dashboard.setIdGrafanaDashboard( document.getString( "uid" ) );
                dashboard.setTitle( document.getString( "title" ) );
                dashboard.setURI( document.getString( "uri" ) );
                listDashBoard.add( dashboard );
            }
        }
        return listDashBoard;
    }

    /**
     * Get Grafana server URL
     * 
     * @return The URL
     */
    public String getGrafanaServerUrl( )
    {
        return GRAFANA_SERVER_URL;
    }

    /**
     * Get Grafana server user login
     * 
     * @return The URL
     */
    public String getGrafanaServerUserLogin( )
    {
        return GRAFANA_SERVER_USER_LOGIN;
    }

    /**
     * Get Grafana server user password
     * 
     * @return The URL
     */
    public String getGrafanaServerUserPassword( )
    {
        return GRAFANA_SERVER_USER_PWD;
    }

    /**
     * is Grafana server user should showed
     * 
     * @return The URL
     */
    public boolean isGrafanaServerUserShow( )
    {
        return GRAFANA_SERVER_SHOW_USER_ACCOUNT;
    }

    /**
     * Insert all dashboard to database
     */
    public void createAllDashboards( )
    {
        try
        {
            List<Dashboard> listDashboards = getDashboards( );
            for ( Dashboard dashboard : listDashboards )
            {
                DashboardHome.createOrUpdate( dashboard );
            }
        }
        catch( NoGrafanaServerException e )
        {
            AppLogService.error( "Unable to connect to Grafana server", e );
        }
        catch( NoGrafanaDashboardException e )
        {
            AppLogService.error( "Unable to find Grafana index", e );
        }
    }

    /**
     * Insert a dashboard to database
     */
    public void createDashboard( Dashboard dashboard )
    {
        DashboardHome.createOrUpdate( dashboard );
    }

    /**
     * Return a dashboard list filtered by RBAC authorizations.
     * 
     * @param listDashboard
     * @param request
     * @return a new dashboard list, according to RBAC authorizations.
     */
    public List<Dashboard> filterDashboardListRBAC( List<Dashboard> listDashboard, HttpServletRequest request )
    {
        List<Dashboard> listFilteredDashboards = new ArrayList<Dashboard>( );
        AdminUser user = AdminUserService.getAdminUser( request );
        for ( Dashboard dashboard : listDashboard )
        {
            Dashboard storedDashboard = DashboardHome.findByGrafanaId( dashboard.getIdGrafanaDashboard( ) );
            if ( RBACService.isAuthorized( storedDashboard, GrafanaConstants.DASHBOARD_PERMISSION_VIEW, user ) )
            {
                listFilteredDashboards.add( dashboard );
            }
        }
        return listFilteredDashboards;
    }

    /**
     * Delete given dashboard and RBACs related to it
     * 
     * @param nIdDashboard
     */
    public void deleteDashboard( int nIdDashboard )
    {
        // First delete RBAC Resource related to given id dashboard
        RBACHome.removeForResource( GrafanaConstants.DASHBOARD_RESOURCE_TYPE, Integer.toString( nIdDashboard ) );
        // Then delete Dashboard
        DashboardHome.delete( nIdDashboard );
    }
}
