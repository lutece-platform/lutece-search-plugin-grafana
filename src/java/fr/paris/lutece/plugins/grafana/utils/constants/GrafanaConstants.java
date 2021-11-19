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
package fr.paris.lutece.plugins.grafana.utils.constants;

/**
 * Constants
 */
public final class GrafanaConstants
{
    // Plugin
    public static final String PLUGIN_NAME = "grafana";

    // URL
    public static final String GRAFANA_URL_PATH = "grafana.baseUrl";

    // Right
    public static final String RIGHT_GRAFANADASHBOARD = "GRAFANA_MANAGEMENT";

    // Templates
    public static final String TEMPLATE_HOME = "/admin/plugins/grafana/grafana.html";
    public static final String TEMPLATE_NO_DASHBOARD = "/admin/plugins/grafana/no_dashboard.html";

    // Views
    public static final String VIEW_DASHBOARD = "dashboard";

    // Properties for page titles
    public static final String PROPERTY_PAGE_TITLE_DASHBOARD = "grafana.adminFeature.GrafanaDashboard.pageTitle";

    // Freemarker
    public static final String MARK_DASHBOARDS_LIST = "dashboards_list";
    public static final String MARK_CURRENT = "current";
    public static final String MARK_ERROR_MESSAGE = "error_message";
    public static final String PARAMETER_TAB = "tab";

    // RBAC
    public static final String DASHBOARD_RESOURCE_TYPE = "grafana_dashboard";
    public static final String DASHBOARD_PERMISSION_VIEW = "VIEW";

    // Parameters
    public static final String PARAMETER_ID_DASHBOARD = "id_dashboard";

    /**
     * Private constructor
     */
    private GrafanaConstants( )
    {
    }
}
