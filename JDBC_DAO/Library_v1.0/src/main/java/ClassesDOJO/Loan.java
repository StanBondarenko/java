package ClassesDOJO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Loan {
    private long loanId;
    private long readerId;
    private long copyId;
    private LocalDateTime loaned_at;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;

    public Loan(){}

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getReaderId() {
        return readerId;
    }

    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }

    public long getCopyId() {
        return copyId;
    }

    public void setCopyId(long copyId) {
        this.copyId = copyId;
    }

    public LocalDateTime getLoaned_at() {
        return loaned_at;
    }

    public void setLoaned_at(LocalDateTime loaned_at) {
        this.loaned_at = loaned_at;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(LocalDateTime dueAt) {
        this.dueAt = dueAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
