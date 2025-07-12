import java.math.BigDecimal;
import java.math.RoundingMode;

public class Employee {
    final private static int SALARY_RATE = 60000;
    private long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private static BigDecimal salary = BigDecimal.valueOf(SALARY_RATE);
    Department department;
    private String hireDate;


    //*************************Getters
    public long getEmployeeId(){
        return this.employeeId;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public BigDecimal getSalary(){
        return this.salary;
    }
    public Department getDepartment(){
        return this.department;
    }
    public String getHireDate(){
        return this.hireDate;
    }
    //**********************************Setters
    public void setEmployeeId(long employeeId){
        this.employeeId=employeeId;
    }
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    public  void  setLastName(String lastName){
        this.lastName=lastName;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setSalary(BigDecimal salary){
        this.salary=salary;
    }
    public void setDepartment(int departmentId, String name){
        this.department = new Department(departmentId,name);
    }
    public  void setHireDate(String hireDate){
        this.hireDate=hireDate;
    }
    public Employee(long employeeId,String firstName, String lastName, String email,Department department, String hireDate){
        this.employeeId=employeeId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.department= department;
        this.hireDate=hireDate;
    }
    public Employee (){
    }

    public String getFullName(){
        return this.lastName+", "+this.firstName;
    }
    public BigDecimal raiseSalary(double percent){
        return salary = salary.add(salary.multiply(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100))));
    }


}

