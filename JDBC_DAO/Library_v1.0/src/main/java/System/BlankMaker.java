package System;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import System.Interfaces.CheckChoice;
import System.Interfaces.Input;
import System.Interfaces.Output;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BlankMaker {
    private final Input input= new SystemInput();
    private final CheckChoice check = new InputCheck();
    private final Output out = new SystemOutput();
    public BlankMaker(){};



    //************************************** Methods for information
    public Book takeInfoForNewBook(){
        boolean isCorrect=true;
        String title="";
        LocalDate publishDate=null;
        int countStoak=1;
        while (isCorrect) {
            title = input.promtChoice("Please enter title>>>>");
            String date = input.promtChoice("Please enter publish date (YYYY-MM-DD)>>>>");
            if (check.isCorrectDateFormat(date)){
                publishDate= LocalDate.parse(date);
            }else {
                continue;
            }
            String count = input.promtChoice("Enter the number of books in stock>>>>");
            if (check.isCorrectInt(count)){
                countStoak = Integer.parseInt(count);
            }else {
                continue;
            }
            isCorrect=false;
        }
        return new Book(title,publishDate,countStoak);
    }
    public List<Genre> takeInfoForGenre(List<Genre> genres){
        List<Genre> genresNew = new ArrayList<>();
        boolean isNotDone = true;
        int maxNum = genres.size();
        while (isNotDone) {
            String choice="";
            out.printGenreName(genres);
            choice =input.promtChoice("Choose the genre of your new book >>>>");
            if (check.isCorrectNavigation(choice,maxNum)){
                int index = Integer.parseInt(choice)-1;
                genresNew.add(genres.get(index));
                if (check.isYesOrNo("Want to add another genre?")){
                    continue;
                }else {
                    isNotDone=false;

                }
            }else {
                out.printError("Invalid input!");
                continue;
            }
        }
        return genresNew;
    }
    public Author takeAuthorInfoForNewBook(List<Author> authors){
        boolean isNotDone= true;
        int total= 0;
        while (isNotDone) {
            total = out.printAuthorsFullName(authors);
            String choice = input.promtChoice("Select an author for a new book or type NEW to add a new author>>> ");
            int answer = check.checkIfAuthorIsNew(choice,total);

                switch (answer){
                    case 0->{
                        continue;
                    }
                    case 1->{
                        int index = Integer.parseInt(choice)-1;
                        return authors.get(index);
                    }
                    case 3->{
                        return takeInfoForNewAuthor();
                    }
            }
        }
        return new Author();
    }
    // Author
    private Author takeInfoForNewAuthor(){
        boolean isNotDone=true;
        String firstName= null;
        String lastName= null;
        LocalDate birthday= null;
        LocalDate deathDay= null;
        Author newAuthor = null;
        while (isNotDone) {
            String answer = input.promtChoice("Enter a first name for the new author>>>");
            if (check.isWordsOnly(answer)){
                firstName = answer;
            }else {
                out.printError("Invalid input!");
                continue;
            }
            answer = input.promtChoice("Enter a last name for the new author (YYYY-MM-DD)>>>");
            if (check.isWordsOnly(answer)){
                lastName = answer;
            }else {
                out.printError("Invalid input!");
                continue;
            }
            answer = input.promtChoice("Enter a date of birth for the new author (YYYY-MM-DD)>>>");
            if (check.isCorrectDateFormat(answer)){
                birthday = LocalDate.parse(answer);
            }else {
                out.printError("Invalid input!");
                continue;
            }
            if (check.isYesOrNo("Rude question: is the author still alive?")){
                answer = input.promtChoice("Enter a date of birth for the new author (YYYY-MM-DD)>>>");
                if (check.isCorrectDateFormat(answer)){
                    deathDay = LocalDate.parse(answer);
                    newAuthor= new Author(firstName,lastName,birthday,deathDay);
                }
            }else {
                newAuthor= new Author(firstName,lastName,birthday,null);
            }
        }
        return newAuthor;
    }


}
