package InterfaceDAO;

import ClassesDOJO.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAllGenre();
    void addGenreToGenreBook(long bookId, long genreId);
}
