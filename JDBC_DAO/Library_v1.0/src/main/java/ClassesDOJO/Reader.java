package ClassesDOJO;

public class Reader {
    private long id;
    private String readerFirstName;
    private String readerLastName;
    private String address;
    private String phoneNumber;
    private String eMail;
    public Reader(){
    }

    public Reader(String readerFirstName, String readerLastName, String address, String phoneNumber, String eMail) {
        this.readerFirstName = readerFirstName;
        this.readerLastName = readerLastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReaderFirstName() {
        return readerFirstName;
    }

    public void setReaderFirstName(String readerFirstName) {
        this.readerFirstName = readerFirstName;
    }

    public String getReaderLastName() {
        return readerLastName;
    }

    public void setReaderLastName(String readerLastName) {
        this.readerLastName = readerLastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    @Override
    public String toString() {
        return "Reader ID--> "+this.id+
                " Full name--> "+this.readerFirstName+" "+this.readerLastName+
                " Address--> "+this.address+
                " Phone number--> "+this.phoneNumber+
                " E-mail--> "+this.eMail;
    }
}
