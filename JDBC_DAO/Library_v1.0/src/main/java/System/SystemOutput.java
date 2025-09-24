package System;

import ClassesDOJO.Author;
import ClassesDOJO.Book;
import ClassesDOJO.Genre;
import ClassesDOJO.Reader;
import System.Interfaces.Output;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import static java.lang.System.in;

public class SystemOutput implements Output {
    //System methods
    @Override
    public void print(String massage){
        System.out.println(massage);
    }
    @Override
    public void printError(String message){
        System.err.println("❌ ERROR:"+message);
    }
    // Methods print for take info from user
    @Override
    public void printBook(Book o) {
        System.out.println(o.toString());
    }

    @Override
    public void printBook(List<Book> books){
        int nRow = 1;
        if (books == null){
            printError("You have entered an empty query!");
        } else  if (books.isEmpty()) {
            printError("No books found.");
        }else {
            for (Book book : books) {
                System.out.println(nRow + ". " + book.toString());
                nRow++;
            }
        }
    }

    @Override
    public void printReader(Reader r) {
        System.out.println(r.toString());
    }
    @Override
    public void printGenreName(List<Genre> genres){
        for(int i=0; i<genres.size();i++){
            print(i+1+". "+ genres.get(i).getGenreName());
        }

    }
    @Override
    public int printAuthorsFullName(List<Author> authors) {
        int count = 0;
        for (Author a : authors){
            System.out.println(++count+": "+a.getAuthorFirstName()+" "+a.getAuthorLastName());
        }
        return authors.size();
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
    @Override
    public int printBookInformationMenu() {
        String menu = """
                1. Title.
                2. Publish date.
                3. Count in stock.""";
        System.out.println(menu);
        return 3;
    }

    @Override
    public int printReaderMenu() {
        String menu= """
                1. Search by ID.
                2. ADD new a reader.
                3. UPDATE a reader.
                4. DELETE a reader.
                5. Return to the main menu.""";
        System.out.println(menu);
        return 5;
    }

}
