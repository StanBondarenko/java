package Exсaption;

public class DaoExcaption extends Exception {
    public DaoExcaption(){
        super();
    }
    public DaoExcaption(String massage){
        super(massage);
    }
    public DaoExcaption(String massage, Exception e){
        super(massage,e);
    }
}
