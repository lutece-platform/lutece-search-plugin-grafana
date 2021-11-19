
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GRAFANA_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRAFANA_MANAGEMENT','grafana.adminFeature.GrafanaDashboard.name',1,'jsp/admin/plugins/grafana/GrafanaDashboard.jsp','grafana.adminFeature.GrafanaDashboard.description',0,'grafana',NULL,NULL,NULL,4);

DELETE FROM core_admin_right WHERE id_right = 'GRAFANA_RBAC_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRAFANA_RBAC_MANAGEMENT','grafana.adminFeature.ManageGrafana.name',1,'jsp/admin/plugins/grafana/ManageDashboards.jsp','grafana.adminFeature.ManageGrafana.description',0,'grafana',NULL,NULL,NULL,4);

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRAFANA_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('GRAFANA_MANAGEMENT',1);

DELETE FROM core_user_right WHERE id_right = 'GRAFANA_RBAC_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('GRAFANA_RBAC_MANAGEMENT',1);

-- Create role for grafana_manager
INSERT INTO core_admin_role (role_key,role_description) VALUES ('grafana_dashboards_manager','Gestion des tableaux de bords Grafana');
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission)  SELECT MAX(rbac_id) + 1 , 'grafana_dashboards_manager', 'grafana_dashboard','*','*' FROM core_admin_role_resource;  

-- Give this role to admin
INSERT INTO core_user_role (role_key,id_user) VALUES ('grafana_dashboards_manager',1);
