package System.Interfaces;

import ClassesDOJO.Book;
import ClassesDOJO.Genre;

import java.util.List;

public interface Output {
    void print(String message);
    void printError(String message);
    void printLogo();
    int printMainMenu();
    int printBookMenu();
    void printBook(Book o);
    void printBook(List<Book> books);
    void printGenreName(List<Genre> genres);
}
