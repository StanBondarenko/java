package RowMappers;

import ClassesDOJO.Loan;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class LoanRowMapper implements RowMapper<Loan> {
    @Override
    public Loan mapRow(ResultSet rs, int rowNum) throws SQLException {
       Loan loan = new Loan();
       loan.setLoanId(rs.getLong("loan_id"));
       loan.setReaderId(rs.getLong("reader_id"));
       loan.setCopyId(rs.getLong("copy_id"));
       loan.setLoanedAt(rs.getObject("loaned_at", OffsetDateTime.class));
       loan.setDueAt(rs.getObject("due_at", OffsetDateTime.class));
       loan.setReturnedAt(rs.getObject("returned_at", OffsetDateTime.class));
       return loan;
    }
}
