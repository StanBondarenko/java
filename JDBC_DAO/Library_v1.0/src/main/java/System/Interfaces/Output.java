package System.Interfaces;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import ClassesDOJO.Reader;

import java.util.List;

public interface Output {
    void print(String message);
    void printError(String message);
    void printLogo();
    int printMainMenu();
    int printBookMenu();
    int printBookInformationMenu();
    int printReaderMenu();
    void printBook(Book o);
    void printBook(List<Book> books);
    void printReader(Reader r);
    void printGenreName(List<Genre> genres);
    int printAuthorsFullName(List<Author> authors);
}
