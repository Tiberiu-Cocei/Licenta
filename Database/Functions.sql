CREATE OR REPLACE FUNCTION transport_line_update_on_station_and_bicycle() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
    UPDATE station 
        SET current_capacity = 
            current_capacity - 1
        WHERE id = NEW.station_from;
		
    UPDATE station
		SET current_capacity =
	    	current_capacity + 1
		WHERE id = NEW.station_to;
		 
	UPDATE bicycle 
        SET arrival_time = NEW.arrival_time,
	     	station_id = NEW.station_to,
			lock_number = NEW.lock_number
        WHERE id = NEW.bicycle_id;
		
	IF (SELECT name FROM station WHERE id = NEW.station_to) = 'Warehouse' THEN
		UPDATE bicycle
			SET status = 'Warehouse'
		WHERE id = NEW.bicycle_id;
	ELSE
		UPDATE bicycle
			SET status = 'Transport'
		WHERE id = NEW.bicycle_id;
	END IF;
	
 
 	RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION inspection_update_on_report() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
    UPDATE report
        SET reviewed = TRUE,
	   	 	fake = NEW.fake
        WHERE id = NEW.report_id;
 
	 RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION report_update_on_user_and_bicycle() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
	IF OLD.severity > 7 AND NEW.fake = TRUE AND NEW.reviewed = TRUE THEN
		UPDATE app_user
			SET warning_count = warning_count + 1
			WHERE id = OLD.user_id;
    END IF;
	
	IF OLD.severity > 7 AND NEW.fake = FALSE AND NEW.reviewed = TRUE THEN
		UPDATE bicycle
        	SET status = 'Damaged'
        	WHERE id = OLD.bicycle_id;
 	END IF;
 
	 RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION user_on_update_check_for_ban() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN

	IF NEW.warning_count = 3 THEN
		UPDATE app_user
        	SET banned = TRUE
        	WHERE id = OLD.id;
 	END IF;
 
	 RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION discount_update_on_activity() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
    UPDATE activity
        SET discounts_from = 
            discounts_from + NEW.discounts_left
        WHERE station_id = NEW.from_station_id AND day = NEW.start_time::DATE AND hour_from = (SELECT EXTRACT(HOUR FROM NEW.start_time));
		
    UPDATE activity
        SET discounts_to = 
            discounts_to + NEW.discounts_left
        WHERE station_id = NEW.to_station_id AND day = NEW.start_time::DATE AND hour_from = (SELECT EXTRACT(HOUR FROM NEW.start_time));
 
	 RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION station_on_update_change_activity() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
 	IF NEW.current_capacity = 0 THEN
		UPDATE activity
			SET was_station_empty = TRUE
			WHERE station_id = OLD.id AND day = CURRENT_DATE AND hour_from = (SELECT EXTRACT(HOUR FROM NOW()));
	END IF;
		
    IF NEW.current_capacity = OLD.max_capacity THEN
		UPDATE activity
			SET was_station_full = TRUE
			WHERE station_id = OLD.id AND day = CURRENT_DATE AND hour_from = (SELECT EXTRACT(HOUR FROM NOW()));
	END IF;
 
	 RETURN NULL;
    END;
    $$;

CREATE OR REPLACE FUNCTION transaction_on_insert_modifies_tables() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
	UPDATE bicycle
		SET station_id = NEW.planned_station_id,
		    status = 'User',
			arrival_time = NEW.planned_time::time,
			lock_number = NULL
		WHERE id = NEW.bicycle_id;
		
	UPDATE station
		SET current_capacity = current_capacity - 1
		WHERE id = NEW.start_station_id;
		
	UPDATE station
		SET current_capacity = current_capacity + 1
		WHERE id = NEW.planned_station_id;
	
	UPDATE app_user
		SET bicycle_id = NEW.bicycle_id
		WHERE id = NEW.user_id;
		
	IF NEW.discount_id IS NOT NULL THEN
		UPDATE discount
			SET discounts_left = discounts_left - 1
			WHERE id = NEW.discount_id;
			
		NEW.penalty = NEW.initial_cost * ((SELECT discount_value FROM discount WHERE id = NEW.discount_id)/100);
		NEW.initial_cost = NEW.initial_cost - NEW.penalty;
	END IF;
	
	UPDATE activity
        SET bicycles_taken = bicycles_taken + 1
        WHERE station_id = NEW.start_station_id AND day = NEW.start_time::DATE
			AND hour_from = (SELECT EXTRACT(HOUR FROM NEW.start_time));
	
 	RETURN NEW;
    END;
    $$;

CREATE OR REPLACE FUNCTION transaction_on_update_modifies_activity_and_penalty() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
 	IF NEW.finish_station_id != OLD.planned_station_id THEN
		UPDATE station
			SET current_capacity = current_capacity - 1
			WHERE id = OLD.planned_station_id;
		
		UPDATE station
			SET current_capacity = current_capacity + 1
			WHERE id = NEW.finish_station_id;
			
		UPDATE app_user
			SET warning_count = warning_count + 1
			WHERE id = OLD.user_id;
	END IF;
 
 	IF NEW.finish_station_id != OLD.planned_station_id OR NEW.finish_time > OLD.planned_time THEN
		NEW.penalty = NEW.penalty + extract(minute from NEW.finish_time - OLD.planned_time) 
			+ extract(hour from NEW.finish_time - OLD.planned_time)*60;
	ELSE
		NEW.penalty = 0;
	END IF;
 
	UPDATE bicycle
		SET station_id = NEW.finish_station_id,
			status = 'Station',
			arrival_time = NULL,
			lock_number = 1
		WHERE id = OLD.bicycle_id;

	UPDATE app_user
		SET bicycle_id = NULL
		WHERE id = OLD.user_id;
		
	UPDATE activity
        SET bicycles_brought = bicycles_brought + 1
        WHERE station_id = NEW.finish_station_id AND day = NEW.finish_time::DATE 
			AND hour_from = (SELECT EXTRACT(HOUR FROM NEW.finish_time));
	
 	RETURN NEW;
    END;
    $$;

CREATE OR REPLACE FUNCTION user_on_update_create_message() RETURNS trigger
    LANGUAGE plpgsql
    SET SCHEMA 'public'
    AS $$
    BEGIN
 
 	IF NEW.warning_count > OLD.warning_count THEN
		INSERT INTO message VALUES
			(md5(random()::text || clock_timestamp()::text)::uuid, '080fd2aa-43ff-4ddb-af72-9c2c8fdec295', OLD.id, 
			 'Atentie! Ai primit un avertisment. La trei avertismente accesul tau la aceasta aplicatie va fi blocat.', NOW(), False);
	END IF;
 
	RETURN NULL;
    END;
    $$;