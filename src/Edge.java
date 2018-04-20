import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a connection between two nodes. Contains all fields for edges, as well as all methods that operate on edges.
 */
public class Edge {
	private String edge_id;
	private String node_id_one;
	private String node_id_two;

	/**
	 * Constructor
	 * @param edge_id Edge ID
	 * @param node_id_one First node connected to
	 * @param node_id_two Second node connected to
	 */
	public Edge(String edge_id, String node_id_one, String node_id_two) {
		this.edge_id = edge_id;
		this.node_id_one = node_id_one;
		this.node_id_two = node_id_two;
	}

	/**
	 * Empty edge constructor
	 * <p>
	 * For creating dummy edges
	 * @param edge_id
	 */
	public Edge(String edge_id) {
		this.edge_id = edge_id;
		this.node_id_one = node_id_two = null;
	}

	/**
	 * Constructor using raw data from DB
	 * @param rs  OraclePipe data to generate edge from
	 */
	public Edge(ResultSet rs) {
		try {
			this.edge_id = rs.getString("edgeID");
			this.node_id_one = rs.getString("node_id_one");
			this.node_id_two = rs.getString("node_id_two");
		} catch (SQLException e) {
			System.out.println("Sql Exception my dude!");
			e.printStackTrace();
		}
	}

	/**
	 * Creates an edge from data conatined in one CSV line
	 * @param csvLine CSV line containing edge data
	 * @return The created edge
	 */
	public static Edge edgeFromCSV(String csvLine) {
		String[] entries = csvLine.replace("\n", "").split(",");
		try {
			String edge_id = entries[0];
			String node_id_one = entries[1];
			String node_id_two = entries[2];

			return new Edge(edge_id, node_id_one, node_id_two);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converts the edge to a string giving to two nodes it connects
	 * @return String giving the two connected nodes
	 */
	public String toString() {
		return ("Connects: " + node_id_one + " ::::: " + node_id_two);
	}

	/**
	 * Adds the current edge to the database
	 * @param  database The DB to insert the edge into
	 */
	public void insert( OraclePipe database) {
		try (PreparedStatement stmt = database.connection.prepareStatement("INSERT INTO EDGE VALUES(?, ?, ?)")
		) {
			stmt.setString(1, edge_id);
			stmt.setString(2, node_id_one);
			stmt.setString(3, node_id_two);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Sql Exception my dudette!");
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the current edge from the database
	 * @param database The database to delete the edge from
	 * @return wtf rich
	 */
	public Edge delete( OraclePipe database) {
		try (PreparedStatement stmt = database.connection.prepareStatement("DELETE FROM EDGE WHERE EDGEID = (?)")) {
			stmt.setString(1, edge_id);
			stmt.executeUpdate();
			database.connection.commit();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Conversts the edge in to a CSV string
	 * @return Data formatted as CSV
	 */
	public String toCSV() {
		return String.format("%s,%s,%s", edge_id, node_id_one, node_id_two);
	}

	public void update( OraclePipe database) {
		try {
			PreparedStatement stmt = database.connection.prepareStatement("UPDATE Edge SET EDGEID = (?), NODE_ID_ONE = (?), NODE_ID_TWO = (?) WHERE EDGEID = (?)");
			stmt.setString(1, edge_id);
			stmt.setString(2, node_id_one);
			stmt.setString(3, node_id_two);
			stmt.setString(4, edge_id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Index update error");
		}
	}

	//Getters and Setters
	public String getEdge_id() {
		return edge_id;
	}

	public void setEdge_id(String edge_id) {
		this.edge_id = edge_id;
	}

	public String getNode_id_one() {
		return node_id_one;
	}

	public void setNode_id_one(String node_id_one) {
		this.node_id_one = node_id_one;
	}

	public String getNode_id_two() {
		return node_id_two;
	}

	public void setNode_id_two(String node_id_two) {
		this.node_id_two = node_id_two;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return Objects.equals(edge_id, edge.edge_id) &&
				Objects.equals(node_id_one, edge.node_id_one) &&
				Objects.equals(node_id_two, edge.node_id_two);
	}

}
