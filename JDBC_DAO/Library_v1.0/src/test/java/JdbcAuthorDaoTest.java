

import ClassesDOJO.Author;
import JdbcAndDAO.JdbcAuthorDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcAuthorDaoTest extends BaseDaoTest {
    private JdbcAuthorDao dao;
    private Author authorId1;
    @BeforeEach
    public void setup(){
        dao = new JdbcAuthorDao(dataSource);
        authorId1= new Author(1,"TestAuthorA_First","TestAuthorA_Last", LocalDate.of(1970,1,1),null);
    }

    @Test
    public void getAuthorById_test_with_invalid_id(){
        Author nullAuthor = dao.getAuthorById(100);
        assertNull(nullAuthor,"Most be null");
    }

    @Test
    public void getAuthorById_test_with_valid_id(){
        Author authorTestId1 = dao.getAuthorById(1);
        assertAuthorsEquals(authorId1,authorTestId1,"Objects must match.");
    }
    @Test
    public void getAuthorByFirstName_test_with_invalid_name(){
        List<Author> nullList = dao.getAuthorByFirstName("FAKE_NAME!!11",true);
        assertNull(nullList,"Must be null");
    }
    @Test
    public void getAuthorByFirstName_test_with_valid_name(){
        List<Author> testList = dao.getAuthorByFirstName("Test", false);
        assertEquals(4,testList.size(),"Size not correct");
        assertAuthorsEquals(authorId1,testList.get(0),"Size must match");
    }
    @Test
    public void getAuthorByLastName_test_with_invalid_Last_name(){
        List<Author> nullList = dao.getAuthorByLastName("Fake_lastName1111",true);
        assertNull(nullList,"Must be null");
    }
    @Test
    public void getAuthorByLastName_test_with_valid_Last_name(){
        List<Author> testList = dao.getAuthorByLastName("Test",false);
        assertEquals(4,testList.size(),"Size must match");
        assertAuthorsEquals(authorId1,testList.get(0),"Objects must match.");
    }
    @Test
    public void getAuthorByTitleBook_test_with_invalid_title(){
        List<Author> nullAuthor = dao.getAuthorByBookTitle("FAKE",true);
        assertNull(nullAuthor,"should be null");
    }
    @Test
    public void getAuthorByTitleBook_test_with_valid_title(){
        List<Author> listTest = dao.getAuthorByBookTitle("Test",false);
        assertEquals(4,listTest.size(),"Size should be 4");
        assertAuthorsEquals(authorId1,listTest.get(0),"Objects must match.");
    }
    @Test
    public void deleteAuthor_test(){
        dao.deleteAuthor(authorId1);
        int id = Integer.parseInt(String.valueOf(authorId1.getId()));
        Author author = dao.getAuthorById(id);
        assertNull(author,"Retern deleted author");
    }
    @Test
    public void updateAuthor_Test(){
        Author updatedAuthor= new Author(1,"UpdatedName","UpdetedLastName",LocalDate.of(2021,2,22),LocalDate.of(2025,03,25));
        dao.updateAuthor(updatedAuthor);
        Author checkUpdate = dao.getAuthorById(1);
        assertAuthorsEquals(updatedAuthor,checkUpdate,"Objects must match.");
    }
    @Test
    public void getAllAuthors_Test(){
        List<Author> allAuthors = dao.getAllAuthors();
        assertNotNull(allAuthors,"Shoudn't be Null");
        assertEquals(4,allAuthors.size(),"Size should be same");
        assertAuthorsEquals(authorId1,allAuthors.get(0),"Objects must match.");
    }
    @Test
    public void createAuthor_Test(){
        Author blanckAuthor = new Author("NewFerstName","NewLastName",LocalDate.of(2021,02,22),LocalDate.of(2025,03,25));
        Author newAuthor = dao.createAuthor(blanckAuthor);
        assertEquals("NewFerstName",newAuthor.getAuthorFirstName(),"First name should match");
        assertEquals("NewLastName",newAuthor.getAuthorLastName(),"Last name should match");

    }
    private void assertAuthorsEquals(Author expected, Author actual, String massage){
        assertEquals(expected.getId(),actual.getId(),massage);
        assertEquals(expected.getAuthorFirstName(),actual.getAuthorFirstName(),massage);
        assertEquals(expected.getAuthorLastName(),actual.getAuthorLastName(),massage);
        assertEquals(expected.getBirthday(),actual.getBirthday(),massage);
        assertEquals(expected.getDeathDate(),actual.getDeathDate(),massage);
    }
}
