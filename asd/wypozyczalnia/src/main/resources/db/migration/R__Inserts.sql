insert into bracket(id, leader)
values (1, 1);

insert into priority(id, name)
values (1, 'low');

insert into priority(id, name)
values (2, 'mid');

insert into priority(id, name)
values (3, 'high');

insert into state(id, name)
values (1, 'to do');

insert into state(id, name)
values (2, 'in progress');

insert into state(id, name)
values (3, 'done');

insert into permission(id, name)
values (1, 'worker');

insert into permission(id, name)
values (2, 'admin');

insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (1, 'BMW', 'E30', 1995, '4SA4RSA4A', 3.0, 'compact', 320.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (2, 'Suzuki', 'Grand Vitara', 2008, '9EKFY23CD', 3.2, 'compact', 370);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (3, 'Audi', 'Q5', 2009, '9KUJDNFD5', 3.0, 'SAV', 450);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (4, 'Kia', 'Venga', 2014, '92KUJFN6A', 1.4, 'minivan', 150);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (5, 'Subaru', 'Baja', 2003, '525DWJDI1', 2.5, 'pick-up', 333.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (6, 'Peugeot', 'Partner', 2006, 'FEF210FE3', 1.9, 'truck', 420);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (7, 'Ford', 'Expedition', 2001, '42948KIFU', 4.6, 'jeep', 720.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (8, 'Chevrolet', 'Silverado', 2003, '97UJNDSK1', 4.3, 'pick-up', 610);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (9, 'Porsche', '911', 2004, '78JF8D7S5', 3.6, 'sport', 865);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (10, 'Ferrari', 'Enzo', 2002, '71JKNYUR6', 6.0, 'supersport', 1450.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (11, 'McLaren', 'F1', 1998, '8KJ67HGR4', 6.1, 'supersport', 1555);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (12, 'Maserati', 'GranTurism', 2009, '9K8J7H5G3', 4.2, 'grand tourer', 915.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (13, 'Aston Martin', 'Vanquish', 2014, '1KD78HFJQ', 5.9, 'grand tourer', 1020);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (14, 'Volkswagen', 'New Beetle', 2009, '76HJYUIOP', 3.0, 'cabrio', 775.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (15, 'BMW', 'E36', 1993, '4S523GWR2', 2.5, 'cabrio', 350);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (16, 'Opel', 'Vectra', 1997, '5SD4R5Q4A', 3.0, 'sport', 320.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (17, 'Dodge', 'Charger', 1976, '4SA4DS51A', 7.2, 'muscle', 2255);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (18, 'Shelby', 'Mustang GT500', 1968, 'CS34RDA41', 5.0, 'muscle', 3750.5);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (19, 'Porsche', 'Boxster', 2010, 'A5AC1DV5A', 4.0, 'sport', 680);
insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (20, 'BMW', 'X6', 2011, '1FDEDRR4A', 3.0, 'SAC', 915.5);

insert into address(id, name, surname, pesel, city, street, house_number, zip_code, telephone_number, email)
values (1, 'adrian', 'czupik', '92391329231', 'Rzeszow', 'Rejtana', 666, '32-232', 2342442424, 'czupik@gmail.com');
insert into address(id, name, surname, pesel, city, street, house_number, zip_code, telephone_number, email)
values (2, 'klaudia', 'cyran', '92391329231', 'Rzeszow', 'Rejtana', 666, '32-232', 2342442424, 'cyran@gmail.com');

insert into client(id, active, address_id)
values (1, 1, 1);

insert into account(id, username, password, permission_id, bracket_id)
values (1, 'admin123', '$2a$10$YrqwJz0w08j77FujYZxHIOy4z453Eb14ncQJV.dmSjcmR9QjLdvSy', 2, 1);
insert into account(id, username, password, permission_id, bracket_id)
values (2, 'user123', '$2a$10$YrqwJz0w08j77FujYZxHIOy4z453Eb14ncQJV.dmSjcmR9QjLdvSy', 1, 1);

insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (1, 'przygotowanie sprawozdania', 'to do to do to do to do', 1, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (2, 'umyj podloge', 'to do to do to do to do', 2, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (3, 'zrob mi loda', 'to do to do to do to do', 3, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (4, 'cho na szluga', 'to do to do to do to do', 2, 1, 1, 1);

insert into employee(id, active, salary, employment_date, position, address_id, account_id)
values (1, 1, 242.4, '1999-04-26 13:00:00', 'gowniak', 1, 1);
insert into employee(id, active, salary, employment_date, position, address_id, account_id)
values (2, 1, 242.4, '1999-04-26 13:00:00', 'dupa', 2, 2);

insert into comment(id, account_id, content, task_id)
values (1, 1, 'chujowe', 1);
insert into comment(id, account_id, content, task_id)
values (2, 1, 'bardzo', 1);