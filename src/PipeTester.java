import java.sql.Connection;

public class PipeTester {

    public static void main(String[] args) {
        OraclePipe database = new OraclePipe(); // SET UP THE CONNECTION TO ORACLE
        DatabaseListener dbListener = new DatabaseListener(database);
        Thread t =  new Thread(dbListener);
        //database.dropEverything(); // DROP ALL THE OLD TABLES
        //database.createTables(); // CREATE OUR TABLES IN THE ORACLE DB



    }



}
