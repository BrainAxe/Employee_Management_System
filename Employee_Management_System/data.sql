drop table employee;
drop table project_employee;
drop table department;
drop table dept_project;
drop table project;
drop table overtime;
drop table overtime_employee;
create table employee
(
  E_id  varchar2(10) not null,
  E_name varchar2(15),
  E_sex varchar2(6),
  E_address varchar2(30),
  E_join_date date,
  E_contact varchar2(16),
  E_type  varchar2(12),
  Dept_id varchar2(10),
  E_salary number(10,2),
  E_tot_salary number(10,2),
  E_pass varchar2(10),
  primary key(E_id)
);

create table department
(
  Dept_id  varchar2(10) not null,
  Dept_name  varchar2(12),
  Dept_building varchar2(15),
  D_budget number,
  primary key(Dept_id)
);

create table project
(
  P_id  varchar2(10) not null,
  P_name varchar2(15),
  P_duration number,
  P_budget  number,
  P_sday  date,
  P_eday  date,
  primary key(P_id)
);

create table overtime
(
  O_id  varchar2(10) not null,
  Hourly_wage  number(6,2),
  primary key(O_id)
);

create table project_employee
(
  P_id  varchar2(10) not null,
  E_id  varchar2(10) not null,
  primary key(P_id,E_id),
  foreign key(P_id) references project(P_id),
  foreign key(E_id) references employee(E_id)
  on delete cascade  
);

create table dept_project
(
  Dept_id  varchar2(10) not null,
  P_id  varchar2(10) not null,
  primary key(P_id,Dept_id),
  foreign key(P_id) references project(P_id),
  foreign key(Dept_id) references department(Dept_id)
  on delete cascade 
);


create table overtime_employee
(
  E_id  varchar2(10) not null,
  O_id  varchar2(10) not null,
  O_duration number,
  O_date  date,
  primary key(O_id,E_id),
  foreign key(O_id) references overtime(O_id),
  foreign key(E_id) references employee(E_id)
  on delete cascade   
);
/*Data Start employee*/

insert into employee values('E-01','Tanzim','Male','Banasree','01/Jan/2002','018479685','Officer','D-01',100000,'123456');
insert into employee values('E-02','Nayeem','Male','Jatrabari','09/Mar/2014','01679854','Dept_Head','D-02',500000,'0123');
insert into employee values('E-03','Rizwan','Male','Rampura','18/Aug/2009','017779685','Project_Head','D-03',578000,'rizwan');
insert into employee values('E-04','Hasan','Male','Malibag','26/Feb/2010','0154796666','Worker','D-04',275000,'hasan');
insert into employee values('E-05','Oshy','Male','Malibag','15/Apr/2015','0111479685','Project_Head','D-01',258000,'1234');
insert into employee values('E-06','Shorna','Female','Jatrabari','17/sep/2008','0188889685','Officer','D-01',12500,'aysha');
insert into employee values('E-07','Fahmida','Female','Uttara','01/Jan/2013','011147968575','Dept_Head','D-01',200000,'12345667');
insert into employee values('E-08','Khadija','Female','Basabo','17/Dec/2012','0186669685','Project_Head','D-03',290000,'00khadija');
insert into employee values('E-09','Mohip','Male','Basabo','20/May/2010','0155559685','Dept_Head','D-03',159000,'1234000');
insert into employee values('E-10','Kafi','Male','Badda','09/Jul/2007','0177779685','Dept_Head','D-04',356000,'kafitk');

/*data start department*/

insert into department values('D-01','CSE','South',1000000); 
insert into department values('D-02','EEE','South',500000);
insert into department values('D-03','ETE','North',2750000);
insert into department values('D-04','Textile','East',9880000);

/* data start overtime*/

insert into overtime values('O-01',3500);
insert into overtime values('O-02',5000);
insert into overtime values('O-03',4500);
insert into overtime values('O-04',1000);
insert into overtime values('O-05',4500);
insert into overtime values('O-06',4500);
insert into overtime values('O-07',3500);
insert into overtime values('O-08',4500);
insert into overtime values('O-09',5000);
insert into overtime values('O-10',5000);
insert into overtime values('O-11',5000);
insert into overtime values('O-12',3500);
insert into overtime values('O-13',1000);
insert into overtime values('O-14',4500);
insert into overtime values('O-15',5000);

/*data start project*/


insert into project values('P-01','A',366,900000,'01/Jan/2002','01/Jan/2003');
insert into project values('P-02','B',10,50000,'05/Feb/2009','15/Feb/2009');
insert into project values('P-03','C',30,85000,'01/Mar/2014','30/Mar/2014');
insert into project values('P-04','D',61,200000,'30/Apr/2007','01/Jun/2007');
insert into project values('P-05','E',25,68000,'05/May/2008','30/May/2008');
insert into project values('P-06','F',100,35000,'01/Jul/2010','08/Sep/2010');
insert into project values('P-07','G',90,25000,'10/Aug/2003','08/Nov/2003');
insert into project values('P-08','H',5,10000,'01/Oct/2005','06/Oct/2005');
insert into project values('P-09','I',31,95000,'05/Dec/2006','05/Jan/2007');
insert into project values('P-10','J',20,77000,'01/Feb/2015','21/Feb/2015');

/* data start project_employee*/

insert into project_employee values('P-01','E-01');
insert into project_employee values('P-02','E-02');
insert into project_employee values('P-03','E-03');
insert into project_employee values('P-04','E-04');
insert into project_employee values('P-05','E-05');
insert into project_employee values('P-06','E-06');
insert into project_employee values('P-07','E-07');
insert into project_employee values('P-08','E-08');
insert into project_employee values('P-09','E-09');
insert into project_employee values('P-01','E-02');
insert into project_employee values('P-01','E-06');
insert into project_employee values('P-02','E-03');
insert into project_employee values('P-06','E-03');
insert into project_employee values('P-07','E-09');
insert into project_employee values('P-08','E-04');

/* data start dept_project*/

insert into dept_project values('D-01','P-01');
insert into dept_project values('D-02','P-02');
insert into dept_project values('D-03','P-03');
insert into dept_project values('D-04','P-04');
insert into dept_project values('D-01','P-05');
insert into dept_project values('D-02','P-06');
insert into dept_project values('D-03','P-07');
insert into dept_project values('D-01','P-08');
insert into dept_project values('D-02','P-09');
insert into dept_project values('D-01','P-10');


/* data start overtime_employee*/

insert into overtime_employee values('E-01','O-01',3,'01/Jan/2014');
insert into overtime_employee values('E-01','O-12',2,'10/Jan/2014');
insert into overtime_employee values('E-02','O-02',1,'06/Aug/2014');
insert into overtime_employee values('E-02','O-14',3,'21/Aug/2014');
insert into overtime_employee values('E-03','O-03',3,'25/Feb/2015');
insert into overtime_employee values('E-03','O-15',2,'26/Feb/2015');
insert into overtime_employee values('E-04','O-04',5,'09/Mar/2015');
insert into overtime_employee values('E-04','O-13',4,'29/Mar/2015');
insert into overtime_employee values('E-05','O-05',3,'06/Jan/2014');
insert into overtime_employee values('E-06','O-06',3,'28/Jan/2014');
insert into overtime_employee values('E-07','O-07',6,'19/Nov/2014');
insert into overtime_employee values('E-08','O-08',2,'02/Dec/2013');
insert into overtime_employee values('E-09','O-09',3,'27/Sep/2014');

commit;


















