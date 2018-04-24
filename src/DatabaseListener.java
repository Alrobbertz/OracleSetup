import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class DatabaseListener implements Runnable {

    private OraclePipe database;
    private Timestamp max_transaction_timestamp;
    private boolean listening;
    private Timestamp start_time;

    public DatabaseListener(OraclePipe pipe) {
        start_time = Timestamp.from(Instant.now());
        this.listening = true;
        this.database = pipe;
        max_transaction_timestamp = Timestamp.from(Instant.now());
    }

    public void listen(int seconds) {
        while(listening) {
            try {
                PreparedStatement stmt = database.connection.prepareStatement("SELECT * FROM mView WHERE TIME_EXECUTED > (?)");
                stmt.setTimestamp(1, max_transaction_timestamp);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Transaction tempTrans = new Transaction(rs);
                    System.out.println(tempTrans.toString());
                    tempTrans.handleTransaction(database);
                    max_transaction_timestamp = tempTrans.getTime_executed();
                }

                rs.close();
                //Timestamp elapsedTime = Timestamp.from(Instant.now() - start_time.toInstant()).;
                System.out.println("Listened");
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Issue Sleeping Listener Thread");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Issue Querying Materialized View in Listener");
            }
        }
    }


    public void run() {
        // code in the other thread, can reference "var" variable
        System.out.println("Starting thread");
        listen(1);

    }
}

//public class MainThreadClass {
//    public static void main(String args[]) {
//        DatabaseListener dbListener = new DatabaseListener(new OraclePipe());
//        Thread t = new Thread(dbListener);
//        t.start();
//    }
//}