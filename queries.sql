create table Department (
    dept_number int not null unique, 
    name varchar(30),
    main_office varchar(30),
    chairman_ssn int,
	primary key(dept_number)
);

create table Professor(
	ssn int not null unique,
    name varchar(30),
    age int,
    gender varchar(10),
    research_specialty varchar(30),
    professor_rank int,
    dept_number int, 
    primary key(ssn)
);

create table Project(
project_number int not null,
budget float,
starting_date DATE,
ending_date DATE,
sponsor_name varchar(30),
principal_invest_ssn int,
primary key(project_number)
);

create table Graduate_Student(
ssn int not null unique, 
name varchar(30),
age int,
gender varchar(10),
degree_program varchar(20),
courses varchar(50),
dept_number int, 
student_advisor_ssn int,
primary key(ssn)
);

create table mg_co_invest(
professor_ssn int, 
project_number int, 
foreign key(professor_ssn) references Professor(ssn) ON DELETE CASCADE,
foreign key(project_number) references Project(project_number) ON DELETE CASCADE
);

create table mg_research_assistant(
graduate_ssn int,
project_number int,
foreign key(graduate_ssn) references Graduate_Student(ssn) ON DELETE CASCADE,
foreign key(project_number) references Project(project_number) ON DELETE CASCADE
);


ALTER TABLE department ADD FOREIGN KEY (chairman_ssn) REFERENCES professor(ssn) ON DELETE SET NULL;

ALTER TABLE professor ADD FOREIGN KEY (dept_number) REFERENCES Department(dept_number) ON DELETE SET NULL;

ALTER TABLE project ADD FOREIGN KEY (principal_invest_ssn) REFERENCES professor(ssn) ON DELETE SET NULL;

ALTER TABLE graduate_student ADD FOREIGN KEY (dept_number) REFERENCES department(dept_number) ON DELETE SET NULL;

ALTER TABLE graduate_student ADD FOREIGN KEY (student_advisor_ssn) REFERENCES graduate_student(ssn) ON DELETE SET NULL;

-- inserting values for department table 
insert into department (dept_number, name, main_office) values (1,'Biology','FA 240');
insert into department (dept_number, name, main_office) values (2,'Computer Science','Physical Sciences 55');
insert into department (dept_number, name, main_office) values (3,'History','BIO 255');
insert into department (dept_number, name, main_office) values (4,'Applied Arts and Sciences','FA 102');
insert into department (dept_number, name, main_office) values (5,'Physics','BIO 105');
insert into department (dept_number, name, main_office) values (6,'Math','SH 100');

-- inserting values for professor table 
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (75849348,'Erick Farias',45, 'M', 'Human Body', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (232345698,'Jake Tyler', 28, 'M', 'Machnie Learning', 8);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (786594838,'Cristina Sanchez', 30, 'F', 'Human Rights', 9);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (983827478, 'Anglica Ramos', 25, 'F', 'Law', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (147852369, 'Carlos Mendoza', 23, 'M', 'Machnie Learning', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (963258741, 'Tia Nicole', 28, 'F', 'Human Rights', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (123456789, 'Ricardo Medina', 38, 'M', 'Law', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (619536251, 'Isabella Mendoza', 25, 'F', 'Machine Learning', 10);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (744527545, 'Sabrina Farias', 29, 'F', 'Law', 7);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (689457815, 'Bryce Hall', 26, 'M', 'Astrophysics', 9);
insert into professor (ssn, name, age, gender, research_specialty, professor_rank) values (458712563, 'Tom Haris', 39, 'M', 'Crytpography', 10);

-- inserting values for project table 
insert into project (project_number, budget, starting_date, ending_date, sponsor_name) values (1, 2000, '2020-2-04', '2020-5-15', 'NSF');
insert into project (project_number, budget, starting_date, ending_date, sponsor_name) values (2, 5000, '2020-3-04', '2020-6-20', 'NSF');
insert into project (project_number, budget, starting_date, ending_date, sponsor_name) values (3, 3000, '2020-4-04', '2020-7-28', 'NSF');

-- inserting values for graduate_student table 
insert into graduate_student (ssn, name, age, gender, degree_program, courses) values (3047852, 'Gonze Gonzalez', 23, 'M', 'P.H.D.', 'Data Science');
insert into graduate_student (ssn, name, age, gender, degree_program, courses) values (3147852, 'Tom Hang', 31, 'M', 'M.S.', 'Database');
insert into graduate_student (ssn, name, age, gender, degree_program, courses) values (3045842, 'Jocelyn Martinez', 32, 'F', 'P.H.D.', 'Government');
insert into graduate_student (ssn, name, age, gender, degree_program, courses) values (3144587, 'Adriana Perez', 24, 'F', 'M.S.', 'Art 101');
insert into graduate_student (ssn, name, age, gender, degree_program, courses) values (3964842, 'Nomeli Arellano', 18, 'F','M.S.', 'Art 302');

-- inserting values for department table 
UPDATE department SET  chairman_ssn = 75849348 where dept_number = 1;
UPDATE department SET  chairman_ssn = 147852369 where dept_number = 2;
UPDATE department SET  chairman_ssn = 963258741 where dept_number = 3;
UPDATE department SET  chairman_ssn = 123456789 where dept_number = 4;
UPDATE department SET  chairman_ssn = 689457815 where dept_number = 5;
UPDATE department SET  chairman_ssn = 458712563 where dept_number = 6;

UPDATE professor SET  dept_number = 4 where ssn = 983827478;
UPDATE professor SET  dept_number = 2 where ssn = 147852369;
UPDATE professor SET  dept_number = 3 where ssn = 963258741;
UPDATE professor SET  dept_number = 4 where ssn = 123456789;
UPDATE professor SET  dept_number = 4 where ssn = 744527545;
UPDATE professor SET  dept_number = 2 where ssn = 619536251;
UPDATE professor SET  dept_number = 5 where ssn = 689457815;
UPDATE professor SET  dept_number = 6 where ssn = 458712563;
UPDATE professor SET  dept_number = 1 WHERE ssn = 75849348;
UPDATE professor SET  dept_number = 2 WHERE ssn = 232345698;
UPDATE professor SET  dept_number = 3 WHERE ssn = 786594838;


UPDATE project SET  principal_invest_ssn = 75849348 where project_number = 1;
UPDATE project SET  principal_invest_ssn = 123456789 where project_number = 2;
UPDATE project SET  principal_invest_ssn = 983827478 where project_number = 3;

UPDATE graduate_student SET  dept_number = 2, student_advisor_ssn = 3045842 where ssn = 3047852;
UPDATE graduate_student SET  dept_number = 2, student_advisor_ssn = 3045842 where ssn = 3147852;
UPDATE graduate_student SET  dept_number = 3, student_advisor_ssn = 3964842 where ssn = 3045842;
UPDATE graduate_student SET  dept_number = 4, student_advisor_ssn = 3964842 where ssn = 3144587;
UPDATE graduate_student SET  dept_number = 4, student_advisor_ssn = 3147852 where ssn = 3964842;

insert into mg_research_assistant (graduate_ssn, project_number) values (3045842, 2);
insert into mg_research_assistant (graduate_ssn, project_number) values (3144587, 1);
insert into mg_research_assistant (graduate_ssn, project_number) values (3964842, 1);

insert into mg_co_invest (professor_ssn, project_number) values (744527545, 1);
insert into mg_co_invest (professor_ssn, project_number) values (983827478, 2);
insert into mg_co_invest (professor_ssn, project_number) values (123456789, 3);






























