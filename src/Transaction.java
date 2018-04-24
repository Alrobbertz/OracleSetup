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

    public void handleTransaction() {
        switch(table_changed) {
            case "node" :
                handleNode();
                break;

            case "edge" :
                handleEdge();
                break;

            case "service" :
                handleService();
                break;

            case "employee" :
                handleEmployee();
                break;

            case "permission" :
                handlePermissions();
                break;

            case "manage" :
                handleManage();
                break;

            default :
                System.out.println("This transaction was on table " + table_changed + "... how???");
                break;
        }
    }

    public void handleNode() {
        switch (update_performed) {
            case "insert" :
                handleNodeInsert();
                break;
            case "update" :
                handleNodeUpdate();
                break;
            case "delete" :
                handleNodeDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }
    }

    public void handleEdge() {
        switch (update_performed) {
            case "insert" :
                handleEdgeInsert();
                break;
            case "update" :
                handleEdgeUpdate();
                break;
            case "delete" :
                handleEdgeDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleService() {
        switch (update_performed) {
            case "insert" :
                handleServiceInsert();
                break;
            case "update" :
                handleServiceUpdate();
                break;
            case "delete" :
                handleServiceDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    public void handleEmployee() {
        switch (update_performed) {
            case "insert" :
                handleEmployeeInsert();
                break;
            case "update" :
                handleEmployeeUpdate();
                break;
            case "delete" :
                handleEmployeeDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed +  "is not valid in this house");
        }

    }

    public void handlePermissions() {
        switch (update_performed) {
            case "insert" :
                handlePermissionInsert();
                break;
            case "update" :
                handlePermissionUpdate();
                break;
            case "delete" :
                handlePermissionDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed  + "is not valid in this house");
        }
    }

    public void handleManage() {
        switch (update_performed) {
            case "insert" :
                handleManageInsert();
                break;
            case "update" :
                handleManageUpdate();
                break;
            case "delete" :
                handleManageDelete();
                break;
            default:
                System.out.println("What kinda update are you doing bro? " + update_performed + "is not valid in this house");
        }
    }

    /////////////////////// HANDLE NODE ////////////////////////////////

    public void handleNodeInsert() {

    }

    public void handleNodeDelete() {

    }

    public void handleNodeUpdate() {

    }

    /////////////////////// HANDLE EDGE ////////////////////////////////

    public void handleEdgeInsert() {

    }

    public void handleEdgeDelete() {

    }

    public void handleEdgeUpdate() {

    }

    /////////////////////// HANDLE SERVICE ////////////////////////////////

    public void handleServiceInsert() {

    }

    public void handleServiceDelete() {

    }

    public void handleServiceUpdate() {

    }

    /////////////////////// HANDLE EMPLOYEE  ////////////////////////////////

    public void handleEmployeeInsert() {

    }

    public void handleEmployeeDelete() {

    }

    public void handleEmployeeUpdate() {

    }

    /////////////////////// HANDLE PERMISSION  ////////////////////////////////

    public void handlePermissionInsert() {

    }

    public void handlePermissionDelete() {

    }

    public void handlePermissionUpdate() {

    }

    /////////////////////// HANDLE MANAGE ////////////////////////////////

    public void handleManageInsert() {

    }

    public void handleManageDelete() {

    }

    public void handleManageUpdate() {

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
}
