import random
import string
from datetime import timedelta, datetime
import psycopg2
import requests
import uuid

first_names, last_names = [], []
staff_list, admin_list, bicycle_list, station_list, city_list, activity_list, user_list, transaction_list = [], [], [], [], [], [], [], []
db_connection = None


class Transaction:
    def __init__(self, transaction_id, payment_id, user_id, bicycle_id, start_station_id, planned_station_id, finish_station_id, discount_id, start_time, planned_time, finish_time, initial_cost, penalty):
        self.transaction_id = transaction_id
        self.payment_id = payment_id
        self.user_id = user_id
        self.bicycle_id = bicycle_id
        self.start_station_id = start_station_id
        self.planned_station_id = planned_station_id
        self.finish_station_id = finish_station_id
        self.discount_id = discount_id
        self.start_time = start_time
        self.planned_time = planned_time
        self.finish_time = finish_time
        self.initial_cost = initial_cost
        self.penalty = penalty

        global transaction_list
        transaction_list.append(self)


class Staff:
    def __init__(self, first_name, last_name, position):
        self.staff_id = str(uuid.uuid4())
        if first_name[-1] in ['a', 'e', 'i', 'o', 'u']:
            self.cnp = '2'
        else:
            self.cnp = '1'
        year_range = random.randint(0, 1)
        if year_range == 0:
            self.cnp += str(random.randint(70, 99))
        else:
            self.cnp += '0' + str(random.randint(0, 2))
        month_range = random.randint(0, 1)
        if month_range == 0:
            self.cnp += '0' + str(random.randint(1, 9))
        else:
            self.cnp += str(random.randint(10, 12))
        day_range = random.randint(0, 1)
        if day_range == 0:
            self.cnp += '0' + str(random.randint(1, 9))
        else:
            self.cnp += str(random.randint(10, 30))
        self.cnp += str(random.randint(10000, 99999))
        if position == 1:
            self.position = 'Inspector'
        elif position == 2:
            self.position = 'Driver'
        elif position == 3:
            self.position = 'Programmer'
        elif position == 4:
            self.position = 'Administrator'
        elif position == 5:
            self.position = 'Cleaner'
        self.first_name = first_name
        self.last_name = last_name
        self.email = first_name + '.' + last_name + '@gmail.com'
        self.phone_number = '07' + str(random.randint(11, 30)) + str(random.randint(100000, 999999))
        self.busy = False

        global staff_list
        staff_list.append(self)


class Admin:
    def __init__(self, first_name, last_name):
        self.admin_id = str(uuid.uuid4())
        self.email = first_name + '.' + last_name + '@gmail.com'
        self.username = first_name + '_' + last_name
        characters = string.ascii_letters + string.digits + string.punctuation
        self.password = ''.join(random.choice(characters) for i in range(14))
        self.first_name = first_name
        self.last_name = last_name

        global admin_list
        admin_list.append(self)


class Bicycle:
    def __init__(self, station_id):
        self.bicycle_id = str(uuid.uuid4())
        self.station_id = station_id
        self.arrival_time = None
        self.status = 'Station'
        characters = string.ascii_letters
        self.model = ''.join(random.choice(characters) for i in range(3))
        self.model += '-' + str(random.randint(100, 950))
        self.lock_number = 1

        global bicycle_list
        bicycle_list.append(self)

    def change_status(self, status, station_id=None, arrival_time=None):
        self.status = status
        if station_id is not None:
            self.station_id = station_id
        if arrival_time is not None:
            self.arrival_time = arrival_time


class Station:
    def __init__(self, city_id, name, coordinates, max_capacity):
        self.station_id = str(uuid.uuid4())
        self.city_id = city_id
        self.name = name
        self.coordinates = coordinates
        self.current_capacity = 0
        self.max_capacity = max_capacity
        self.bicycle_list = []

        global station_list
        station_list.append(self)

    def take_bicycle(self, time):
        if self.current_capacity > 0:
            try:
                bicycle = random.choice([i for i in self.bicycle_list if i.arrival_time is None or i.arrival_time < time])
                self.bicycle_list.remove(bicycle)
                self.current_capacity = self.current_capacity - 1
                return bicycle
            except IndexError:
                return None
        return None

    def put_bicycle(self, bicycle):
        if self.current_capacity < self.max_capacity:
            self.current_capacity = self.current_capacity + 1
            self.bicycle_list.append(bicycle)
            return True
        return False


class CityAndSettings:
    def __init__(self, name):
        self.city_id = str(uuid.uuid4())
        self.name = name

        self.base_price = 3.5
        self.interval_price = 1.25
        self.interval_time = 5
        self.are_discounts_used = True
        self.discount_value = 15.0
        self.are_transports_used = True

        global city_list
        city_list.append(self)


class Activity:
    def __init__(self, station_id, day, hour_from, hour_to):
        self.activity_id = str(uuid.uuid4())
        self.station_id = station_id
        self.day = day
        self.hour_from = hour_from
        self.hour_to = hour_to
        self.bicycles_taken = 0
        self.bicycles_brought = 0
        self.discounts_from = 0
        self.discounts_to = 0
        self.was_station_empty = False
        self.was_station_full = False

        global activity_list
        activity_list.append(self)


def random_date():
    start = datetime.strptime('2021/1/1', '%Y/%m/%d')
    end = datetime.strptime('2024/1/1', '%Y/%m/%d')
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return (start + timedelta(seconds=random_second)).date()


class UserAndPaymentMethod:
    def __init__(self, first_name, last_name):
        self.user_id = str(uuid.uuid4())
        self.bicycle_id = None
        self.payment_method_id = str(uuid.uuid4())
        self.email = first_name + '.' + last_name + '@gmail.com'
        self.username = first_name + '_' + last_name + str(random.randint(0, 99))
        characters = string.ascii_letters + string.digits + string.punctuation
        self.password = ''.join(random.choice(characters) for i in range(14))
        self.warning_count = 0
        self.banned = False

        self.card_number = str(random.randint(1000, 9999)) + str(random.randint(1000, 9999)) + str(
            random.randint(1000, 9999)) + str(random.randint(1000, 9999))
        self.expiry_date = random_date()
        self.first_name = first_name
        self.last_name = last_name

        global user_list
        user_list.append(self)

    def give_warning(self):
        self.warning_count = self.warning_count + 1
        if self.warning_count == 3:
            self.banned = True


def random_person_info():
    global first_names, last_names
    resp = requests.get('https://uinames.com/api/?amount=500')
    for item in resp.json():
        first_names.append(item['name'].encode('ascii', errors='ignore').decode().capitalize())
        last_names.append(item['surname'].encode('ascii', errors='ignore').decode().capitalize())
    first_names = list(filter(lambda x: len(x) >= 3, first_names))
    last_names = list(filter(lambda x: len(x) >= 3, last_names))


def get_database_parameters():
    parameters = dict()
    file = open("database.ini", "r")
    for line in file:
        info = line.split("=")
        parameters[info[0]] = info[1][:-1]
    return parameters


def get_database_connection():
    global db_connection
    if db_connection is None:
        try:
            parameters = get_database_parameters()
            connection = psycopg2.connect(**parameters)
            return connection
        except Exception as error:
            print(error)
    else:
        return db_connection


def clear_tables():
    try:
        connection = get_database_connection()
        cursor = connection.cursor()
        cursor.execute('TRUNCATE activity CASCADE')
        cursor.execute('TRUNCATE app_admin CASCADE')
        cursor.execute('TRUNCATE app_transaction CASCADE')
        cursor.execute('TRUNCATE app_user CASCADE')
        cursor.execute('TRUNCATE bicycle CASCADE')
        cursor.execute('TRUNCATE city CASCADE')
        cursor.execute('TRUNCATE discount CASCADE')
        cursor.execute('TRUNCATE inspection CASCADE')
        cursor.execute('TRUNCATE message CASCADE')
        cursor.execute('TRUNCATE payment_method CASCADE')
        cursor.execute('TRUNCATE predicted_activity CASCADE')
        cursor.execute('TRUNCATE report CASCADE')
        cursor.execute('TRUNCATE settings CASCADE')
        cursor.execute('TRUNCATE staff CASCADE')
        cursor.execute('TRUNCATE station CASCADE')
        cursor.execute('TRUNCATE transport CASCADE')
        cursor.execute('TRUNCATE transport_line CASCADE')

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def create_persons():
    global first_names, last_names
    random_person_info()
    for i in range(0, 5):
        Staff(random.choice(first_names), random.choice(last_names), 1)
    for i in range(0, 5):
        Staff(random.choice(first_names), random.choice(last_names), 2)
    for i in range(0, 8):
        Staff(random.choice(first_names), random.choice(last_names), 3)
    for i in range(0, 2):
        Staff(random.choice(first_names), random.choice(last_names), 5)

    for i in range(0, 3):
        admin_first_name = random.choice(first_names)
        admin_last_name = random.choice(last_names)
        Staff(admin_first_name, admin_last_name, 4)
        Admin(admin_first_name, admin_last_name)

    for i in range(0, 5000):
        UserAndPaymentMethod(random.choice(first_names), random.choice(last_names))

    try:
        global staff_list, admin_list, user_list
        connection = get_database_connection()
        cursor = connection.cursor()
        for staff in staff_list:
            cursor.execute("INSERT INTO staff VALUES (%s, %s, %s, %s, %s, %s, %s, %s)", (staff.staff_id, staff.cnp, staff.position, staff.first_name, staff.last_name, staff.email, staff.phone_number, True))
        for admin in admin_list:
            cursor.execute("INSERT INTO app_admin VALUES (%s, %s, %s)", (admin.admin_id, admin.username, admin.password))
        for user in user_list:
            cursor.execute("INSERT INTO app_user VALUES (%s, %s, %s, %s, %s, %s, %s, %s)", (user.user_id, None, None, user.email, user.username, user.password, 0, False))
            cursor.execute("INSERT INTO payment_method VALUES (%s, %s, %s, %s, %s)", (user.payment_method_id, user.card_number, user.expiry_date, user.first_name, user.last_name))
            cursor.execute("UPDATE app_user SET payment_method_id = %s WHERE id = %s", (user.payment_method_id, user.user_id))

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def create_city_and_settings():
    CityAndSettings('Iasi')

    try:
        global city_list
        connection = get_database_connection()
        cursor = connection.cursor()
        for city in city_list:
            cursor.execute("INSERT INTO city VALUES (%s, %s)", (city.city_id, city.name))
            cursor.execute("INSERT INTO settings VALUES (%s, %s, %s, %s, %s, %s, %s)", (city.city_id, city.base_price, city.interval_price, city.interval_time, True, city.discount_value, True))

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def create_stations():
    global city_list
    Station(city_list[0].city_id, 'Bucium', '(47.114611, 27.632425)', 15)
    Station(city_list[0].city_id, 'UAIC', '(47.173949, 27.575110)', 25)
    Station(city_list[0].city_id, 'Palas', '(47.158254, 27.588857)', 30)
    Station(city_list[0].city_id, 'Iulius', '(47.156147, 27.605260)', 25)
    Station(city_list[0].city_id, 'Copou', '(47.186689, 27.562538)', 20)
    Station(city_list[0].city_id, 'Tesatura', '(47.151555, 27.597913)', 20)
    Station(city_list[0].city_id, 'Nicolina', '(47.141690, 27.579738)', 20)
    Station(city_list[0].city_id, 'Poitiers', '(47.136875, 27.598974)', 20)
    Station(city_list[0].city_id, 'Tatarasi', '(47.165001, 27.603857)', 20)

    try:
        global station_list
        connection = get_database_connection()
        cursor = connection.cursor()
        for station in station_list:
            cursor.execute("INSERT INTO station VALUES (%s, %s, %s, %s, %s, %s)", (station.station_id, station.city_id, station.name, station.coordinates, station.max_capacity, station.current_capacity))

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def create_bicycles():
    global station_list
    for station in station_list:
        for i in range(0, station.max_capacity - 7):
            bicycle = Bicycle(station.station_id)
            station.current_capacity = station.current_capacity + 1
            station.bicycle_list.append(bicycle)

    try:
        global bicycle_list
        connection = get_database_connection()
        cursor = connection.cursor()
        for bicycle in bicycle_list:
            cursor.execute("INSERT INTO bicycle VALUES (%s, %s, %s, %s, %s, %s)", (bicycle.bicycle_id, bicycle.station_id, bicycle.arrival_time, bicycle.status, bicycle.model, bicycle.lock_number))

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def insert_daily_activity():
    global station_list, user_list, bicycle_list, city_list, activity_list, staff_list, transaction_list
    current = datetime.strptime('2020/1/1 00:00:00', '%Y/%m/%d %H:%M:%S')
    end = datetime.strptime('2020/6/15 00:00:00', '%Y/%m/%d %H:%M:%S')
    while current < end:
        print(current)
        for hour_of_departure in range(7, 22):
            for station in station_list:
                Activity(station.station_id, current.date(), hour_of_departure, hour_of_departure + 1)
        for hour_of_departure in range(7, 22):
            for user in user_list:
                user.bicycle_id = None
            for station in station_list:
                valid_stations = station_list.copy()
                valid_stations.remove(station)
                departures = random.randint(0, station.current_capacity)
                start_activity = list(filter(lambda x: x.station_id == station.station_id and x.day == current.date() and x.hour_from == hour_of_departure, activity_list))[0]
                for j in range(0, departures):
                    minute_of_departure = random.randint(0, 59)
                    departure_time = current.replace(hour=hour_of_departure, minute=minute_of_departure)
                    bicycle = station.take_bicycle(departure_time)

                    if bicycle is None:
                        continue

                    if len(station.bicycle_list) < 5:
                        try:
                            for _station in station_list:
                                if len(_station.bicycle_list) + 5 > _station.max_capacity:
                                    transport_id = str(uuid.uuid4())
                                    staff_id = random.choice([i.staff_id for i in staff_list if i.position == 'Driver'])
                                    transport_time = departure_time + timedelta(minutes=15)
                                    connection = get_database_connection()
                                    cursor = connection.cursor()
                                    cursor.execute("INSERT INTO transport VALUES (%s, %s, %s)", (transport_id, staff_id, departure_time))
                                    connection.commit()
                                    for k in range(0, 5):
                                        transport_line_id = str(uuid.uuid4())
                                        _bicycle = _station.take_bicycle(departure_time)
                                        if _bicycle is None:
                                            continue
                                        _bicycle.arrival_time = transport_time
                                        station.put_bicycle(_bicycle)
                                        cursor.execute("INSERT INTO transport_line VALUES (%s, %s, %s, %s, %s, %s, %s)",
                                                       (transport_line_id, transport_id, _bicycle.bicycle_id, _station.station_id, station.station_id, transport_time, 0))
                                    connection.commit()
                                    cursor.close()
                                    break
                        except (Exception, psycopg2.DatabaseError) as error:
                            print(error)

                    start_activity.bicycles_taken = start_activity.bicycles_taken + 1
                    if len(station.bicycle_list) == 0:
                        start_activity.was_station_empty = True

                    penalty = 0.0
                    transaction_id = str(uuid.uuid4())
                    user = random.choice([i for i in user_list if i.bicycle_id is None and not i.banned])
                    user.bicycle_id = bicycle.bicycle_id
                    planned_destination = random.choice([i for i in valid_stations if i.current_capacity < i.max_capacity])
                    is_planned_destination = random.randint(0, 100)
                    if is_planned_destination < 99:
                        finish_destination = planned_destination
                    else:
                        remaining_stations = valid_stations.copy()
                        remaining_stations.remove(planned_destination)
                        finish_destination = random.choice([i for i in remaining_stations if i.current_capacity < i.max_capacity])
                        penalty = penalty + 5.0
                        user.give_warning()
                    if minute_of_departure < 55:
                        planned_arrival_hour = random.randint(0, 2)
                    else:
                        planned_arrival_hour = random.randint(1, 2)
                    if planned_arrival_hour == 0:
                        planned_arrival_minute = random.randint(minute_of_departure, 59)
                    else:
                        planned_arrival_minute = random.randint(0, 59)
                    planned_arrival_time = departure_time.replace(hour=planned_arrival_hour + departure_time.hour, minute=planned_arrival_minute)
                    is_on_time = random.randint(0, 100)
                    if is_on_time < 97:
                        arrival_time = planned_arrival_time - timedelta(minutes=random.randint(0, 5))
                    else:
                        extra_minutes = random.randint(5, 60)
                        if extra_minutes > 30:
                            user.give_warning()
                        arrival_time = planned_arrival_time + timedelta(minutes=extra_minutes)
                        penalty = penalty + extra_minutes * 0.5
                    total_minutes = (planned_arrival_time - departure_time).total_seconds() // 60
                    initial_cost = city_list[0].base_price + (total_minutes // city_list[0].interval_time + 1) * city_list[0].interval_price
                    bicycle.arrival_time = arrival_time
                    finish_destination.put_bicycle(bicycle)

                    try:
                        end_activity = list(filter(lambda x: x.station_id == finish_destination.station_id and x.day == current.date() and x.hour_from == planned_arrival_time.hour, activity_list))[0]
                        end_activity.bicycles_brought = end_activity.bicycles_brought + 1
                        if len(finish_destination.bicycle_list) == finish_destination.max_capacity:
                            end_activity.was_station_full = True
                    except IndexError:
                        pass

                    Transaction(transaction_id, user.payment_method_id, user.user_id, bicycle.bicycle_id,
                                station.station_id, planned_destination.station_id, finish_destination.station_id, None,
                                departure_time, planned_arrival_time, arrival_time, initial_cost, penalty)

            random.shuffle(station_list)
        current = current + timedelta(days=1)
    connection = None
    try:
        connection = get_database_connection()
        cursor = connection.cursor()
        for user in user_list:
            cursor.execute("UPDATE app_user SET warning_count = %s, banned = %s WHERE id = %s", (user.warning_count, user.banned, user.user_id))
        for station in station_list:
            cursor.execute("UPDATE station SET current_capacity = %s WHERE id = %s",
                           (station.current_capacity, station.station_id))
        for bicycle in bicycle_list:
            cursor.execute("UPDATE bicycle SET station_id = %s, arrival_time = %s, status = %s WHERE id = %s",
                           (bicycle.station_id, None, 'Station', bicycle.bicycle_id))
        for activity in activity_list:
            cursor.execute("INSERT INTO activity VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                           (activity.activity_id, activity.station_id, activity.day, activity.hour_from, activity.hour_to,
                            activity.bicycles_taken, activity.bicycles_brought, 0, 0, activity.was_station_empty, activity.was_station_full))
        for transaction in transaction_list:
            cursor.execute("INSERT INTO app_transaction VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                           (transaction.transaction_id, transaction.payment_id, transaction.user_id, transaction.bicycle_id, transaction.start_station_id,
                            transaction.planned_station_id, transaction.finish_station_id, transaction.discount_id, transaction.start_time,
                            transaction.planned_time, transaction.finish_time, transaction.initial_cost, transaction.penalty))

        connection.commit()
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if connection is not None:
            connection.close()


if __name__ == '__main__':
    clear_tables()
    create_persons()
    create_city_and_settings()
    create_stations()
    create_bicycles()
    insert_daily_activity()
