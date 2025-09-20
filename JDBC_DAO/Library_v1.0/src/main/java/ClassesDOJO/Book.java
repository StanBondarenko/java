package ClassesDOJO;

import java.time.LocalDate;

public class Book {
    private long id;
    private  String title;
    private LocalDate publishDate;
    private int countStock;

    public Book(){
    }

    public Book(String title, LocalDate publishDate, int countStock) {
        this.title = title;
        this.publishDate = publishDate;
        this.countStock = countStock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public int getCountStock() {
        return countStock;
    }

    public void setCountStock(int countStock) {
        this.countStock = countStock;
    }
    @Override
    public  String toString(){
        return "Book ID->  "+this.id+
                " Title->  "+this.title+
                " Publish Date->  "+publishDate+
                " Left in Stock->  "+countStock;
    }

}
