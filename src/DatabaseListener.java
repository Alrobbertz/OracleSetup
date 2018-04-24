import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DatabaseListener implements Runnable {

    private OraclePipe database;
    private Timestamp max_transaction_timestamp;

    public DatabaseListener(OraclePipe pipe) {
        this.database = database;
    }

    public void listen(int seconds) {
        try {
            PreparedStatement stmt = database.connection.prepareStatement("SELECT * FROM mView WHERE TIMESTAMP > (?)");
            stmt.setTimestamp(1, max_transaction_timestamp);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                processTransaction(rs);
            }
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Issue Sleeping Listener Thread");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Issue Querying Materialized View in Listener");
        }
    }

    public void processTransaction(ResultSet rs) {

    }



    public void run() {
        // code in the other thread, can reference "var" variable
    }
}

public class MainThreadClass {
    public static void main(String args[]) {
        DatabaseListener dbListener = new DatabaseListener(new OraclePipe());
        Thread t = new Thread(dbListener);
        t.start();
    }
}