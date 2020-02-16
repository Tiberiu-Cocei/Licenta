INSERT INTO city VALUES ('40e6215d-b5c6-4896-987c-f30f3678f608', 'Test');
INSERT INTO station VALUES ('41e6215d-b5c6-4896-987c-f30f3678f608', '40e6215d-b5c6-4896-987c-f30f3678f608', 'Test', '(0.0, 0.0)', 20, 3);
INSERT INTO station VALUES ('41a6215d-b5c6-4896-987c-f30f3678f608', '40e6215d-b5c6-4896-987c-f30f3678f608', 'Test2', '(2.0, 3.0)', 20, 0);
INSERT INTO station VALUES ('41d6215d-b5c6-4896-987c-f30f3678f608', '40e6215d-b5c6-4896-987c-f30f3678f608', 'Test3', '(4.0, 6.0)', 20, 1);
INSERT INTO bicycle VALUES ('42e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NULL, 'Station', 'Test1M', 1);
INSERT INTO bicycle VALUES ('43e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NULL, 'Station', 'Test2M', 1);
INSERT INTO bicycle VALUES ('44e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NULL, 'Station', 'Test3M', 1);
INSERT INTO staff VALUES ('45e6215d-b5c6-4896-987c-f30f3678f608', '190010111111', 'Driver', 'John', 'Doe', 'john_doe@test.com', '0724000111', TRUE);
INSERT INTO transport VALUES ('46e6215d-b5c6-4896-987c-f30f3678f608', '45e6215d-b5c6-4896-987c-f30f3678f608', NOW());
-- insert-ul de mai jos activeaza trigger-ul "transport_line_update_on_station_and_bicycle"
INSERT INTO transport_line VALUES ('47e6215d-b5c6-4896-987c-f30f3678f608', '46e6215d-b5c6-4896-987c-f30f3678f608', '42e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', '18:50:00', 1);
INSERT INTO payment_method VALUES ('48e6215d-b5c6-4896-987c-f30f3678f608', 'TEST_CARD_NUMBER', '2025/05/01', 'TEST1', 'TEST2');
INSERT INTO app_user VALUES ('49e6215d-b5c6-4896-987c-f30f3678f608', '48e6215d-b5c6-4896-987c-f30f3678f608', NULL, 'test_email@test.com', 'Test', 'Test_hashed', 2, FALSE);
INSERT INTO report VALUES ('50e6215d-b5c6-4896-987c-f30f3678f608', '49e6215d-b5c6-4896-987c-f30f3678f608', '43e6215d-b5c6-4896-987c-f30f3678f608', 10, 'VERY SEVERE TEST', FALSE, NULL);
INSERT INTO report VALUES ('51e6215d-b5c6-4896-987c-f30f3678f608', '49e6215d-b5c6-4896-987c-f30f3678f608', '44e6215d-b5c6-4896-987c-f30f3678f608', 10, 'VERY SEVERE TEST', FALSE, NULL);
-- cele 2 insert-uri de mai jos activeaza trigger-ele "inspection_update_on_report", "report_update_on_user_and_bicycle" si "user_on_update_check_for_ban"
INSERT INTO inspection VALUES ('50e6215d-b5c6-4896-987c-f30f3678f608', '45e6215d-b5c6-4896-987c-f30f3678f608', 'Very true yes', FALSE, NOW());
INSERT INTO inspection VALUES ('51e6215d-b5c6-4896-987c-f30f3678f608', '45e6215d-b5c6-4896-987c-f30f3678f608', 'Very false omg', TRUE, NOW());
INSERT INTO activity VALUES ('00e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 16, 17, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('01e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 17, 18, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('02e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 18, 19, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('03e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 16, 17, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('04e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 17, 18, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('05e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 18, 19, 0, 0, 0, 0, FALSE, FALSE);
INSERT INTO activity VALUES ('06e6215d-b5c6-4896-987c-f30f3678f608', '41d6215d-b5c6-4896-987c-f30f3678f608', NOW()::DATE, 17, 18, 0, 0, 0, 0, FALSE, FALSE);
-- cele 2 insert-uri de mai jos activeaza trigger-ul "discount_update_on_activity"
INSERT INTO discount VALUES('52e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', 5, 25.0, NOW(), NOW() + INTERVAL '1 HOUR');
INSERT INTO discount VALUES('53e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', 3, 20.0, NOW() + INTERVAL '1 HOUR', NOW() + INTERVAL '2 HOUR');
-- cele 2 update-uri de mai jos activeaza trigger-ul "station_on_update_change_activity"
UPDATE station SET current_capacity = 0 WHERE id = '41e6215d-b5c6-4896-987c-f30f3678f608';
UPDATE station SET current_capacity = 20 WHERE id = '41a6215d-b5c6-4896-987c-f30f3678f608';
UPDATE station SET current_capacity = 2 WHERE id = '41e6215d-b5c6-4896-987c-f30f3678f608';
UPDATE station SET current_capacity = 1 WHERE id = '41a6215d-b5c6-4896-987c-f30f3678f608';
-- cele 2 insert-uri de mai jos activeaza trigger-ul "transaction_on_insert_modifies_tables"
INSERT INTO app_transaction VALUES('54e6215d-b5c6-4896-987c-f30f3678f608', '48e6215d-b5c6-4896-987c-f30f3678f608', '49e6215d-b5c6-4896-987c-f30f3678f608', '44e6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', NULL, '52e6215d-b5c6-4896-987c-f30f3678f608', NOW(), NOW() + INTERVAL '10 MINUTE', NULL, 10.0, 0);
INSERT INTO app_transaction VALUES('55e6215d-b5c6-4896-987c-f30f3678f608', '48e6215d-b5c6-4896-987c-f30f3678f608', '49e6215d-b5c6-4896-987c-f30f3678f608', '44e6215d-b5c6-4896-987c-f30f3678f608', '41a6215d-b5c6-4896-987c-f30f3678f608', '41e6215d-b5c6-4896-987c-f30f3678f608', NULL, '53e6215d-b5c6-4896-987c-f30f3678f608', NOW(), NOW() + INTERVAL '10 MINUTE', NULL, 10.0, 0);
-- cele 2 update-uri de mai jos activeaza trigger-ul "transaction_on_update_modifies_activity_and_penalty"
UPDATE app_transaction SET finish_station_id = '41a6215d-b5c6-4896-987c-f30f3678f608', finish_time = NOW() WHERE id = '54e6215d-b5c6-4896-987c-f30f3678f608';
UPDATE app_transaction SET finish_station_id = '41d6215d-b5c6-4896-987c-f30f3678f608', finish_time = NOW() + INTERVAL '15 MINUTE' WHERE id = '55e6215d-b5c6-4896-987c-f30f3678f608';
-- update-ul de mai jos activeaza trigger-ul "user_on_warning_count_update_create_message"
UPDATE app_user SET warning_count = 3 WHERE id = '49e6215d-b5c6-4896-987c-f30f3678f608';