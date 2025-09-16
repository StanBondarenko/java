package System.Interfaces;

import ClassesDOJO.Book;

public interface Output {
    void print(String message);
    void printError(String message);
    void printLogo();
    int printMainMenu();
    int printBookMenu();
    void printBook(Book o);
}
