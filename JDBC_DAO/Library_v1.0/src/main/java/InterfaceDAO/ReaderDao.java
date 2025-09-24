package InterfaceDAO;

import ClassesDOJO.Reader;

public interface ReaderDao {
    Reader getReaderById(int id);
    Reader createNewReader(Reader reader);
}
