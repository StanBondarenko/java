package System;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import ClassesDOJO.Reader;
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
    //
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
                out.printError("Invalid input.");
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
    public Book takeBookForUpdate(List<Book> books){
        Book bookForUpdate= null;
        boolean isNotDone= true;
        while (isNotDone) {
            out.printBook(books);
            String choice =input.promtChoice("Select the book you want to update>>>");
            if (check.isCorrectNavigation(choice,books.size())){
                int index = Integer.parseInt(choice)-1;
                bookForUpdate = books.get(index);
                isNotDone = false;

            }else {
                out.printError("Invalid input.");
                continue;
            }
        }
        return bookForUpdate;
    }
    public Book takeInfoForBookUpdate(Book book){
        Book blankBook= null;
        boolean isNotDone= true;
        while (isNotDone){
            int numRows=out.printBookInformationMenu();
            String choice = input.promtChoice("Select what you want to update in the book>>>");
            if (check.isCorrectNavigation(choice,numRows)){
                switch (choice){
                    case "1"->{
                        String title = input.promtChoice("Enter a new title>>>");
                        blankBook = new Book(book.getId(),title,book.getPublishDate(),book.getCountStock());
                        isNotDone = false;
                    }
                    case "2"->{
                        String pDate = input.promtChoice("Enter a new publication date(YYYY-DD-MM)>>>");
                        if (check.isCorrectDateFormat(pDate)){
                            LocalDate newPDate = LocalDate.parse(pDate);
                            blankBook = new Book(book.getId(),book.getTitle(),newPDate,book.getCountStock());
                            isNotDone = false;
                        }else {
                            continue;
                        }
                    }
                    case "3"->{
                        String count = input.promtChoice("Enter the new quantity of books in stock>>>");
                        if (check.isCorrectInt(count)){
                            int countStock= Integer.parseInt(count);
                            blankBook = new Book(book.getId(),book.getTitle(),book.getPublishDate(),countStock);
                            isNotDone = false;
                        }else {
                            out.printError("Invalid input.");
                            continue;
                        }
                    }
                }
            }else {
                out.printError("Invalid input.");
                continue;
            }
        }
        return blankBook;

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
            String answerFN = input.promtChoice("Enter a first name for the new author>>>");
            if (check.isWordsOnly(answerFN)){
                firstName = answerFN;
            }else {
                out.printError("Invalid input!");
                continue;
            }
            String answerLN = input.promtChoice("Enter a last name for the new author>>>");
            if (check.isWordsOnly(answerLN)){
                lastName = answerLN;
            }else {
                out.printError("Invalid input!");
                continue;
            }
           String answerB = input.promtChoice("Enter a date of birth for the new author (YYYY-MM-DD)>>>");
            if (check.isCorrectDateFormat(answerB)){
                birthday = LocalDate.parse(answerB);
            }else {
                continue;
            }
            if (!check.isYesOrNo("Rude question: is the author still alive?")){
              String  answerDD = input.promtChoice("Enter a date of deаth for the new author (YYYY-MM-DD)>>>");
                if (check.isCorrectDateFormat(answerDD)){
                    deathDay = LocalDate.parse(answerDD);
                    if (deathDay.isBefore(birthday)){
                        out.printError("The day of death cannot be earlier than the day of birth.");
                        continue;
                    }else {
                        newAuthor = new Author(firstName, lastName, birthday, deathDay);
                        isNotDone = false;
                    }
                }else {
                    continue;
                }
            }else {
                newAuthor= new Author(firstName,lastName,birthday,null);
                isNotDone = false;
            }
        }
        return newAuthor;
    }
    //Reader
    public Reader takeInfoForNewReader(){
        boolean isNotDone=true;
        String firstName ="";
        String lastName="";
        String address ="";
        String phoneNum="";
        String eMail="";
        while (isNotDone){
            firstName = input.promtChoice("Enter the reader's first name>>>");
            if (!check.isWordsOnly(firstName)){
                firstName="";
                continue;
            }
            lastName = input.promtChoice("Enter the reader's last name>>>");
            if (!check.isWordsOnly(lastName)){
                lastName="";
                continue;
            }
            address=input.promtChoice("Enter the reader's address>>>");
            if (!check.hasNotSpecialCharacters(address)){
                address="";
                continue;
            }
            phoneNum=input.promtChoice("Enter the reader's phone number>>>");
            if (!check.isCorrectPhoneNumber(phoneNum)){
                phoneNum="";
                continue;
            }
            eMail = input.promtChoice("Enter the reader's email address>>>");
            if (!check.isCorrectEmail(eMail)){
                eMail="";
                continue;
            }else {
                isNotDone=false;
            }
        }
        return new Reader(firstName,lastName,address,phoneNum,eMail);
    }
}
