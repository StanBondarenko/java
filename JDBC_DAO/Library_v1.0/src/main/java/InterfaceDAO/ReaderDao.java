package InterfaceDAO;

import ClassesDOJO.Reader;

import java.util.List;

public interface ReaderDao {
    Reader getReaderById(int id);
    Reader createNewReader(Reader reader);
    void updateReader(Reader reader);
    List<Reader> getAllReaders();
    void deleteReader(Reader readerForDelete);
}
