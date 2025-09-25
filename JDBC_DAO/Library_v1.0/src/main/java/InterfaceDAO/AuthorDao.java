package InterfaceDAO;

import ClassesDOJO.Author;

import java.util.List;

public interface AuthorDao {
    Author getAuthorById(int id);
    List<Author> getAllAuthors();
    Author createAuthor(Author blank);
    void addNewDataToAuthorBook(long authorId, long bookId);
    List<Author> getAuthorByFirstName(String firstName, boolean isFull);
    List<Author> getAuthorByLastName(String lastName, boolean isFull);
    List<Author> getAuthorByBookTitle(String title, boolean isFull);
    void deleteAuthor(Author author);
    void updateAuthor(Author author);


}
