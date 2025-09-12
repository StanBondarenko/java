package ClassesDOJO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class Loan {
    private long loanId;
    private long readerId;
    private long copyId;
    private OffsetDateTime loanedAt;
    private OffsetDateTime dueAt;
    private OffsetDateTime returnedAt;

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

    public OffsetDateTime getLoanedAt() {
        return loanedAt;
    }

    public void setLoanedAt(OffsetDateTime loanedAt) {
        this.loanedAt = loanedAt;
    }

    public OffsetDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(OffsetDateTime dueAt) {
        this.dueAt = dueAt;
    }

    public OffsetDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(OffsetDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
