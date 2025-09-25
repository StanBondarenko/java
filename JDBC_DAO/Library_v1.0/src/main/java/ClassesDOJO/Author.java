package ClassesDOJO;

import java.time.LocalDate;
import java.util.Objects;

public class Author {
    private long id;
    private String authorFirstName;
    private String authorLastName;
    private LocalDate birthday;
    private LocalDate deathDate;

    public Author(){

    }

    public Author(String authorFirstName, String authorLastName, LocalDate birthday, LocalDate deathDate) {
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.birthday = birthday;
        this.deathDate = deathDate;
    }

    public Author(long id, String authorFirstName, String authorLastName, LocalDate birthday, LocalDate deathDate) {
        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.birthday = birthday;
        this.deathDate = deathDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }
    @Override
    public String toString(){
        return "ID-->  "+this.id+
                " First Name--> "+this.authorFirstName+
                " Last Name--> "+this.authorLastName+
                " Birthday-->"+this.birthday+
                " Death Day--> "+this.deathDate;
    }
    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id
                && Objects.equals(authorFirstName, author.authorFirstName)
                && Objects.equals(authorLastName, author.authorLastName)
                && Objects.equals(birthday, author.birthday)
                && Objects.equals(deathDate, author.deathDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,authorFirstName,authorLastName,birthday,deathDate);
    }
}
