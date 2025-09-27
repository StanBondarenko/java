

import ClassesDOJO.Reader;
import JdbcAndDAO.JdbcReaderDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JdbcReaderDaoTest extends BaseDaoTest{
    private JdbcReaderDao dao;
    private Reader readerId1;
    @BeforeEach
    public void setup(){
        dao= new JdbcReaderDao(dataSource);
        readerId1 = new Reader(1,"TestReader1_First","TestReader1_Last","Addr 1","+1-555-0001","testreader1@example.com");
    }
    @Test
    public void getReaderById_test_invalid_data(){
        Reader nullReader = dao.getReaderById(10);
        assertNull(nullReader,"Should be null");
    }
    @Test
    public void getReaderById_test_valid_data(){
        Reader readerTest = dao.getReaderById(1);
        assertReaderEquels(readerId1,readerTest);
    }
    @Test
    public  void getAllReaders_test(){
        List<Reader> readers = dao.getAllReaders();
        assertEquals(4,readers.size(),"Size not mutch.");
        assertReaderEquels(readerId1,readers.get(0));
    }
    @Test
    public  void createNewReader_test(){
        Reader blank = new Reader("TestReader5_New","TestReader5","Addr 5","+1-555-0101","testreader5@example.com");
        Reader newReader = dao.createNewReader(blank);
        Reader forCheck = dao.getReaderById(5);
        assertReaderEquels(forCheck,newReader);
    }
    @Test
    public  void updateReader_test(){
        Reader blank = new Reader(1,"Updeted","111","Adwqdqw","+1-555-0101","tdwqcf@example.com");
        dao.updateReader(blank);
        Reader updatedReader = dao.getReaderById(1);
        assertReaderEquels(blank,updatedReader);
    }
    @Test
    public void deleteReader_test(){
        dao.deleteReader(readerId1);
        Reader nullReader = dao.getReaderById(1);
        assertNull(nullReader, "Reader wasn't delete.");
    }
    public void assertReaderEquels(Reader exual, Reader test){
        String text="Readers do not match";
        assertEquals(exual.getId(),test.getId(),text);
        assertEquals(exual.getReaderFirstName(),test.getReaderFirstName(),text);
        assertEquals(exual.getReaderLastName(),test.getReaderLastName(),text);
        assertEquals(exual.getAddress(),test.getAddress(),text);
        assertEquals(exual.getPhoneNumber(),test.getPhoneNumber(),text);
        assertEquals(exual.geteMail(),test.geteMail(),text);

    }
}
