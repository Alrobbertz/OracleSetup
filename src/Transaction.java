import java.sql.*;

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

    public void handleTransaction(OraclePipe db) {
        switch(table_changed) {
            case "node" :
                handleNode(db);
                break;

            case "edge" :
                handleEdge(db);
                break;

            case "service" :
                handleService(db);
                break;

            case "employee" :
                handleEmployee(db);
                break;

            case "permission" :
                handlePermissions(db);
                break;

            case "manage" :
                handleManage(db);
                break;

            default :
                System.out.println("This transaction was on table " + table_changed + "... how???");
                break;
        }
    }

    public void handleNode(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handleNodeInsert(db);
                break;
            case "update" :
                handleNodeUpdate(db);
                break;
            case "delete" :
                handleNodeDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }
    }

    public void handleEdge(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handleEdgeInsert(db);
                break;
            case "update" :
                handleEdgeUpdate(db);
                break;
            case "delete" :
                handleEdgeDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleService(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handleServiceInsert(db);
                break;
            case "update" :
                handleServiceUpdate(db);
                break;
            case "delete" :
                handleServiceDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleEmployee(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handleEmployeeInsert(db);
                break;
            case "update" :
                handleEmployeeUpdate(db);
                break;
            case "delete" :
                handleEmployeeDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }

    }

    public void handlePermissions(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handlePermissionInsert(db);
                break;
            case "update" :
                handlePermissionUpdate(db);
                break;
            case "delete" :
                handlePermissionDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed  + "is not valid in this house");
        }
    }

    public void handleManage(OraclePipe db) {
        switch (update_performed) {
            case "insert" :
                handleManageInsert(db);
                break;
            case "update" :
                handleManageUpdate(db);
                break;
            case "delete" :
                handleManageDelete(db);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    /////////////////////// HANDLE NODE ////////////////////////////////

    public void handleNodeInsert(OraclePipe db) {
        Node n;
        try {
            PreparedStatement stmt = db.connection.prepareStatement("SELECT * FROM NODE WHERE NODEID = (?)");
            stmt.setString(1, pkey_row);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                n = new Node(rs);
                System.out.println("Loaded New Node: " + n);
                n.setBeingEdited(true);
                n.update(db);
                //n.delete(db);
                //Add to Map
                //Attach Observers to it
                //Notify
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Issue inserting node, from materialized view call");
            e.printStackTrace();
        }

    }

    public void handleNodeDelete(OraclePipe db) {

    }

    public void handleNodeUpdate(OraclePipe db) {

    }

    /////////////////////// HANDLE EDGE ////////////////////////////////

    public void handleEdgeInsert(OraclePipe db) {
        Edge edge;
        try {
            PreparedStatement stmt = db.connection.prepareStatement("SELECT * FROM EDGE WHERE EDGEID = (?)");
            stmt.setString(1, pkey_row);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                edge = new Edge(rs);
                System.out.println("Loaded New Edge: " + edge);
                //Add to Map
                //Attach Observers to it
                //Notify
            }
        } catch (SQLException e) {
            System.out.println("Issue inserting node, from materialized view call");
            e.printStackTrace();
        }
    }

    public void handleEdgeDelete(OraclePipe db) {

    }

    public void handleEdgeUpdate(OraclePipe db) {

    }

    /////////////////////// HANDLE SERVICE ////////////////////////////////

    public void handleServiceInsert(OraclePipe db) {
//        try {
//            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SERVICE WHERE SERVICEID = (?)");
//            stmt.setString(1, pkey_row);
//            ResultSet rs = stmt.executeQuery();
//            if(rs.next()) {
//                Service s = new Service(rs);
//                System.out.println("Loaded New Service: " + s);
//            }
//        } catch (SQLException e) {
//            System.out.println("Issue inserting service, from materialized view call");
//            e.printStackTrace();
//        }
    }

    public void handleServiceDelete(OraclePipe db) {

    }

    public void handleServiceUpdate(OraclePipe db) {

    }

    /////////////////////// HANDLE EMPLOYEE  ////////////////////////////////

    public void handleEmployeeInsert(OraclePipe db) {

    }

    public void handleEmployeeDelete(OraclePipe db) {

    }

    public void handleEmployeeUpdate(OraclePipe db) {

    }

    /////////////////////// HANDLE PERMISSION  ////////////////////////////////

    public void handlePermissionInsert(OraclePipe db) {

    }

    public void handlePermissionDelete(OraclePipe db) {

    }

    public void handlePermissionUpdate(OraclePipe db) {

    }

    /////////////////////// HANDLE MANAGE ////////////////////////////////

    public void handleManageInsert(OraclePipe db) {

    }

    public void handleManageDelete(OraclePipe db) {

    }

    public void handleManageUpdate(OraclePipe db) {

    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTable_changed() {
        return table_changed;
    }

    public void setTable_changed(String table_changed) {
        this.table_changed = table_changed;
    }

    public String getUpdate_performed() {
        return update_performed;
    }

    public void setUpdate_performed(String update_performed) {
        this.update_performed = update_performed;
    }

    public String getPkey_row() {
        return pkey_row;
    }

    public void setPkey_row(String pkey_row) {
        this.pkey_row = pkey_row;
    }

    public Timestamp getTime_executed() {
        return time_executed;
    }

    public void setTime_executed(Timestamp time_executed) {
        this.time_executed = time_executed;
    }

    public String toString() {
        String temp = "";
        temp += "TransactionID: [" + transactionID + "] TABLE: [" + table_changed + "] CHANGE: [" + update_performed +
                "] PKEY: [" + pkey_row + "]";
        return temp;
    }
}
