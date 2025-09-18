package System;

import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import System.Interfaces.Output;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import static java.lang.System.in;

public class SystemOutput implements Output {
    @Override
    public void print(String massage){
        System.out.println(massage);
    }
    @Override
    public void printError(String message){
        System.err.println("❌ ERROR:"+message);
    }
    @Override
    public void printBook(Book o) {
        System.out.println(o.toString());
    }
    @Override
    public void printBook(List<Book> books){
        if (books == null){
            printError("You have entered an empty query!");
        } else  if (books.isEmpty()) {
            printError("No books found.");
        }else {
            for (Book book: books) {
                printBook(book);
            }
        }
    }
    @Override
    public void printGenreName(List<Genre> genres){
        for(int i=0; i<genres.size();i++){
            print(i+1+". "+ genres.get(i).getGenreName());
        }

    }
    @Override
    public void printLogo(){
        System.out.println("""
                        .....                    .....         \s
                           ..:.                .:..            \s
                              ::              :.               \s
                               .-.          :-                 \s
                                .-.   ..   ::                  \s
                                  :::=++-::.       ..          \s
                                  .:=**+-::-.     ..           \s
                                 .:--++:-.:=.  .:..            \s
                        ..::.-:  .=-=*=-+==:  :-.              \s
                 .......      .-:  :-:::=--.  --               \s
                               -=..:. :::-: --  .:.            \s
                          .:-.  ::..:..-=::.. .-=:.-:          \s
                         :: .-=:   :-::-=:.  :=:.   --         \s
                        :-    .::..::::::.:....      --        \s
                       :-   .......::...::...:----:  ..:.      \s
                     .:.  .::::-:.....:-:::-:.    :-    ::.    \s
                    .:.   -:      .::-+*+:.:==.   :=     ...:. \s
                 .::.     =.     .:.-====-...:=:  .=:          \s
                ..       :-      .::=+***=:.:..:  .-=          \s
                         :.      .:.-=++=-::- .-   .:.         \s
                        :.        .::=++=:.:. :=    .-         \s
                       ::          .:-==-::.  :=     ::        \s
                     .:.             .:::.     -.     .:       \s
                   ...                         ::      .:..    \s
                   .                            ::       ..    \s
                                                 ::.           \s
                                                  .:           \s""");
        System.out.println("\uD83D\uDCDA ✏️ \uD83D\uDCBB Welcome to the ant library \uD83D\uDCDA ✏️ \uD83D\uDCBB");
    }
    //****************** MENU
    @Override
    public int printMainMenu(){
        int numOfString =5;
        System.out.println("""
                1. Book navigation menu.
                2. Reader navigation menu.
                3. Author navigation menu.
                4. Service menu.
                5. Exit.""");
        return numOfString;

    }
    @Override
    public int printBookMenu(){
        int numOfString = 7;
        String menu= """
                1. Show the list of books.
                2. Search for a book by title.
                3. Search for a book by author.
                4. ADD a new book.
                5. UPDATE a book.
                6. DELETE a book.
                7. Return to the main menu.""";
        System.out.println(menu);
        return numOfString;
    }
}
