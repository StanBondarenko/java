

import ClassesDOJO.Genre;
import JdbcAndDAO.JdbcGenreDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcGenreDaoTest extends BaseDaoTest{
    private  JdbcGenreDao dao;
    private Genre genreId1;
    @BeforeEach
    public void setup(){
        dao= new JdbcGenreDao(dataSource);
        genreId1= new Genre(1,"TestGenre1");
    }
    @Test
    public void getAllGenre(){
        List<Genre> allGenre = dao.getAllGenre();
        assertEquals(4,allGenre.size(),"Size should match");
        assertGenreEquals(genreId1,allGenre.get(0));
    }
    public void assertGenreEquals(Genre ex, Genre test){
        String text = "The genres don't match";
        assertEquals(ex.getId(),test.getId(),text);
        assertEquals(ex.getGenreName(),test.getGenreName(),text);
    }
}
