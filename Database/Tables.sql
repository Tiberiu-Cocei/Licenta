CREATE TABLE city (
	id    UUID PRIMARY KEY,
	name  TEXT NOT NULL
);

CREATE TABLE settings (
	city_id               UUID PRIMARY KEY,
	base_price            NUMERIC(12,2) NOT NULL CHECK (base_price > 0),
	interval_price        NUMERIC(12,2) NOT NULL CHECK (interval_price > 0),
	interval_time         INTEGER NOT NULL CHECK (interval_time > 0),
	discounts_used        BOOLEAN NOT NULL,
	discount_value        NUMERIC(3,1) NOT NULL CHECK (discount_value > 0), CHECK (discount_value <= 50),
	transports_used       BOOLEAN NOT NULL,
	FOREIGN KEY (city_id) REFERENCES city (id)
);

CREATE TABLE station (
	id                UUID PRIMARY KEY,
	city_id           UUID NOT NULL REFERENCES city (id),
	name              TEXT NOT NULL,
	coordinates       POINT NOT NULL,
	max_capacity      INTEGER NOT NULL CHECK (max_capacity > 0),
	current_capacity  INTEGER NOT NULL CHECK (current_capacity >= 0), CHECK (current_capacity <= max_capacity) 
);

CREATE TABLE discount (
	id               UUID PRIMARY KEY,
	from_station_id  UUID NOT NULL REFERENCES station (id),
	to_station_id    UUID NOT NULL REFERENCES station (id),
	discounts_left   INTEGER NOT NULL CHECK (discounts_left >= 0),
	discount_value   NUMERIC(3,1) NOT NULL CHECK (discount_value > 0), CHECK (discount_value <= 50),
	start_time       TIMESTAMP NOT NULL,
	end_time         TIMESTAMP NOT NULL CHECK (start_time < end_time)
);

CREATE TABLE activity (
	id                   UUID PRIMARY KEY,
	station_id           UUID NOT NULL REFERENCES station (id),
	day                  DATE NOT NULL,
	hour_from            INTEGER NOT NULL CHECK (hour_from >= 7), CHECK (hour_from <= 21),
	hour_to              INTEGER NOT NULL CHECK (hour_to >= 8), CHECK (hour_to <= 22),
	bicycles_taken       INTEGER DEFAULT 0 CHECK (bicycles_taken >= 0),
	bicycles_brought     INTEGER DEFAULT 0 CHECK (bicycles_brought >= 0),
	discounts_from       INTEGER DEFAULT 0 CHECK (discounts_from >= 0),
	discounts_to         INTEGER DEFAULT 0 CHECK (discounts_to >= 0),
	was_station_empty    BOOLEAN DEFAULT false,
	was_station_full     BOOLEAN DEFAULT false,
	CONSTRAINT unique_datetime_activity UNIQUE(station_id, day, hour_from)
);

CREATE TABLE predicted_activity (
	id                   UUID PRIMARY KEY,
	station_id           UUID NOT NULL REFERENCES station (id),
	day                  DATE NOT NULL,
	hour                 INTEGER NOT NULL CHECK (hour >= 7), CHECK (hour < 22),
	number_of_bicycles   INTEGER NOT NULL CHECK (number_of_bicycles >= 0),
	CONSTRAINT unique_datetime_pred_activity UNIQUE(station_id, day, hour)
);

CREATE TABLE payment_method (
	id            UUID PRIMARY KEY,
	card_number   TEXT NOT NULL,
	expiry_date   DATE NOT NULL,
	first_name    TEXT NOT NULL,
	last_name     TEXT NOT NULL
);

CREATE TYPE bicycle_status AS ENUM ('Station', 'Warehouse', 'Transport', 'User', 'Damaged', 'Stolen');

CREATE TABLE bicycle (
	id              UUID PRIMARY KEY,
	station_id      UUID REFERENCES station (id),
	arrival_time    TIME,
	status	        BICYCLE_STATUS NOT NULL,
	model           TEXT NOT NULL,
	lock_number     INTEGER CHECK (lock_number > 0)
);

CREATE TABLE app_user (
	id                    UUID PRIMARY KEY,
	payment_method_id     UUID REFERENCES payment_method (id),
	bicycle_id            UUID REFERENCES bicycle (id),
	email                 TEXT NOT NULL,
	username              TEXT NOT NULL,
	password              TEXT NOT NULL,
	warning_count         INTEGER DEFAULT 0,
	banned                BOOLEAN DEFAULT false,
	password_reset_code   TEXT,
	authentication_token  UUID,
	salt                  TEXT,
	CONSTRAINT app_username UNIQUE(username)
);

CREATE TABLE app_transaction (
	id                   UUID PRIMARY KEY,
	payment_method_id    UUID NOT NULL REFERENCES payment_method (id),
	user_id              UUID NOT NULL REFERENCES app_user (id),
	bicycle_id           UUID NOT NULL REFERENCES bicycle (id),
	start_station_id     UUID NOT NULL REFERENCES station (id),
	planned_station_id   UUID NOT NULL REFERENCES station (id),
	finish_station_id    UUID REFERENCES station (id),
	discount_id          UUID REFERENCES discount (id),
	start_time           TIMESTAMP NOT NULL,
	planned_time         TIMESTAMP NOT NULL,
	finish_time          TIMESTAMP,
	initial_cost         NUMERIC(11,2) NOT NULL CHECK (initial_cost > 0),
	penalty              NUMERIC(11,2) DEFAULT 0 CHECK (penalty >= 0)
);

CREATE TABLE report (
	id             UUID PRIMARY KEY,
	user_id        UUID NOT NULL REFERENCES app_user (id),
	bicycle_id     UUID NOT NULL REFERENCES bicycle (id),
	severity       NUMERIC(2) NOT NULL CHECK (severity > 0), CHECK (severity <= 10),
	description    TEXT,
	reviewed       BOOLEAN DEFAULT false,
	fake           BOOLEAN DEFAULT false
);

CREATE TABLE app_admin (
	staff_id     UUID PRIMARY KEY,
	username     TEXT NOT NULL,
	password     TEXT NOT NULL,
	CONSTRAINT admin_username UNIQUE(username),
	FOREIGN KEY (staff_id) REFERENCES staff (id)
);

CREATE TYPE staff_position AS ENUM ('Driver', 'Programmer', 'Inspector', 'Administrator', 'Cleaner');

CREATE TABLE staff (
	id            UUID PRIMARY KEY,
	CNP	      TEXT NOT NULL,
	position      STAFF_POSITION NOT NULL,
	first_name    TEXT NOT NULL,
	last_name     TEXT NOT NULL,
	email         TEXT NOT NULL,
	phone_number  TEXT NOT NULL,
	active	      BOOLEAN NOT NULL,
	available     BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE inspection (
	report_id     UUID PRIMARY KEY,
	staff_id      UUID NOT NULL REFERENCES staff (id),
	description   TEXT NOT NULL,
	fake          BOOLEAN NOT NULL,
	date          TIMESTAMP NOT NULL,
	FOREIGN KEY (report_id) REFERENCES report (id)
);

CREATE TABLE message (
	id            UUID PRIMARY KEY,
	admin_id      UUID NOT NULL REFERENCES app_admin (id),
	user_id       UUID NOT NULL REFERENCES app_user (id),
	description   TEXT NOT NULL,
	date          TIMESTAMP NOT NULL,
	seen          BOOLEAN DEFAULT false
);

CREATE TABLE transport (
	id          UUID PRIMARY KEY,
	staff_id    UUID NOT NULL REFERENCES staff (id),
	date        TIMESTAMP NOT NULL
);

CREATE TABLE transport_line (
	id              UUID PRIMARY KEY,
	transport_id    UUID NOT NULL REFERENCES transport (id),
	bicycle_id      UUID NOT NULL REFERENCES bicycle (id),
	station_from    UUID NOT NULL REFERENCES station (id),
	station_to      UUID NOT NULL REFERENCES station (id),
	arrival_time    TIME NOT NULL,
	lock_number     INTEGER NOT NULL
);