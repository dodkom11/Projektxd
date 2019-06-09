CREATE TABLE `permission` (
    id              bigint NOT NULL AUTO_INCREMENT,
    name            VARCHAR(15) ,
    PRIMARY KEY (id)
);

CREATE TABLE `priority` (
    id              bigint NOT NULL AUTO_INCREMENT,
    name            VARCHAR(15) ,
    PRIMARY KEY (id)
);

CREATE TABLE `state` (
    id              bigint NOT NULL AUTO_INCREMENT,
    name            VARCHAR(15) ,
    PRIMARY KEY (id)
);

CREATE TABLE `car` (
    id              bigint NOT NULL AUTO_INCREMENT,
    brand           VARCHAR(60) NOT NULL,
    model           VARCHAR(60) NOT NULL,
    production_year int NOT NULL,
    vin             VARCHAR(30) NOT NULL,
    capacity        float NOT NULL,
    type            VARCHAR(30),
    price           float NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE `address` (
    id               bigint NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50),
    surname          VARCHAR(50),
    pesel            VARCHAR(25),
    city             VARCHAR(50),
    street           VARCHAR(50),
    house_number     int,
    zip_code         VARCHAR(25),
    telephone_number bigint,
    email            VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE `client` (
    id        bigint NOT NULL AUTO_INCREMENT,
    active    BOOLEAN not null default 1,
    address_id bigint,
    PRIMARY KEY (id)
);

CREATE TABLE `bracket` (
    id     bigint NOT NULL AUTO_INCREMENT,
    leader bigint,
    PRIMARY KEY (id)
);

CREATE TABLE `account` (
    id            bigint NOT NULL AUTO_INCREMENT,
    username      VARCHAR(40),
    password      VARCHAR(200),
    permission_id bigint,
    bracket_id    bigint,
    PRIMARY KEY (id)
);

CREATE TABLE `comment` (
    id          bigint NOT NULL AUTO_INCREMENT,
    account_id  bigint,
    content     VARCHAR(500),
    task_id     bigint,
    PRIMARY KEY (id)
);

CREATE TABLE `employee` (
    id              bigint NOT NULL AUTO_INCREMENT,
    salary          double DEFAULT 0,
    employment_date DATETIME,
    position        VARCHAR(30),
    address_id      bigint,
    account_id      bigint,
    PRIMARY KEY (id)
);

CREATE TABLE `rent` (
    id          bigint NOT NULL AUTO_INCREMENT,
    employee_id bigint,
    car_id      bigint,
    client_id   bigint,
    rental_time int,
    rental_date DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE `task` (
    id          bigint NOT NULL AUTO_INCREMENT,
    title       VARCHAR(50),
    content     VARCHAR(150),
    state_id      bigint,
    priority_id    bigint,
    bracket_id    bigint,
    assigned_person  bigint,
    PRIMARY KEY (id)
);




