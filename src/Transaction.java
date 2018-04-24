import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Transaction {
    private String transactionID;
    private String table_changed;
    private String update_performed;
    private String pkey_row;
    private Timestamp time_executed;

    public Transaction(ResultSet rs) throws SQLException{
        transactionID = rs.getString("transactionID");
        table_changed = rs.getString("table_changed");
        update_performed = rs.getString("update_performed");
        pkey_row = rs.getString("pkey_row");
        time_executed = rs.getTimestamp("time_executed");

    }

}
