
--
-- Structure for table grafana_dashboard
--

DROP TABLE IF EXISTS grafana_dashboard;
CREATE TABLE grafana_dashboard (
id_dashboard int(6) NOT NULL,
idgrafanadashboard varchar(255) default '' NOT NULL UNIQUE,
title long varchar NOT NULL,
uri long varchar,
PRIMARY KEY (id_dashboard)
);
