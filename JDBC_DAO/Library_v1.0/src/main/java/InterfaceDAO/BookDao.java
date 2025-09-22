package InterfaceDAO;

import ClassesDOJO.Book;

import java.util.List;

public interface BookDao {
    Book getBookById(int id);
    List<Book> getAllBooks();
    List<Book> getBookByTile(String title);
    List<Book> getBookByAuthorFullName (String firstName, String lastName);
    Book createBook(Book blank);
    void createBookCopy(int valueCopy, long bookId);

}
