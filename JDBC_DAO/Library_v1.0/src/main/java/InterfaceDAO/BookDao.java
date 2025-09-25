package InterfaceDAO;

import ClassesDOJO.Book;

import java.util.List;

public interface BookDao {
    Book getBookById(int id);
    List<Book> getAllBooks();
    List<Book> getBookByTile(String title, boolean isFull);
    List<Book> getBookByAuthorFullName (String firstName, String lastName);
    Book createBook(Book blank);
    void createBookCopy(Book blankBook);
    void updateBook (Book blankBook);
    void deleteBook(Book bookForDelete);

}
