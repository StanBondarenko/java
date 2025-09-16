package InterfaceDAO;

import ClassesDOJO.Book;

import java.util.List;

public interface BookDao {
    List<Book> getAllBooks();
    List<Book> getBookByTile(String title);
    List<Book> getBookByAuthorFullName (String firstName, String lastName);
}
