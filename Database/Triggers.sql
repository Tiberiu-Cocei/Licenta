CREATE TRIGGER transport_line_update_on_station_and_bicycle
    AFTER INSERT ON transport_line
    FOR EACH ROW
    EXECUTE PROCEDURE transport_line_update_on_station_and_bicycle();

CREATE TRIGGER inspection_update_on_report
    AFTER INSERT ON inspection
    FOR EACH ROW
    EXECUTE PROCEDURE inspection_update_on_report();

CREATE TRIGGER report_update_on_user_and_bicycle
    AFTER UPDATE ON report
    FOR EACH ROW
    EXECUTE PROCEDURE report_update_on_user_and_bicycle();

CREATE TRIGGER user_on_update_check_for_ban
    AFTER UPDATE OF warning_count ON app_user
    FOR EACH ROW
    EXECUTE PROCEDURE user_on_update_check_for_ban();

CREATE TRIGGER discount_update_on_activity
    AFTER INSERT ON discount
    FOR EACH ROW
    EXECUTE PROCEDURE discount_update_on_activity();

CREATE TRIGGER station_on_update_change_activity
    AFTER UPDATE ON station
    FOR EACH ROW
    EXECUTE PROCEDURE station_on_update_change_activity();

CREATE TRIGGER transaction_on_insert_modifies_tables
    BEFORE INSERT ON app_transaction
    FOR EACH ROW
    EXECUTE PROCEDURE transaction_on_insert_modifies_tables();

CREATE TRIGGER transaction_on_update_modifies_activity_and_penalty
    BEFORE UPDATE ON app_transaction
    FOR EACH ROW
    EXECUTE PROCEDURE transaction_on_update_modifies_activity_and_penalty();

CREATE TRIGGER user_on_update_create_message
    AFTER UPDATE OF warning_count ON app_user
    FOR EACH ROW
    EXECUTE PROCEDURE user_on_update_create_message();