create schema medical_center;

create table medical_center.client(
id int not null primary key auto_increment,
name varchar(255) not null,
surname varchar(255) not null,
telephone_number varchar(255) not null,
user_id int,
state varchar(255) not null
);

create table medical_center.department(
id int not null primary key auto_increment,
name varchar(255) not null,
department_id int,
state varchar(255) not null
);

create table medical_center.med_card(
id int not null primary key auto_increment,
client_id int not null,
date date not null,
personal_job_id int,
service_id int,
complaints varchar(255) not null,
diagnosis varchar(255) not null,
examination_result varchar(255),
appointment varchar(255) not null,
state varchar(255) not null
);

create table medical_center.personal(
id int not null primary key auto_increment,
name varchar(255) not null,
surname varchar(255) not null,
birth_date date not null,
employment_date date not null,
dismissal_date date,
experience int not null,
photo varchar(255),
state varchar(255) not null
);

create table medical_center.personalJob(
id int not null primary key auto_increment,
personal_id int not null,
job_title varchar(255) not null,
department_id int not null,
user_id int,
state varchar(255) not null
);

create table medical_center.schedule(
id int not null primary key auto_increment,
service_id int,
personal_job_id int,
date date,
start_time time,
end_time time,
day_off varchar(255),
state varchar(255) not null
);

create table medical_center.service(
id int not null primary key auto_increment,
type varchar(255),
price double,
state varchar(255) not null
);

create table medical_center.ticket(
id int not null primary key auto_increment,
service_id int not null,
personal_job_id int,
date date not null,
time time not null,
client_id int,
state varchar(255) not null,
cancel_request_from_role varchar(255)
);

create table medical_center.user(
id int not null primary key auto_increment,
name varchar(255) not null,
email varchar(255) not null,
user_credentials int not null,
state varchar(255) not null,
role varchar(255) not null
);

create table medical_center.user_credentials(
id int not null primary key auto_increment,
login varchar(255) not null,
password varchar(255) not null,
state varchar(255) not null
);