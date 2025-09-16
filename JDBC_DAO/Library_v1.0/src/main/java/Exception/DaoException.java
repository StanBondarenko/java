package Exception;

public class DaoException extends RuntimeException {
    public DaoException(){
        super();
    }
    public DaoException(String massage){
        super(massage);
    }
    public DaoException(String massage, Exception e){
        super(massage,e);
    }
}
