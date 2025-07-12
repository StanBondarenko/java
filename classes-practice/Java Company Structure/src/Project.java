import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private String startDate;
    private String dueDate;
    private List<Employee> teamMembers;
    //*******************************************Getters
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getStartDate(){
        return this.startDate;
    }
    public List<Employee> getTeamMembers(){
        return this.teamMembers;
    }
    //*********************************************Setters
    public void setName(String name){
        this.name=name;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void  setStartDate(String startDate){
        this.startDate=startDate;
    }
    public void  setDueDate(String dueDate){
        this.startDate=startDate;
    }
    public void  setTeamMembers(List<Employee> teamMembers){
        this.teamMembers=teamMembers;
    }

    public  Project (String name, String description,String startDate, String dueDate){
        this.name=name;
        this.description=description;
        this.startDate=startDate;
        this.dueDate=dueDate;
    }


}

