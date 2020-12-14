package cs4222;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class JavaJDBC {
	static Connection connection = null;
	static String url = "jdbc:postgresql://cs1.calstatela.edu:5432/cs4222s20";
	static String userName = "cs4222s20";
	static String password = "jMvmaEdj";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true)
		{
			getOptions();
		}
	}

	// Display Options for User
	public static void getOptions() {
		String outPut = "Select a method you want to execute: " + "\n 1. Add a faculty" + "\n 2. Remove a faculty"
				+ "\n 3. Add a project\"" + "\n 4. Remove a project"
				+ "\n 5. Display the project principal-investigator, the project's co-investigators, and student information of a specific project"
				+ "\n 6. Display all students a professor supervises, and the project each student works on \n 7. Call function female_faculty \n"
				+ " 8. Call function total_people\n 9. Assign Project to Faculty(Test Trigger Function faculty_restrict) "
				+ "\n 10. Assign Project to Student(Test Trigger Function student_restrict) \n"
				+ " 11. Display Faculty \n 12. Display Projects \n 13. EXIT\n";

		Scanner scanner = new Scanner(System.in);
		int input = 0;
		do {
			System.out.println(outPut);
			input = scanner.nextInt();
			switch (input) {
			case 1: // case 1: Add New Faculty
				addNewFaculty();
				break;
			case 2: // case 2: Remove a Faculty
				removeFaculty();
				break;
			case 3: // case 3: Add a new project
				addNewProject();
				break;
			case 4:// case 4: remove a project
				removeProject();
				break;
			case 5:// display the pl, co's, students for a specific project
				printProjectTeam();
				break;
			case 6:// case 6: display all the students a supervior supervises and the projects the students are working on
				printStudentSupervisee();
				break;
			case 7:// case 7: call function female_faculty()
					femaleFaculty();
				break;
			case 8:// get count of how many people work for a specific project
					totalPeople();
				break;
			case 9:// this is to test trigger function of faculty_restrict
				assignFacultyToProject();
			break;
			case 10:// this is to test the trigger function of student_restrict
				assignStudentProject();
			break;
			case 11: //display all professors
				displayProfessors();
				break;
			case 12: //display all projects
				displayProjects();
				break;
			case 13://case 11: exit the program
				System.out.println("You exited the program.");
				System.exit(0);
				break;
			default:
				System.out.println("You entered an invalid option!");
			}
		} while (input < 1 || input > 7);
	}

	// Add New Faculty (Professor)
	public static void addNewFaculty() {
		Scanner scanner = new Scanner(System.in);
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			System.out.println("Enter Gender: ");
			String gender = scanner.next();
			scanner.nextLine();

			System.out.println("Enter Full Name: ");
			String name = scanner.nextLine();

			System.out.println("Enter Research Specialty: ");
			String researchSpecialty = scanner.nextLine();

			System.out.println("\nEnter ssn: ");
			int ssn = scanner.nextInt();

			System.out.println("Enter Age: ");
			int age = scanner.nextInt();

			System.out.println("Enter Rank: ");
			int professorRank = scanner.nextInt();

			System.out.println("Enter Associated Depart Number: ");
			int departmentNumber = scanner.nextInt();

			// make query statements
			String sql = "insert into Professor (ssn, name, age, gender, research_specialty, professor_rank, dept_number) values (?, ?, ?, ?, ?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, ssn);
			pstmt.setString(2, name);
			pstmt.setInt(3, age);
			pstmt.setString(4, gender);
			pstmt.setString(5, researchSpecialty);
			pstmt.setInt(6, professorRank);
			pstmt.setInt(7, departmentNumber);
			pstmt.executeUpdate();

			System.out.println("You added a new Faculty!");
			System.out.println();
		} catch (Exception ex) {
			System.out.println("There was an error with connection");
			System.out.println(ex.getMessage());
		}
	}

	// method to remove a faculty (only remove faculty who is not a chairman of a
	// department or principal investigator for a project)
	public static void removeFaculty() {
		try {
			Scanner scanner = new Scanner(System.in);
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			String sql = "select * from professor";
			ResultSet rs = stmt.executeQuery(sql);
			// Store ssn to an arraylist to know what to delete
			ArrayList<Integer> list = new ArrayList<Integer>();
			int index = 1;
			System.out.println("These are the faculties: ");
			while (rs.next()) {
				list.add(rs.getInt(1));
				System.out.print("\n" + index + "\tssn: " + rs.getInt(1) + "\t" + "name: " + rs.getString(2) + "\t"
						+ "age: " + rs.getInt(3) + "\t" + "gender: " + rs.getString(4) + "\t" + "research specialty: "
						+ rs.getString(5) + "\t" + "professor rank: " + rs.getInt(6) + "\t" + "Department Number: "
						+ rs.getInt(7));
				index++;
			}
			System.out.println("\nEnter the number (1-" + (index - 1) + ") that you want to remove: ");
			int input = scanner.nextInt();
			while (input < 1 || input > index - 1) {
				// invalid input
				System.out.println("Entered invalid number. Please enter a valid number.");
				input = scanner.nextInt();
			}

			// remove from professor table
			sql = new String("DELETE FROM Professor where ssn = " + list.get(input - 1));
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("You removed ssn with " + list.get(input - 1));
			System.out.println();
		} catch (Exception ex) {
			System.out.println("There was an error with connection");
			System.out.println(ex.getMessage());
		}
	}

	// method to add a project
	public static void addNewProject() {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter Project Number: ");
			int projNumber = scanner.nextInt();
			System.out.println("Enter Budget: ");
			double budget = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter Starting Date (year-month-day): ");
			String starDate = scanner.nextLine();
			System.out.println("Enter Ending Date (year-month-day): ");
			String endDate = scanner.nextLine();
			System.out.println("Enter Sponsor Name: ");
			String sponsor = scanner.nextLine();

			
			Date starDateConversion = Date.valueOf(starDate);
			Date endDateConversion = Date.valueOf(endDate);
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);

			// make query statements
			String sql = "insert into project (project_number, budget, starting_date, ending_date, sponsor_name, principal_invest_ssn) values (?, ?, ?, ?, ?,null)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, projNumber);
			pstmt.setDouble(2, budget);
			pstmt.setDate(3, starDateConversion);
			pstmt.setDate(4, endDateConversion);
			pstmt.setString(5, sponsor);
			pstmt.executeUpdate();
			System.out.println("You added a new Project!");
			System.out.println();
		} catch (Exception ex) {
			System.out.println("There was an error when entering data");
			System.out.println(ex.getMessage());
		}
	}

	//method to remove project
	public static void removeProject() 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			String sql = "select * from project";
			ResultSet rs = stmt.executeQuery(sql);
			// Store project number to an arraylist to know what to delete
			ArrayList<Integer> list = new ArrayList<Integer>();
			int index = 1;
			System.out.println("These are the projects: ");
			while (rs.next()) {
				list.add(rs.getInt(1));
				System.out.print("\n" + index + "\tProject Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
						+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
						+ rs.getString(5) + "\t" + "Principal Investigator: " + rs.getInt(6));
				index++;
			}
			System.out.println("\nEnter the number (1-" + (index - 1) + ") that you want to remove: ");
			int input = scanner.nextInt();
			while (input < 1 || input > index - 1) {
				// invalid input
				System.out.println("Entered invalid number. Please enter a valid number.");
				input = scanner.nextInt();
			}

			// remove from project table
			sql = new String("DELETE FROM project where project_number = " + list.get(input - 1));
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("You removed Project Number " + list.get(input - 1));
			System.out.println();
		} catch(Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
	}
	
	//method to display co,pl, and students for each project
	public static void printProjectTeam() 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			System.out.println("These are the projects: ");
			String sql = "select * from project";
			ResultSet rs = stmt.executeQuery(sql);
			int index = 1;
			while (rs.next()) 
			{
				String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
						+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
						+ rs.getString(5) + "\t" + "Principal Investigator: " + rs.getInt(6);
				System.out.println(outPut);
				index++;
			}
			System.out.println("Enter the project number you want to get information: ");
			int selectedProject = scanner.nextInt();
			
			sql = "select p.ssn, p.name, m.project_number from professor p inner join mg_co_invest m on p.ssn = m.professor_ssn where m.project_number = " + selectedProject;
			rs = stmt.executeQuery(sql);
			ArrayList<String> coList = new ArrayList<>();
			while (rs.next()) 
			{
				coList.add(rs.getInt(1) + "," + rs.getString(2));
			}
		
			sql = "select * from mg_research_assistant m inner join graduate_student g on m.graduate_ssn = g.ssn where m.project_number =  " + selectedProject;
			rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				coList.add(rs.getInt(1) + "," + rs.getString(4) + "," + rs.getString(7));
			}
			
			
			sql = "select * from project p inner join professor pr on p.principal_invest_ssn = pr.ssn where project_number = " + selectedProject;
			rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
						+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
						+ rs.getString(5);
				System.out.println(outPut);
				System.out.println("Principal Investigator\t" + "SSN: " + rs.getString(6) + "\tName: " + rs.getString(8));
				for (int i = 0; i < coList.size(); i++) 
				{
					String[] arr = coList.get(i).split(",");
					if (arr.length == 2) 
					{
						System.out.println("CO-INVESTIGATOR\tSSN: " + arr[0] + "\tName: " + arr[1]);
					}
					else 
					{
						System.out.println("STUDENT\tSSN: " + arr[0] + "\tName: " + arr[1] + "\tDegree Program: " + arr[2]);
					}
				}
			}
			System.out.println();
		} catch(Exception ex) 
		{
			
		}
	}
	
	//method to display all students a professor supervises, and the project each student works on 
	public static void printStudentSupervisee() 
	{
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			String sql = "select p.ssn, p.name, pr.project_number from professor p inner join project pr on p.ssn = pr.principal_invest_ssn order by pr.project_number asc";
			ResultSet rs = stmt.executeQuery(sql);
			//store name of principal investigator, ssn, and project number
			ArrayList<String> principalList = new ArrayList<>();
			while (rs.next()) 
			{
				principalList.add(rs.getInt(1) + "," + rs.getString(2) + "," + rs.getInt(3));
			}
	
			//obtain the students working on any project
			sql = "select g.name, m.project_number from graduate_student g inner join mg_research_assistant m on g.ssn = m.graduate_ssn order by m.project_number asc";
			rs = stmt.executeQuery(sql);
			ArrayList<String> studentList = new ArrayList<String>();
			while(rs.next()) 
			{
				studentList.add(rs.getString(1) + ","  + rs.getInt(2));
			}
		
			boolean projectHaveStudents = false;
			for (int i = 0; i < principalList.size(); i++) 
			{
				String[] arr = principalList.get(i).split(",");
				System.out.println("Supervisor: " + arr[1] + " Supervises");
				for (int j = 0; j < studentList.size(); j++)
				{
					String[] arr2 = studentList.get(j).split(",");
					if (arr[2].equals(arr2[1]))
					{
						//print student with corresponding supervisor
						System.out.println("Student " + arr2[0] + " Works on project " + arr2[1]);
						projectHaveStudents = true;
					}
					
				}
				if (!projectHaveStudents)
				{
					System.out.println("No students for project " + arr[2]);
				}
				projectHaveStudents = false;
			}
			System.out.println();
		} catch(Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
	}
	
	//call fucntion female_faculty()
	public static void femaleFaculty() 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			Statement stmt = connection.createStatement();
			String sql = "select female_faculty() as output";
			ResultSet rs = stmt.executeQuery(sql);
			//store name of principal investigator, ssn, and project number
			System.out.println("The percentage of female faculty is: ");
			while (rs.next()) 
			{
				System.out.println(rs.getInt(1) + "%");
			}
		} catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	//call fucntion total_people()
		public static void totalPeople() 
		{
			try 
			{
				Scanner scanner = new Scanner(System.in);
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, userName, password);
				Statement stmt = connection.createStatement();
				String sql = "select * from project";
				ResultSet rs = stmt.executeQuery(sql);
				//store name of principal investigator, ssn, and project number
				System.out.println("These are the projects: ");
				while (rs.next()) 
				{
					String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
							+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
							+ rs.getString(5);
					System.out.println(outPut);
				}
				System.out.println("Enter the project number: ");
				int selectedProject = scanner.nextInt();
				sql = "select total_people(" + selectedProject + ") as output";
				rs = stmt.executeQuery(sql);
				System.out.println("The is the number of people that work on project: " + selectedProject);
				while(rs.next()) 
				{
					System.out.println(rs.getInt(1));
				}
			} catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		
		//assign faculties to a project that don't work on any projects to test trigger function faculty_restrict(), faculties that arent pl and cl
		public static void assignFacultyToProject()
		{
			try 
			{
				Scanner scanner = new Scanner(System.in);
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, userName, password);
				Statement stmt = connection.createStatement();
				String sql = "select * from project";
				ResultSet rs = stmt.executeQuery(sql);
				//store name of principal investigator, ssn, and project number
				System.out.println("Available Projects: ");
				while (rs.next()) 
				{
					String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
							+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
							+ rs.getString(5);
					System.out.println(outPut + "\n");
				}
				
				sql = "select p.ssn, p.name from professor p WHERE NOT EXISTS (SELECT * FROM project pr where pr.principal_invest_ssn = p.ssn) AND NOT EXISTS(SELECT * FROM mg_co_invest m where p.ssn = m.professor_ssn)";
				rs = stmt.executeQuery(sql);
				System.out.println("These are the faculties that do not work in any project: \n");
				while (rs.next()) 
				{
					String outPut = "SSN: " + rs.getInt(1) + "\tName: " + rs.getString(2);
					System.out.println(outPut + "\n");
				}				
				
				System.out.println("Assign a project to a faculty.");
				System.out.println("Enter Project Number: ");
				int projectNumber = scanner.nextInt();
				System.out.println("Enter SSN of faculty: ");
				int ssn = scanner.nextInt();
				
				sql = "INSERT INTO mg_co_invest values (" + ssn + "," + projectNumber + ")";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.executeUpdate();
				System.out.println("You assigned project number " + projectNumber + " to faculty " + ssn);
			} catch(Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
		}
		public static void assignStudentProject()
		{
			try 
			{
				Scanner scanner = new Scanner(System.in);
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, userName, password);
				Statement stmt = connection.createStatement();
				String sql = "select * from project";
				ResultSet rs = stmt.executeQuery(sql);
				//store name of principal investigator, ssn, and project number
				System.out.println("Available Projects: ");
				while (rs.next()) 
				{
					String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
							+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
							+ rs.getString(5);
					System.out.println(outPut + "\n");
				}
				
				sql = "select g.ssn, g.name, m.project_number from graduate_student g left join mg_research_assistant m on g.ssn = m.graduate_ssn order by m.project_number asc";
				rs = stmt.executeQuery(sql);
				System.out.println("All graduate students: \n");
				while (rs.next()) 
				{
					String outPut = "";
					int projectNumber = rs.getInt(3);
					if (projectNumber == 0) 
					{
						outPut = "SSN: " + rs.getInt(1) + "\tName: " + rs.getString(2) + "\t does not work on any project ";
					}
					else 
					{
						outPut = "SSN: " + rs.getInt(1) + "\tName: " + rs.getString(2) + "\tworks on project " + projectNumber;
					}
					System.out.println(outPut + "\n");
				}				
			
				System.out.println("Assign a project to a graduate student.");
				System.out.println("Enter Project Number: ");
				int projectNumber = scanner.nextInt();
				System.out.println("Enter SSN of graduate student ssn: ");
				int ssn = scanner.nextInt();				
				sql = "INSERT INTO mg_research_assistant values (" + ssn + "," + projectNumber + ")";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.executeUpdate();
				System.out.println("You assigned project number " + projectNumber + " to graduate student " + ssn);
			} catch(Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
		}
		public static void displayProfessors()
		{
			try {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, userName, password);
				Statement stmt = connection.createStatement();
				String sql = "select * from professor";
				ResultSet rs = stmt.executeQuery(sql);
				int index = 1;
				System.out.println("These are the faculties: ");
				while (rs.next()) {
					System.out.print("\n" + index + "\tssn: " + rs.getInt(1) + "\t" + "name: " + rs.getString(2) + "\t"
							+ "age: " + rs.getInt(3) + "\t" + "gender: " + rs.getString(4) + "\t" + "research specialty: "
							+ rs.getString(5) + "\t" + "professor rank: " + rs.getInt(6) + "\t" + "Department Number: "
							+ rs.getInt(7));
					index++;
				}
				System.out.println();
			} catch (Exception ex) {
				System.out.println("There was an error with connection");
				System.out.println(ex.getMessage());
			}
		}
		public static void displayProjects()
		{
			try 
			{
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, userName, password);
				Statement stmt = connection.createStatement();
				String sql = "select * from project";
				ResultSet rs = stmt.executeQuery(sql);
				//store name of principal investigator, ssn, and project number
				System.out.println("All Projects: ");
				while (rs.next()) 
				{
					String outPut = "Project Number: " + rs.getInt(1) + "\t" + "budget: " + rs.getDouble(2) + "\t"
							+ "Starting Date: " + rs.getDate(3) + "\t" + "Ending Date: " + rs.getDate(4) + "\t" + "Sponsor Name: "
							+ rs.getString(5);
					System.out.println(outPut + "\n");
				}
			} catch(Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
		}
}