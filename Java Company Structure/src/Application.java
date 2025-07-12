import java.math.BigDecimal;
import java.util.*;

public class Application {
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    private void run() {
        // create some departments
        createDepartments();
        // print each department by name
        printDepartments();

        // create employees
        createEmployees();

        // give Angie a 10% raise, she is doing a great job!

        // print all employees
        printEmployees();

        // create the TEams project
        createTeamsProject();
        // create the Marketing Landing Page Project
        createLandingPageProject();

        // print each project name and the total number of employees on the project
        printProjectsReport();
    }
    public List<Department> departments= new ArrayList<>();
    final int MARETING=0;
    final int SALES=1;
    final int ENGINEERING=2;
    public void createDepartments() {
        Department d1 = new Department(1, "Marketing");
        Department d2 = new Department(2, "Sales");
        Department d3 = new Department(3, "Engineering");

        departments.add(d1);
        departments.add(d2);
        departments.add(d3);
    }
    private void printDepartments() {
        System.out.println("------------- DEPARTMENTS ------------------------------");
        for (int i =0; i<departments.size(); i++){
            System.out.println(departments.get(i).getName());
        }
    }
    List <Employee> employees = new ArrayList<>();
    private void createEmployees() {
        Employee e1 = new Employee();
        e1.setEmployeeId(1);
        e1.setFirstName("Dean");
        e1.setLastName("Johnson");
        e1.setEmail("djohnson@teams.com");
        e1.setSalary(BigDecimal.valueOf(60000));
        e1.setDepartment(3,"Engineering");
        e1.setHireDate("2023-04-17");
        Employee e2= new Employee(2,"Angie","Smith","asmith@teams.com",departments.get(ENGINEERING),"2020-08-24");
        Employee e3= new Employee(3,"Margaret","Thompson","mthompson@teams.com",departments.get(MARETING),"2024-06-03");
        e2.raiseSalary(10);
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

    }
    private void printEmployees() {
        System.out.println("\n------------- EMPLOYEES ------------------------------");
        for (int i=0; i< employees.size(); i++) {
            System.out.println(employees.get(i).getFullName()+" "+employees.get(i).getSalary()+" "+ employees.get(i).department.getName());
        }
    }

    Map<String,Project> projects = new HashMap<>();

    private void createTeamsProject() {
        Project p1 = new Project("TEams", "Project Management Software","2024-11-04","2024-12-16");
        List <Employee> team = new ArrayList<>();
        for (Employee employee: employees){

            if (employee.getDepartment().getName().equals("Engineering")){
                team.add(employee);
            }
        }
        p1.setTeamMembers(team);
        projects.put(p1.getName(),p1);
    }
    private void createLandingPageProject() {
        Project p2 = new Project("Marketing Landing Page","Lead Capture Landing Page for Marketing","2025-02-03","2025-02-24");
        List<Employee> team = new ArrayList<>();
        for (Employee employee: employees){
            if (employee.getDepartment().getName().equals("Marketing")){
                team.add(employee);
            }
        }
        p2.setTeamMembers(team);
        projects.put(p2.getName(),p2);
    }
    private void printProjectsReport() {
        System.out.println("\n------------- PROJECTS ------------------------------");
        for (Map.Entry<String, Project> entry : projects.entrySet()) {
            String projectName = entry.getKey();
            Project project = entry.getValue();
            int teamSize = project.getTeamMembers().size();
            System.out.println(projectName + ": " + teamSize);
        }
    }

}