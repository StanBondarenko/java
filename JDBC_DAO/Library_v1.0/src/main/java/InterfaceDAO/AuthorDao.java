package InterfaceDAO;

import ClassesDOJO.Author;

import java.util.List;

public interface AuthorDao {
    Author getAuthorById(int id);
    List<Author> getAllAuthors();
    Author createAuthor(Author blank);
    void addNewDataToAuthorBook(long authorId, long bookId);
}
