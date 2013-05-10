package dbPhase.hypeerweb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The Database backend for the HyPeerWeb
 * 
 * @author Spencer Glazier
 */
public class HyPeerWebDatabase {

	public static final java.lang.String DEFAULT_DATABASE_NAME = "HyPeerWeb.db";
	private static HyPeerWebDatabase db;
	private static Connection connection;

	public static final java.lang.String DATABASE_DIRECTORY = "db";

	private HyPeerWebDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Based on the Singleton pattern, we only want to have one instance of the
	 * database in memory and thus use this in order to keep from having several
	 * instances.
	 * 
	 * @return Returns the current instance of the HyPeerWebDatabase
	 */
	public static HyPeerWebDatabase getSingleton() {
		if (db == null) {
			db = new HyPeerWebDatabase();
		}

		return db;
	}

	/**
	 * Opens a new connection to the designated database given as a parameter.
	 * 
	 * @param dbName
	 *            Designated database
	 */
	public void startTransaction(java.lang.String dbName) {
		// AutoCommit is set to true here, although it can be changed in future
		// versions; however, it throws some weird error that will need to be
		// fixed in order to work.

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:./"
					+ DATABASE_DIRECTORY + "/" + dbName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a new connection to the default database.
	 */
	public void startTransaction() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:./"
					+ DATABASE_DIRECTORY + "/" + DEFAULT_DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the tables in the current working database. Also, the SQL will
	 * check to see if the table already exists, if it does, it will not create
	 * the table.
	 */
	public void createTables() {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS Nodes(webID integer, currentState integer, height integer);";
			PreparedStatement pStmt = connection.prepareStatement(sql);
			pStmt.execute();

			sql = "CREATE TABLE IF NOT EXISTS Connections(firstWebID integer, secondWebID integer, connType text, FOREIGN KEY (firstWebID) REFERENCES Nodes(webID), FOREIGN KEY (secondWebID) REFERENCES Nodes(webID));";
			pStmt = connection.prepareStatement(sql);
			pStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drops the tables from the current working database. Also, the SQL will
	 * check to see if the table already exists, if it does, it will not drop
	 * the table.
	 */
	public void dropTables() {
		try {
			String sql = "DROP TABLE IF EXISTS Nodes;";
			PreparedStatement pStmt = connection.prepareStatement(sql);
			pStmt.executeUpdate();

			sql = "DROP TABLE IF EXISTS Connections";
			pStmt = connection.prepareStatement(sql);
			pStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The sister method to startTransaction, this method will close the
	 * connection.
	 * 
	 * @param commit
	 *            Deprecated - this can be revised at a later edition.
	 */
	public void endTransaction(boolean commit) {
		// The parameter is kept, because we want to eventually set AutoCommit
		// to false and have the system either roll back or commit based on the
		// input.

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a node in the database based on the Node given as a parameter.
	 * 
	 * @param node
	 *            Node to be saved to the database.
	 */
	public void createNode(Node node) {
		try {
			String sql = "INSERT INTO Nodes (webID, currentState) VALUES (?, ?);";
			PreparedStatement pStmt = connection.prepareStatement(sql);

			pStmt.setInt(1, node.getWebIdValue());
			pStmt.setInt(2, 0);

			pStmt.executeUpdate();

			sql = "INSERT INTO Connections (firstWebID, secondWebID, connType) VALUES (?, ?, ?);";
			pStmt = connection.prepareStatement(sql);
			pStmt.setInt(1, node.getWebIdValue());

			for (Node tmp : node.getDownPointers()) {
				pStmt.setInt(2, tmp.getWebIdValue());
				pStmt.setString(3, "Surrogate Neighbor");
				pStmt.executeUpdate();
			}

			for (Node tmp : node.getUpPointers()) {
				pStmt.setInt(2, tmp.getWebIdValue());
				pStmt.setString(3, "Inverse Surrogate Neighbor");
				pStmt.executeUpdate();
			}

			for (Node tmp : node.getNeighbors()) {
				pStmt.setInt(2, tmp.getWebIdValue());
				pStmt.setString(3, "Neighbor");
				pStmt.executeUpdate();
			}

			pStmt.setInt(2, node.getFold().getWebIdValue());
			pStmt.setString(3, "Fold");
			pStmt.executeUpdate();

			pStmt.setInt(2, node.getSurrogateFold().getWebIdValue());
			pStmt.setString(3, "Surrogate Fold");
			pStmt.executeUpdate();

			pStmt.setInt(2, node.getInverseSurrogateFold().getWebIdValue());
			pStmt.setString(3, "Inverse Surrogate Fold");
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// A helper method for getting a single node or all of the nodes. Builds the
	// HashSets needed to hold the UpPointers, DownPointers, and Neighbors.
	private HashSet<Integer> buildSet(ResultSet rs) throws SQLException {
		HashSet<Integer> set = new HashSet<Integer>();

		while (rs.next()) {
			set.add(rs.getInt("secondWebID"));
		}

		return set;
	}

	// Gets the connections webID. This is also a helper method for getting a
	// single node or all the nodes from the database. It creates
	// the necessary webID based on the ResultSet given.
	private int getConnectionWebID(ResultSet rs) throws SQLException {
		int i = 0;

		if (rs.next()) {
			i = rs.getInt("secondWebID");
		}

		return i;
	}

	/**
	 * Gets all the nodes in the database. The method works by getting the first
	 * node and assigning all of its connections, then looping through all of
	 * the other nodes and assigning their connections.
	 * 
	 * @return ArrayList of Nodes
	 */
	public ArrayList<Node> getAllNodes() {
		try {
			ArrayList<Node> nodes = new ArrayList<Node>();

			String sql = "SELECT webID FROM Nodes;";
			PreparedStatement pStmt = connection.prepareStatement(sql);

			ResultSet rs = pStmt.executeQuery();

			while (rs.next()) {

				sql = "SELECT secondWebID FROM Connections WHERE firstWebID = ? AND connType = ?;";
				pStmt = connection.prepareStatement(sql);
				pStmt.setInt(1, rs.getInt("webID"));

				Node node = new Node(rs.getInt("webID"));

				pStmt.setString(2, "Inverse Surrogate Neighbor");
				ResultSet tmpRS = pStmt.executeQuery();
				HashSet<Integer> upPointers = buildSet(tmpRS);

				for (int i : upPointers) {
					Node tmp = new Node(i);
					node.addUpPointer(tmp);
				}

				pStmt.setString(2, "Surrogate Neighbor");
				tmpRS = pStmt.executeQuery();
				HashSet<Integer> downPointers = buildSet(tmpRS);

				for (int i : downPointers) {
					Node tmp = new Node(i);
					node.addDownPointer(tmp);
				}

				pStmt.setString(2, "Neighbor");
				tmpRS = pStmt.executeQuery();
				HashSet<Integer> neighbors = buildSet(tmpRS);

				for (int i : neighbors) {
					Node tmp = new Node(i);
					node.addNeighbor(tmp);
				}

				pStmt.setString(2, "Fold");
				tmpRS = pStmt.executeQuery();
				int fold = getConnectionWebID(tmpRS);

				Node tmp = new Node(fold);
				node.setFold(tmp);

				pStmt.setString(2, "Surrogate Fold");
				tmpRS = pStmt.executeQuery();
				int surrogateFold = getConnectionWebID(tmpRS);

				tmp = new Node(surrogateFold);
				node.setSurrogateFold(tmp);

				pStmt.setString(2, "Inverse Surrogate Fold");
				tmpRS = pStmt.executeQuery();
				int inverseSurrogateFold = getConnectionWebID(tmpRS);

				tmp = new Node(inverseSurrogateFold);
				node.setInverseSurrogateFold(tmp);

				nodes.add(node);
			}
			return nodes;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets a single node from the database. However, instead of returning a
	 * Node object, it returns a SimplifiedNodeDomain object, which is important
	 * for testing purposes.
	 * 
	 * @param webId
	 *            WebID of the Node you want to get.
	 * @return SimplifiedNodeDomain version of the object in the database.
	 */
	public SimplifiedNodeDomain getNode(int webId) {
		try {
			String sql = "SELECT secondWebID FROM Connections WHERE webID = ? AND connType = ?;";
			PreparedStatement pStmt = connection.prepareStatement(sql);

			pStmt.setInt(1, webId);
			pStmt.setString(2, "Surrogate Neighbor");

			ResultSet rs = pStmt.executeQuery();
			HashSet<Integer> downPointers = buildSet(rs);

			pStmt.setString(2, "Inverse Surrogate Neighbor");
			rs = pStmt.executeQuery();
			HashSet<Integer> upPointers = buildSet(rs);

			pStmt.setString(2, "Neighbor");
			rs = pStmt.executeQuery();
			HashSet<Integer> neighbors = buildSet(rs);

			pStmt.setString(2, "Fold");
			rs = pStmt.executeQuery();
			int fold = getConnectionWebID(rs);

			pStmt.setString(2, "Surrogate Fold");
			rs = pStmt.executeQuery();
			int surrogateFold = getConnectionWebID(rs);

			pStmt.setString(2, "Inverse Surrogate Fold");
			rs = pStmt.executeQuery();
			int inverseSurrogateFold = getConnectionWebID(rs);

			sql = "SELECT height FROM Nodes WHERE webID = ?;";
			pStmt = connection.prepareStatement(sql);

			pStmt.setInt(1, webId);
			rs = pStmt.executeQuery();

			int height = 0;
			if (rs.next()) {
				height = rs.getInt("height");
			}

			SimplifiedNodeDomain node = new SimplifiedNodeDomain(webId, height,
					neighbors, upPointers, downPointers, fold, surrogateFold,
					inverseSurrogateFold);

			return node;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
