package System;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import ClassesDOJO.Reader;
import InterfaceDAO.AuthorDao;
import InterfaceDAO.BookDao;
import InterfaceDAO.GenreDao;
import InterfaceDAO.ReaderDao;
import JdbcAndDAO.JdbcAuthorDao;
import JdbcAndDAO.JdbcBookDao;
import JdbcAndDAO.JdbcGenreDao;
import JdbcAndDAO.JdbcReaderDao;
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
    ReaderDao jdbcReaderDao = new JdbcReaderDao(connect.getDataSource());

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
                    out.printError("Invalid input.");
                    continue;
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
                while (isSomethingElse) {
                    int numOfString = out.printReaderMenu();
                    String choice = input.promtChoice("Enter number>>>>");
                    if (check.isCorrectNavigation(choice, numOfString)) {
                        isSomethingElse=navigationReader(choice);
                    } else {
                        out.printError("Invalid input!");
                    }
                }
            }
            case "3"->{
                while (isSomethingElse){
                    int numOfString = out.printAuthorMenu();
                    String choice = input.promtChoice("Enter number>>>>");
                    if (check.isCorrectNavigation(choice, numOfString)) {
                        isSomethingElse=navigationAuthor(choice);
                    } else {
                        out.printError("Invalid input!");
                    }

                }
            }
            case  "4"->{
            out.printError("It will be implemented only when I figure out how to work with transactions. Stay tuned for version 2.0.");
            input.pause();
            return false;
            }
            case "5"->{
                return false;
            }
        }
        return isSomethingElse;
    }
    private boolean navigationBook(String inputUser) {
        boolean isSomethingElse =true;
        switch (inputUser){
            case "1"->{
                List<Book> listBook = jdbcBookDao.getAllBooks();
                for (Book book: listBook){
                    out.printBook(book);
                }
            }
            case "2"->{
                boolean isFull= true;
                String title = input.promtChoice("Enter the book's title>>>");
                if(check.isWordsOnly(title)){
                    if (!check.isYesOrNo("Strict search for the entered phrase?")){
                        isFull = false;
                    }
                    List<Book> books = jdbcBookDao.getBookByTile(title,isFull);
                    if(books==null){
                        out.printError("No book with title \""+title+"\" was found.");
                        input.pause();
                    }else {
                        out.printBook(books);
                        input.pause();
                    }
                }
            }
            case "3"->{
                String firstName = input.promtChoice("Please enter the author's  first name>>>>");
                String lastName = input.promtChoice("Please enter the author's last name>>>>");
                out.printBook(jdbcBookDao.getBookByAuthorFullName(firstName,lastName));
            }
            case "4"->{
                // ADD book
                Book newBook =jdbcBookDao.createBook(blank.takeInfoForNewBook());
                out.print("A new book has been created:");
                out.printBook(newBook);
                input.pause();
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
                    out.print("A new author has been created:");
                    out.print(newAuthor.toString());
                    input.pause();
                }
                // add to author_book
                jdbcAuthorDao.addNewDataToAuthorBook(newAuthor.getId(),newBook.getId());
                // add to copy_book
                jdbcBookDao.createBookCopy(newBook);
            }
            case "5"->{ // Update
                List<Book> listBook = jdbcBookDao.getAllBooks();
                Book blankForUpdate=blank.takeInfoForBookUpdate(blank.takeBookForUpdate(listBook));
                jdbcBookDao.updateBook(blankForUpdate);
            }
            case "6"->{
                // DELETE
                List<Book> listBook = jdbcBookDao.getAllBooks();
                boolean isNeedMoreInfo = true;
                while (isNeedMoreInfo) {
                    out.printBook(listBook);
                    String num=input.promtChoice("Select a book to delete>>>");
                    if (check.isCorrectNavigation(num,listBook.size())){
                       int index = Integer.parseInt(num)-1;
                        Book bookForDelete = listBook.get(index);
                        jdbcBookDao.deleteBook(bookForDelete);
                        isNeedMoreInfo = false;
                    }else {
                        out.printError("Invalid input");
                        continue;
                    }
                }
            }
            case "7"->{
                return false;
            }
        }
        return isSomethingElse;
    }
    private boolean navigationReader(String inputUser){
        boolean isSomethingElse =true;
        switch (inputUser){
            case "1"->{
                String idUser = input.promtChoice("Enter reader ID>>>>");
                if (check.isCorrectInt(idUser)){
                    int id = Integer.parseInt(idUser);
                    Reader reader = jdbcReaderDao.getReaderById(id);
                    if (reader==null
                    ){
                        out.printError("No reader with this ID found.");
                        input.pause();
                    }else {
                        out.printReader(reader);
                        input.pause();
                    }
                }else {
                    out.printError("Invalid input!");
                }
            }
            case "2"->{
                Reader blankReader = blank.takeInfoForNewReader();
                Reader newReader = jdbcReaderDao.createNewReader(blankReader);
                out.print("A new reader has been created:");
                out.printReader(newReader);
                input.pause();
            }
            case "3"->{
                List<Reader> allReader = jdbcReaderDao.getAllReaders();
                Reader newReader = blank.takeReaderForUpdate(allReader);
                newReader=blank.takeInfoForUpdateReader(newReader);
                jdbcReaderDao.updateReader(newReader);
                out.print("Reader has been updated:");
                out.printReader(newReader);
                input.pause();
            }
            case "4"->{
                List<Reader> allReader = jdbcReaderDao.getAllReaders();
                Reader readerForDelete = blank.takeReaderForUpdate(allReader);
                out.print("Do you want to delete reader:");
                out.printReader(readerForDelete);
                if (check.isYesOrNo("Choose yes or no")){
                    jdbcReaderDao.deleteReader(readerForDelete);
                    out.print("The reader was removed.");
                    input.pause();
                }
            }
            case "5"->{
                return false;
            }
        }
        return  isSomethingElse;
    }
    private boolean navigationAuthor(String inputUser){
        boolean isSomethingElse =true;
        switch (inputUser){
            case "1"->{
                out.printAllAuthors(jdbcAuthorDao.getAllAuthors());
                input.pause();
            }
            case "2"->{
                boolean isFull= true;
                String firstName = input.promtChoice("Enter the author's first name>>>");
                if(check.isWordsOnly(firstName)){
                    if (!check.isYesOrNo("Strict search for the entered phrase?")){
                        isFull = false;
                    }
                    List<Author> authors = jdbcAuthorDao.getAuthorByFirstName(firstName,isFull);
                    if(authors==null){
                        out.printError("No author named \""+firstName+"\" was found.");
                        input.pause();
                    }else {
                        out.printAllAuthors(authors);
                        input.pause();
                    }
                }
            }
            case "3"->{
                boolean isFull= true;
                String lastName = input.promtChoice("Enter the author's last name>>>");
                if(check.isWordsOnly(lastName)){
                    if (!check.isYesOrNo("Strict search for the entered phrase?")){
                        isFull = false;
                    }
                    List<Author> authors = jdbcAuthorDao.getAuthorByLastName(lastName,isFull);
                    if(authors==null){
                        out.printError("No author with last name \""+lastName+"\" was found.");
                        input.pause();
                    }else {
                        out.printAllAuthors(authors);
                        input.pause();
                    }
                }
            }
            case "4"->{
                boolean isFull= true;
                String title = input.promtChoice("Enter the book's title>>>");
                if(check.isWordsOnly(title)){
                    if (!check.isYesOrNo("Strict search for the entered phrase?")){
                        isFull = false;
                    }
                    List<Author> authors = jdbcAuthorDao.getAuthorByBookTitle(title,isFull);
                    if(authors==null){
                        out.printError("No book with title \""+title+"\" was found.");
                        input.pause();
                    }else {
                        out.printAllAuthors(authors);
                        input.pause();
                    }
                }
            }
            case "5"->{
                Author newAuthor = blank.takeInfoForNewAuthor();
                newAuthor=jdbcAuthorDao.createAuthor(newAuthor);
                out.print("A new author was created:");
                out.print(newAuthor.toString());
                input.pause();
            }
            case "6"->{
                List<Author> authors = jdbcAuthorDao.getAllAuthors();
                Author author = blank.takeAuthorForUpdate(authors);
                author= blank.takeInfoForAuthorUpdate(author);
                jdbcAuthorDao.updateAuthor(author);
                out.print("The author has been updated:");
                out.print(author.toString());
                input.pause();
            }
            case "7"->{
                List<Author> authors = jdbcAuthorDao.getAllAuthors();
                Author author = blank.takeAuthorForUpdate(authors);
                if (check.isYesOrNo("Are you sure you want to delete this author?")){
                 jdbcAuthorDao.deleteAuthor(author);
                 out.print("The author was removed.");
                 input.pause();
                }
            }
            case "8"->{return false;}
        }
        return  isSomethingElse;
    }

    // delete same genre
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
