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

    public void handleTransaction(Connection conn) {
        switch(table_changed) {
            case "node" :
                handleNode(conn);
                break;

            case "edge" :
                handleEdge(conn);
                break;

            case "service" :
                handleService(conn);
                break;

            case "employee" :
                handleEmployee(conn);
                break;

            case "permission" :
                handlePermissions(conn);
                break;

            case "manage" :
                handleManage(conn);
                break;

            default :
                System.out.println("This transaction was on table " + table_changed + "... how???");
                break;
        }
    }

    public void handleNode(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handleNodeInsert(conn);
                break;
            case "update" :
                handleNodeUpdate(conn);
                break;
            case "delete" :
                handleNodeDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }
    }

    public void handleEdge(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handleEdgeInsert(conn);
                break;
            case "update" :
                handleEdgeUpdate(conn);
                break;
            case "delete" :
                handleEdgeDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleService(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handleServiceInsert(conn);
                break;
            case "update" :
                handleServiceUpdate(conn);
                break;
            case "delete" :
                handleServiceDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleEmployee(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handleEmployeeInsert(conn);
                break;
            case "update" :
                handleEmployeeUpdate(conn);
                break;
            case "delete" :
                handleEmployeeDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }

    }

    public void handlePermissions(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handlePermissionInsert(conn);
                break;
            case "update" :
                handlePermissionUpdate(conn);
                break;
            case "delete" :
                handlePermissionDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed  + "is not valid in this house");
        }
    }

    public void handleManage(Connection conn) {
        switch (update_performed) {
            case "insert" :
                handleManageInsert(conn);
                break;
            case "update" :
                handleManageUpdate(conn);
                break;
            case "delete" :
                handleManageDelete(conn);
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    /////////////////////// HANDLE NODE ////////////////////////////////

    public void handleNodeInsert(Connection conn) {
        Node n;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM NODE WHERE NODEID = (?)");
            stmt.setString(1, pkey_row);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                n = new Node(rs);
                System.out.println("Loaded New Node: " + n);
                //Add to Map
                //Attach Observers to it
                //Notify
            }
        } catch (SQLException e) {
            System.out.println("Issue inserting node, from materialized view call");
            e.printStackTrace();
        }

    }

    public void handleNodeDelete(Connection conn) {

    }

    public void handleNodeUpdate(Connection conn) {

    }

    /////////////////////// HANDLE EDGE ////////////////////////////////

    public void handleEdgeInsert(Connection conn) {
        Edge edge;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EDGE WHERE EDGEID = (?)");
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

    public void handleEdgeDelete(Connection conn) {

    }

    public void handleEdgeUpdate(Connection conn) {

    }

    /////////////////////// HANDLE SERVICE ////////////////////////////////

    public void handleServiceInsert(Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SERVICE WHERE SERVICEID = (?)");
            stmt.setString(1, pkey_row);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Service s = new Service(rs);
                System.out.println("Loaded New Service: " + s);
            }
        } catch (SQLException e) {
            System.out.println("Issue inserting service, from materialized view call");
            e.printStackTrace();
        }
    }

    public void handleServiceDelete(Connection conn) {

    }

    public void handleServiceUpdate(Connection conn) {

    }

    /////////////////////// HANDLE EMPLOYEE  ////////////////////////////////

    public void handleEmployeeInsert(Connection conn) {

    }

    public void handleEmployeeDelete(Connection conn) {

    }

    public void handleEmployeeUpdate(Connection conn) {

    }

    /////////////////////// HANDLE PERMISSION  ////////////////////////////////

    public void handlePermissionInsert(Connection conn) {

    }

    public void handlePermissionDelete(Connection conn) {

    }

    public void handlePermissionUpdate(Connection conn) {

    }

    /////////////////////// HANDLE MANAGE ////////////////////////////////

    public void handleManageInsert(Connection conn) {

    }

    public void handleManageDelete(Connection conn) {

    }

    public void handleManageUpdate(Connection conn) {

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
