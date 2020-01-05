CREATE INDEX ON transport (date);

CREATE INDEX ON transport_line (transport_id);

CREATE INDEX ON inspection (report_id);

CREATE INDEX ON report (bicycle_id, reviewed);

CREATE INDEX ON app_admin (username);

CREATE INDEX ON message (user_id);

CREATE INDEX ON app_user (username);

CREATE INDEX ON bicycle (station_id);

CREATE INDEX ON app_transaction (start_time);

CREATE INDEX ON app_transaction (start_station_id, start_time);

CREATE INDEX ON app_transaction (finish_station_id, start_time);

CREATE INDEX ON station (city_id);

CREATE INDEX ON predicted_activity (station_id, day, hour);

CREATE INDEX ON discount (from_station_id, to_station_id, start_time);

CREATE INDEX ON activity (station_id, day, hour_from);