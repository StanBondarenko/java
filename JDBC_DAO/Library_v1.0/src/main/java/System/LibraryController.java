package System;

import ClassesDOJO.Book;
import InterfaceDAO.BookDao;
import JDBCandDAO.JdbcBookDao;
import System.Interfaces.CheckChoice;
import System.Interfaces.Input;
import System.Interfaces.Output;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public class LibraryController {
    Output out = new SystemOutput();
    Input input = new SystemInput();
    CheckChoice check = new InputCheck();
    Connector connect = new Connector();
    BookDao jdbcBookDao = new JdbcBookDao(connect.getDataSource());

    public void mainMenuNavigation(){
        out.printLogo();
        boolean isMain = true;
        while (isMain) {
            boolean isSubMain= true;
            while (isSubMain) {
                String userNum = null;
                int numOfString = out.printMainMenu();
                userNum = input.promtChoice("Enter number>>>>");
                if (check.isCorrect(userNum, numOfString)) {
                    if(userNum.equals("5")){
                        isSubMain=false;
                        isMain=false;
                    }else {
                        isSubMain = navigationMain(userNum);

                    }
                } else {
                    out.printError("❌ ERROR: Invalid input!");
                }
            }

        }
    }

    private boolean navigationMain(String userInput){
        boolean isSomethingElse= true;
        switch (userInput){
            case "1" ->{
                while (isSomethingElse) {
                    int numOfString = out.printBookMenu();
                    String choice = input.promtChoice("Enter number>>>>");
                    if (check.isCorrect(choice, numOfString)) {
                        isSomethingElse=navigationBook(choice);
                    } else {
                        out.printError("❌ ERROR: Invalid input!");
                    }
                }
            }
            case "2"->{

            }
            case "3"->{

            }
            case  "4"->{

            }
            case "5"->{
                isSomethingElse=false;
            }
        }
        return isSomethingElse;
    }

    private boolean navigationBook(String inputUser) {
        boolean isSomethingElse =true;
        switch (inputUser){
            case "1"-> {
                List<Book> listBook = jdbcBookDao.getAllBooks();
                for (Book book: listBook){
                    out.printBook(book);
                }

            }
            case "2"->{
                String userTitle= input.promtChoice("Please, Enter title >>>>");
                List<Book> books= jdbcBookDao.getBookByTile(userTitle);
                if (books == null){
                    out.printError("You have entered an empty query!");
                } else  if (books.isEmpty()) {
                    out.printError("No books with this title found.");
                }else {
                    for (Book book: books) {
                        out.printBook(book);
                    }
                }
            }
            case "3"->{
                String firstName = input.promtChoice("Please enter the author's  first name>>>>");
                String lastName = input.promtChoice("Please enter the author's last name>>>>");
                List<Book> books=jdbcBookDao.getBookByAuthorFullName(firstName,lastName);
                if (books.isEmpty()) {
                    out.printError("No books with this title found.");
                }else {
                    for (Book book : books) {
                        out.printBook(book);
                    }
                }
            }
            case "4"->{

            }
            case "5"->{

            }
            case "6"->{

            }
            case "7"->{
                return false;
            }
        }
        return isSomethingElse;
    }

}
