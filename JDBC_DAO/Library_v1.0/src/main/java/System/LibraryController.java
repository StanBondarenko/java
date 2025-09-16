package System;

import ClassesDOJO.Book;
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
    JdbcBookDao jdbcBookDao = new JdbcBookDao(connect.getDataSource());

    public void mainMenuNavigation(){
        out.printLogo();
        boolean isMain = true;
        while (isMain) {
                String userNum = null;
                int numOfString=out.printMainMenu();
                userNum = input.promtChoice("Enter number>>>>");
                if (check.isCorrect(userNum, numOfString)) {
                    isMain= navigationMain(userNum);
                } else {
                    out.printError("❌ ERROR: Invalid input!");
                }

        }
    }

    private boolean navigationMain(String userInput){
        boolean isSomethingElse= true;
        switch (userInput){
            case "1" ->{
                int numOfString = out.printBookMenu();
                String choice = input.promtChoice("Enter number>>>>");
                if(check.isCorrect(choice,numOfString)){
                    navigationBook(choice);
                }else {
                    out.printError("❌ ERROR: Invalid input!");
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

    private boolean navigationBook(String input) {
        boolean isSomethingElse =true;
        switch (input){
            case"1"-> {
                List<Book> listBook = jdbcBookDao.allBooks();
                for (Book book: listBook){
                    out.printBook(book);
                }

            }
            case "2"->{

            }
            case "3"->{

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
