

import ClassesDOJO.Book;
import JdbcAndDAO.JdbcBookDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JdbcBookDaoTest extends BaseDaoTest {
    private JdbcBookDao dao;
    private Book bookId1;
    @BeforeEach
    public void setup(){
        dao= new JdbcBookDao(dataSource);
        bookId1= new Book(1,"TestBook1", LocalDate.of(2001,1,1),2);
    }
    @Test
    public void getBookById_test_invalid_id(){
        Book test = dao.getBookById(100);
        assertNull(test,"Should be null");
    }

    @Test
    public void getBookById_test_valid_id(){
        Book test = dao.getBookById(1);
        assertBookMatch(bookId1,test);
    }
    @Test
    public void getAllBooks_test(){
        List<Book> listBooks = dao.getAllBooks();
        assertEquals(4,listBooks.size(),"Size should match");
        assertBookMatch(bookId1,listBooks.get(0));
    }
    @Test
    public void getBookByTitle_test_invalid_title(){
        List<Book> nullList= dao.getBookByTile("I LOVE YOUR MOM",true);
        assertNull(nullList,"Should be null");
    }
    @Test
    public void getBookByTitle_test_valid_title(){
        List<Book> books = dao.getBookByTile("test",false);
        assertEquals(4,books.size(),"Size should match.");
        assertBookMatch(bookId1,books.get(0));
    }
    @Test
    public void getBookByAuthorFullName_test(){
      List<Book> books = dao.getBookByAuthorFullName("test","test");
      assertEquals(4,books.size(),"Size not match");
      assertBookMatch(bookId1,books.get(0));
    }
    @Test
    public void createBook_test(){
        Book blank = new Book("NewTitle",LocalDate.of(2005,12,11),2);
        Book newBook = dao.createBook(blank);
        Book bookForTest = dao.getBookById(5);
        assertBookMatch(bookForTest,newBook);
    }
    @Test
    public void updateBook_test(){
        Book bookUpdate = new Book(1,"Updated", LocalDate.of(2001,1,1),2);
        dao.updateBook(bookUpdate);
        Book updatedBook = dao.getBookById(1);
        assertEquals("Updated",updatedBook.getTitle(),"title not match.");
    }
    @Test
    public void deleteBook_test(){
        dao.deleteBook(bookId1);
        Book nullBook = dao.getBookById(1);
        assertNull(nullBook,"Book wasn't delete");
    }
    public void assertBookMatch(Book expected, Book tested){
        String text = "Objects must match";
        assertEquals(expected.getId(),tested.getId(),text);
        assertEquals(expected.getTitle(),tested.getTitle(),text);
        assertEquals(expected.getPublishDate(),tested.getPublishDate(),text);
        assertEquals(expected.getCountStock(),tested.getCountStock(),text);
    }
}
