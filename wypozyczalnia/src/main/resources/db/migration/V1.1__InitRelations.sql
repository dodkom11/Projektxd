alter table client
    add foreign key(address_id) references address(id);

alter table account
    add foreign key(group_id) references `group`(id);

alter table comment
    add foreign key(account_id) references account(id);
alter table comment
    add foreign key(task_id) references task(id);

alter table employee
    add foreign key(address_id) references address(id);
alter table employee
    add foreign key(account_id) references account(id);

alter table rent
    add foreign key(employee_id) references employee(id);
alter table rent
    add foreign key(client_id) references client(id);
alter table rent
    add foreign key(car_id) references car(id);

alter table task
    add foreign key(group_id) references `group`(id);
alter table task
    add foreign key(assigned_person) references account(id);