CREATE FUNCTION female_faculty() RETURNS double precision AS'
 	DECLARE
 		count_female int8 := 0;
		count_faculty int8 := 0;
		percentage double precision := 0;
 		row_data professor%ROWTYPE;
 	BEGIN
 		FOR row_data IN SELECT * FROM PROFESSOR
 		LOOP
 			IF row_data.gender = ''F''
 				THEN
 					count_female := count_female + 1;
					count_faculty := count_faculty + 1;
				ELSE 
					count_faculty := count_faculty + 1;
 			END IF;
 		END LOOP;
		
		percentage := trunc((cast(count_female as decimal) / count_faculty) * 100, 2);
		
 	RETURN percentage;
 	END;
 	'LANGUAGE 'plpgsql';


CREATE FUNCTION total_people(INTEGER) RETURNS INTEGER AS'
	DECLARE
		pno ALIAS FOR $1;
		count integer := 0;
		count_grad_students integer;
		plExist integer;
		count_mg_co_professors integer;
		countPl Integer := 1;
	BEGIN
		select principal_invest_ssn INTO plExist from project where project_number = pno;
		SELECT count(project_number) INTO count_mg_co_professors from mg_co_invest where project_number = pno group by project_number;
		SELECT count(project_number) INTO count_grad_students from mg_research_assistant where project_number = pno group by project_number;
		IF count_grad_students is null
			THEN
				count_grad_students = 0;
		END IF;
		IF count_mg_co_professors is null
			THEN 
				count_mg_co_professors = 0;
		END IF;
		IF plExist is null
			THEN 
				countPl := 0;
		END IF;
		count := count_grad_students + countPl + count_mg_co_professors;
	RETURN count;
	END;
 	'LANGUAGE 'plpgsql';



CREATE FUNCTION faculty_restrict() RETURNS trigger AS'
DECLARE
	countCo int := 0;
BEGIN
	SELECT count(project_number) INTO countCo from mg_co_invest where project_number = NEW.project_number group by project_number;
	countCo := countCo + 1;
	IF NEW.professor_ssn IS NULL
		THEN RAISE EXCEPTION ''professor ssn cannot be null''; END IF;
	IF NEW.project_number IS NULL
		THEN RAISE EXCEPTION ''project number cannot be null''; END IF;
	IF countCo > 4
		THEN RAISE EXCEPTION ''All projects can only have at most four faculty members as CO''; END IF;
		
	RETURN NEW;
	END;
	'LANGUAGE 'plpgsql';

CREATE TRIGGER faculty_restrict 
BEFORE INSERT OR UPDATE 
ON mg_co_invest 
FOR EACH ROW 
EXECUTE PROCEDURE faculty_restrict();


CREATE FUNCTION student_restrict() RETURNS trigger AS'
DECLARE
	count int := 0;
BEGIN
	SELECT count(graduate_ssn) INTO count from mg_research_assistant where graduate_ssn = NEW.graduate_ssn group by graduate_ssn;
	IF NEW.graduate_ssn IS NULL
		THEN RAISE EXCEPTION ''professor ssn cannot be null''; END IF;
	IF NEW.project_number IS NULL
		THEN RAISE EXCEPTION ''project number cannot be null''; END IF;
	IF count > 1
		THEN RAISE EXCEPTION ''Students are only allowed to work on at most 2 projects''; END IF;
		
	RETURN NEW;
	END;
	'LANGUAGE 'plpgsql';

CREATE TRIGGER student_restrict 
BEFORE INSERT OR UPDATE 
ON mg_research_assistant 
FOR EACH ROW 
EXECUTE PROCEDURE student_restrict();



	
	
