insert into bracket(id, leader)
values (1, 1);

insert into priority(id, name)
values (1, 'low');

insert into priority(id, name)
values (2, 'mid');

insert into priority(id, name)
values (3, 'high');

insert into state(id, name)
values (1, 'to_do');

insert into state(id, name)
values (2, 'in_progress');

insert into state(id, name)
values (3, 'done');

insert into permission(id, name)
values (1, 'worker');

insert into permission(id, name)
values (2, 'admin');

insert into car(id, brand, model, production_year, vin, capacity, type, price)
values (1, 'BMW', 'E30', 1990, '4SA4RSA4A', 3.2, 'asd', 320.5);

insert into address(id, name, surname, pesel, city, street, house_number, zip_code, telephone_number, email)
values (1, 'adrian', 'czupik', '92391329231', 'Rzeszow', 'Rejtana', 666, '32-232', 2342442424, 'czupik@gmail.com');
insert into address(id, name, surname, pesel, city, street, house_number, zip_code, telephone_number, email)
values (2, 'klaudia', 'cyran', '92391329231', 'Rzeszow', 'Rejtana', 666, '32-232', 2342442424, 'cyran@gmail.com');

insert into client(id, address_id)
values (1, 1);

insert into account(id, username, password, permission_id, bracket_id)
values (1, 'admin123', '$2a$10$YrqwJz0w08j77FujYZxHIOy4z453Eb14ncQJV.dmSjcmR9QjLdvSy', 1, 1);

insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (1, 'przygotowanie sprawozdania', 'to do to do to do to do', 1, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (2, 'umyj podloge', 'to do to do to do to do', 2, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (3, 'zrob mi loda', 'to do to do to do to do', 3, 1, 1, 1);
insert into task(id, title, content, state_id, priority_id, bracket_id, assigned_person)
values (4, 'cho na szluga', 'to do to do to do to do', 2, 1, 1, 1);

insert into employee(id, salary, employment_date, position, address_id, account_id)
values (1, 242.4, '1999-04-26 13:00:00', 'gowniak', 1, 1);

insert into comment(id, account_id, content, task_id)
values (1, 1, 'chujowe', 1);
insert into comment(id, account_id, content, task_id)
values (2, 1, 'bardzo', 1);