import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a point of interest on the map. This class contains all the fields for a node, as well as creation and import of nodes.
 */
public class Node {
	private String node_id;
	private String long_name;
	private String short_name;
	private double x_2d, y_2d;
	private double x_3d, y_3d;
	private String node_type;
	private String building;
	private String floor;
	private ArrayList<Edge> edges;
	private ArrayList<Node> neighbors;
	private Node source;
	private double weight = 10000;
	private double pureweight = 10000;
	private int floornumber;
	private int timesvisited = 0;
	private boolean isBeingEdited;
	/**
	 * Constructor
	 * @param node_id 10-Character ID, has specific nomencalutre
	 * @param long_name Long format name
	 * @param short_name Short format name
	 * @param x_2d X-Coordinate on 2D map
	 * @param y_2d Y-Coordinate on 2D map
	 * @param x_3d X-Coordinate on 3D map
	 * @param y_3d Y-Coodrinate on 3D map
	 * @param node_type Type of location, such as hall or department
	 * @param building Building containing the node
	 * @param floor Floor containing the node
	 */
	public Node(String node_id, String long_name, String short_name, double x_2d, double y_2d, double x_3d, double y_3d, String node_type, String building, String floor) {
		this.node_id = node_id;
		this.long_name = long_name;
		this.short_name = short_name;
		this.x_2d = x_2d;
		this.y_2d = y_2d;
		this.x_3d = x_3d;
		this.y_3d = y_3d;
		this.node_type = node_type;
		this.building = building;
		this.floor = floor;
		edges = new ArrayList<>();
		neighbors = new ArrayList<>();
		source = null;
		weight = 10000.0;
		pureweight = 10000.0;
		setFloor(floor);
	}

	/**
	 * Dummy node constructor for searching nodes
	 * @param node_id The node ID to be searched for
	 */
	public Node(String node_id) {
		this.node_id = node_id;
		this.long_name = this.short_name = node_type = building = floor = null;
		this.x_2d = this.y_2d = this.x_3d = this.y_3d = 0;
	}

	/**
	 * Constructor to create node from database ResultSet
	 * @param rs Data to craete the node from
	 */
	public Node(ResultSet rs) {
		try {
			this.node_id = rs.getString("nodeID");
			this.long_name = rs.getString("longName");
			this.short_name = rs.getString("shortName");
			this.x_2d = rs.getDouble("coordX2D");
			this.y_2d = rs.getDouble("coordY2D");
			this.x_3d = rs.getDouble("coordX3D");
			this.y_3d = rs.getDouble("coordY3D");
			this.node_type = rs.getString("nodeType");
			this.building = rs.getString("building");
			this.floor = rs.getString("floor");
			this.isBeingEdited = rs.getBoolean("isBeingEdited");
			this.edges = new ArrayList<>();
			this.neighbors = new ArrayList<>();
		} catch (SQLException e) {
			System.out.println("Sql Exception :OOOO");
			e.printStackTrace();
		}
		source = null;
		weight = 10000.0;
		pureweight = 10000.0;
		switch (floor){
			case("L2"): floornumber = 0; break;
			case("L1"): floornumber = 1; break;
			case("1"): floornumber =  2; break;
			case("2"): floornumber =  3; break;
			case("3"): floornumber =  4; break;
		}
	}


	/**
	 * Creates a node from a single CSV line
	 * @param csvLine The CSV line with node data
	 * @return Created node
	 */
	public static Node nodeFromCSV(String csvLine) {
		String[] entries = csvLine.replace("\n", "").split(",");

		try {
			String node_id = entries[0];
			double x_2d = Double.parseDouble(entries[1]);
			double y_2d = Double.parseDouble(entries[2]);
			String floor = entries[3];
			String building = entries[4];
			String node_type = entries[5];
			String long_name = entries[6];
			String short_name = entries[7];
			double x_3d = Double.parseDouble(entries[9]);
			double y_3d = Double.parseDouble(entries[10]);
			int weight = 0;

			return new Node(node_id, long_name, short_name, x_2d, y_2d, x_3d, y_3d, node_type, building, floor);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Delete this node from the database
	 * @param database The database containing the node
	 */
	public void delete( OraclePipe database) {
		try {
			deleteEdges(database);
			PreparedStatement stmt = database.connection.prepareStatement("DELETE FROM NODE WHERE NODEID = (?)");
			stmt.setString(1, getNode_id());
			stmt.executeUpdate();
			stmt.close();
			System.out.println("node deleted");
		} catch (SQLException e) {
			System.out.println("SQL");
			e.printStackTrace();
		}
	}

	public void deleteEdges(OraclePipe database) {
		try {
			PreparedStatement stmt = database.connection.prepareStatement("SELECT * FROM EDGE WHERE NODE_ID_ONE = (?) OR NODE_ID_TWO = (?)");
			stmt.setString(1, getNode_id());
			stmt.setString(2, getNode_id());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Edge next = new Edge(rs);
				next.delete(database);
			}
		} catch (SQLException e) {
			System.out.println("trouble getting rid of dem nodes");
		}
	}

	/**
	 * Adds an edge to the node
	 * <p>
	 * This will not "connect" any other nodes on the edge to this node
	 * @param edge The edge to be added
	 */
	public void addEdge(Edge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge);
		}
	}

	/**
	 * Converts the node in to a string that gives its ID and weight
	 * @return String containing node ID and weight
	 */
	public String toString() {
		return (long_name + " Building: " + building + " Floor: " + floor);
		//return node_id + " Weight = " + weight;
	}

	public String printItAll() {
		String print = "";
		print += "Node ID: " + this.node_id + "\nLong Name: " + this.long_name;
		print += "\nShort Name: " + this.short_name + "\nWeight: " + this.weight;
		print += "\nPureWeight: " + this.pureweight;
		print += "\nNeighbors :\n" + printNeighbors();
		return print;


	}

	public String printNeighbors(){
		String print = "";
		for(Node neighbor: neighbors) {
			print += "[" + neighbor.toString() + "]";
		}
		return print;

	}

	/**
	 * Sends the node to the database
	 * @param database the database to send the node to
	 */
	public void insert( OraclePipe database) {
		System.out.println("node inserted");
		try {
			PreparedStatement stmt = database.connection.prepareStatement("INSERT INTO NODE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, node_id);
			stmt.setString(2, long_name);
			stmt.setString(3, short_name);
			stmt.setDouble(4, x_2d);
			stmt.setDouble(5, y_2d);
			stmt.setDouble(6, x_3d);
			stmt.setDouble(7, y_3d);
			stmt.setString(8, node_type);
			stmt.setString(9, building);
			stmt.setString(10, floor);
			stmt.setBoolean(11, isBeingEdited);
			stmt.executeUpdate();
			database.connection.commit();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("Sql Exception my dude!");
			e.printStackTrace();
		}
	}

	/**
	 * Esport the node fields as a CSV string
	 * @return CSV of node data
	 */
	public String toCSV() {
		return String.format("%s,%.0f,%.0f,%s,%s,%s,%s,%s,%.0f,%.0f", node_id, x_2d, y_2d, floor, building, node_type, long_name, short_name, x_3d, y_3d);
	}

	/**
	 * Refreshes the database
	 * @param database
	 */
	public void update( OraclePipe database) {
		try {
			PreparedStatement stmt = database.connection.prepareStatement("UPDATE Node SET NODEID = (?), LONGNAME = (?), SHORTNAME = (?), \n" +
					"COORDX2D = (?), COORDY2D = (?), COORDX3D = (?), COORDY3D = (?),\n" +
					"NODETYPE = (?), BUILDING = (?), FLOOR = (?), ISBEINGEDITED = (?) WHERE NODEID = (?)");
			stmt.setString(1, node_id);
			stmt.setString(2, long_name);
			stmt.setString(3, short_name);
			stmt.setDouble(4, x_2d);
			stmt.setDouble(5, y_2d);
			stmt.setDouble(6, x_3d);
			stmt.setDouble(7, y_3d);
			stmt.setString(8, node_type);
			stmt.setString(9, building);
			stmt.setString(10, floor);
			stmt.setBoolean(11, isBeingEdited);
			stmt.setString(12, node_id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Index update error");
		}
	}

	//Getters and Setters
	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	//Getter for node_id
	public String get_node_id() {
		return node_id;
	}

	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(Node node) {
		neighbors.add(node);

	}

	public void setNeighbors(ArrayList<Node> neighbors) {
		ArrayList<Node> tempneighbors = new ArrayList<>(neighbors);
		this.neighbors = tempneighbors;
	}

	public void generate2d() {
		switch (floor) {
			case "1":
				x_2d = 1.08642882 * x_3d + 0.69735628 * y_3d - 1037.37682587;
				y_2d = -0.55264221 * x_3d + 1.51397163 * y_3d - 55.84994856;
				break;
			case "2":
				x_2d = 1.10611156 * x_3d + 0.70469044 * y_3d - 1084.78401994;
				y_2d = -0.55207467 * x_3d + 1.49566961 * y_3d + 9.10817152;
				break;
			case "3":
				x_2d = 1.10864092 * x_3d + 0.72586456 * y_3d - 1113.76074646;
				y_2d = -0.5553405 * x_3d + 1.53375625 * y_3d + 3.59473034;
				break;
			case "L1":
				x_2d = 1.10513906 * x_3d + 0.7407155 * y_3d - 1186.89332935;
				y_2d = -0.55091499 * x_3d + 1.49209425 * y_3d - 67.25975378;
				break;
			case "L2":
				x_2d = 1.01438622 * x_3d + 0.67732539 * y_3d - 931.69677785;
				y_2d = -0.51871678 * x_3d + 1.58332171 * y_3d - 305.22339117;
				break;
		}

	}

	public void generate3d() {
		switch (floor) {
			case "1":
				x_3d = 0.71649644 * x_2d - 0.3491012 * y_2d + 822.46096671;
				y_3d = 0.27010337 * x_2d + 0.53370892 * y_2d + 318.57886913;
				break;
			case "2":
				x_3d = 0.73228378 * x_2d - 0.34363391 * y_2d + 794.79224328;
				y_3d = 0.26926342 * x_2d + 0.53905684 * y_2d + 293.28131411;
				break;
			case "3":
				x_3d = 0.72819525 * x_2d - 0.34445566 * y_2d + 814.35718851;
				y_3d = 0.2639599 * x_2d + 0.52646443 * y_2d + 293.08285013;
				break;
			case "L1":
				x_3d = 0.72549407 * x_2d - 0.35985439 * y_2d + 836.12809855;
				y_3d = 0.26726916 * x_2d + 0.53663225 * y_2d + 355.93529706;
				break;
			case "L2":
				x_3d = 0.76544317 * x_2d - 0.35830216 * y_2d + 746.59689863;
				y_3d = 0.23769532 * x_2d + 0.51005328 * y_2d + 467.79806508;
				break;
		}
	}

	//public add/update

	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	//public add/update

	public double getX_2d() {
		return x_2d;
	}

	public void setX_2d(double x_2d) {
		this.x_2d = x_2d;
	}

	public double getY_2d() {
		return y_2d;
	}

	public void setY_2d(double y_2d) {
		this.y_2d = y_2d;
	}

	public double getX_3d() {
		return x_3d;
	}

	public void setX_3d(double x_3d) {
		this.x_3d = x_3d;
	}

	public double getY_3d() {
		return y_3d;
	}

	public void setY_3d(double y_3d) {
		this.y_3d = y_3d;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
		switch (floor){
			case("L2"): floornumber = 0; break;
			case("L1"): floornumber = 1; break;
			case("1"): floornumber =  2; break;
			case("2"): floornumber =  3; break;
			case("3"): floornumber =  4; break;
		}
	}

	public int getFloornumber() {
		return floornumber;
	}

		public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setPureweight(double pureweight) {
		this.pureweight = pureweight;
	}

	public double getPureweight() {
		return pureweight;
	}

	public void clear() {
		this.source = null;
	}

	public int getTimesvisited() {
		return timesvisited;
	}
	public void incrementTimesvisisted(){
		timesvisited = timesvisited + 1;
	}

	/**
	 * Gets a copy of the current node
	 * @return copy of the node
	 */
	public Node copyNode() {

		Node clonenode = new Node(node_id, long_name, short_name, x_2d, y_2d, x_3d, y_3d, node_type, building, floor);

		clonenode.edges = edges;
		clonenode.neighbors = neighbors;
		clonenode.source = source;
		clonenode.weight = weight;
		clonenode.pureweight = pureweight;
		clonenode.timesvisited = timesvisited;
		return clonenode;
	}

	/**
	 * Check if this node shares IDs with another node
	 * @param node Node to check
	 * @return true if IDs are equal
	 */
	public boolean hasSameID(Node node) {
		return this.node_id.equals(node.get_node_id());

	}

    public boolean isBeingEdited() {
        return isBeingEdited;
    }

    public void setBeingEdited(boolean beingEdited) {
        isBeingEdited = beingEdited;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node node = (Node) o;
		return Double.compare(node.x_2d, x_2d) == 0 &&
				Double.compare(node.y_2d, y_2d) == 0 &&
				Double.compare(node.x_3d, x_3d) == 0 &&
				Double.compare(node.y_3d, y_3d) == 0 &&
				Objects.equals(node_id, node.node_id) &&
				Objects.equals(long_name, node.long_name) &&
				Objects.equals(short_name, node.short_name) &&
				Objects.equals(node_type, node.node_type) &&
				Objects.equals(building, node.building) &&
				Objects.equals(floor, node.floor);
	}

}
