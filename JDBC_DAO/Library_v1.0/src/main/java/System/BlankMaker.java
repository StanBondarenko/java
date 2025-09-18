package System;

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


    private Book createBookBlank(String title, LocalDate publishDate, int countStoak ){
        return new Book(title,publishDate,countStoak);
    }
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
        return createBookBlank(title,publishDate,countStoak);
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
                    out.printError("Invalid input!");
                }
            }else {
                continue;
            }
        }
        return genresNew;
    }
}
