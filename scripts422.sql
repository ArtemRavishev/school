---У каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной.
-- У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
-- У каждой машины есть марка, модель и стоимость.
-- Также не забудьте добавить таблицам первичные ключи и связать их.

CREATE TABLE cars
(
    id BIGSERIAL primary key ,
    brand varchar(15) not null,
    model varchar(31)not null ,
    price int check ( price>0 ) not null
);
CREATE TABLE  owners
(
    id BIGSERIAL primary key ,
    name varchar(15) not null,
    age int check ( age>17 ) not null ,
    has_driver_license boolean default true not null,
    car_id bigint references cars (id) NOT NULL
);

INSERT INTO cars(brand, model, price)
values ('BMV','X8','3000000'),
('Haval','Jolion','2500000'),
('Geely','Coolray','2300000');

INSERT INTO owners(name, age, car_id)
values ('Виктор','32','1'),
       ('Анжела','28','2'),
       ('Вадим','23','3');