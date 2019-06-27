drop table if exists gc_schedule_log;
create table gc_schedule_log(id int not null primary key AUTO_INCREMENT,person_id int not null,person_name char(15) not null,events_date char(10) not null,from_events varchar(15) not null,to_events varchar(15),log_date char(19) not null);
