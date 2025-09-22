package System;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import InterfaceDAO.AuthorDao;
import InterfaceDAO.BookDao;
import InterfaceDAO.GenreDao;
import JdbcAndDAO.JdbcAuthorDao;
import JdbcAndDAO.JdbcBookDao;
import JdbcAndDAO.JdbcGenreDao;
import System.Interfaces.CheckChoice;
import System.Interfaces.Input;
import System.Interfaces.Output;

import java.util.List;
import java.util.ListIterator;

public class LibraryController {
    Output out = new SystemOutput();
    Input input = new SystemInput();
    CheckChoice check = new InputCheck();
    Connector connect = new Connector();
    BookDao jdbcBookDao = new JdbcBookDao(connect.getDataSource());
    GenreDao jdbcGenreDao = new JdbcGenreDao(connect.getDataSource());
    AuthorDao jdbcAuthorDao = new JdbcAuthorDao(connect.getDataSource());
    BlankMaker blank = new BlankMaker();

    public void mainMenuNavigation(){
        out.printLogo();
        boolean isMain = true;
        while (isMain) {
            boolean isSubMain= true;
            while (isSubMain) {
                String userNum = null;
                int numOfString = out.printMainMenu();
                userNum = input.promtChoice("Enter number>>>>");
                if (check.isCorrectNavigation(userNum, numOfString)) {
                    if(userNum.equals("5")){
                        isSubMain=false;
                        isMain=false;
                    }else {
                        isSubMain = navigationMain(userNum);
                    }
                } else {
                    out.printError("Invalid input!");
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
                    if (check.isCorrectNavigation(choice, numOfString)) {
                        isSomethingElse=navigationBook(choice);
                    } else {
                        out.printError("Invalid input!");
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
                out.printBook(jdbcBookDao.getBookByTile(userTitle));
            }
            case "3"->{
                String firstName = input.promtChoice("Please enter the author's  first name>>>>");
                String lastName = input.promtChoice("Please enter the author's last name>>>>");
                out.printBook(jdbcBookDao.getBookByAuthorFullName(firstName,lastName));
            }
            case "4"->{
                // ADD book
                Book newBook =jdbcBookDao.createBook(blank.takeInfoForNewBook());
                // List of genre
                List<Genre> genres=blank.takeInfoForGenre(jdbcGenreDao.getAllGenre());
                // Sort List of genre
                deleteGenreIfEqual(genres);
                // Add genres from list to the DB
                for (Genre g : genres){
                    jdbcGenreDao.addGenreToGenreBook(newBook.getId(),g.getId());
                }
                // Add new author for new book
                Author newAuthor = blank.takeAuthorInfoForNewBook(jdbcAuthorDao.getAllAuthors());
                if (newAuthor.getId()==0){
                    newAuthor= jdbcAuthorDao.createAuthor(newAuthor);
                }
                // add to author_book
                jdbcAuthorDao.addNewDataToAuthorBook(newAuthor.getId(),newBook.getId());
                // add to copy_book
                jdbcBookDao.createBookCopy(newBook.getCountStock(),newBook.getId());
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
    private void deleteGenreIfEqual(List<Genre> listGenre){
       for (int i=0; i<listGenre.size();i++){
           Genre genre = listGenre.get(i);
           ListIterator<Genre> iter = listGenre.listIterator(i+1);
           while (iter.hasNext()){
               if(genre.equals(iter.next())){
                   iter.remove();
               }
           }
       }
    }
}
