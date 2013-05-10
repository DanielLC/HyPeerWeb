package dbPhase.hypeerweb;

import java.util.ArrayList;

/**
 * A web of Peer to Peer connected nodes
 * 
 * @author Matthew
 */
public class HyPeerWeb {

	private static HyPeerWeb singleton = null;

	private ArrayList<Node> nodes;
	private HyPeerWebDatabase database;

	private HyPeerWeb() {
		nodes = new ArrayList<Node>();
		database = HyPeerWebDatabase.getSingleton();
	}

	/**
	 * @return The single instance of the HyPeerWeb, creating it if necessary
	 */
	public static HyPeerWeb getSingleton() {
		if (singleton == null) {
			singleton = new HyPeerWeb();
		}
		return singleton;
	}

	/**
	 * 
	 * @return The number of nodes in the HyPeerWeb
	 */
	public int size() {
		return nodes.size();
	}

	/**
	 * @param i
	 *            The index (WebID value) of the node to get
	 * @return The ith node or null if i >= size()
	 * @pre i > -1
	 */
	public Node getNode(int i) {
		if (i >= size()) {
			return null;
		} else {
			return nodes.get(i);
		}
	}

	/**
	 * Checks whether a node is in the HyPeerWeb
	 * 
	 * @param node
	 *            the node to look for
	 * @return true if the node is in the HyPeerWeb, false if not
	 * @pre node != null
	 */
	public boolean contains(Node node) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).equals(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Clears the HyPeerWeb
	 * 
	 * @post size() == 0
	 */
	public void clear() {
		nodes.clear();
	}

	/**
	 * Adds a node to the HyPeerWeb
	 * 
	 * @param node
	 *            The node to add
	 * @pre node != null
	 * @post The node is in the HyPeerWeb
	 */
	public void addNode(Node node) {
		nodes.add(node);
	}

	/**
	 * Removes a node from the HyPeerWeb
	 * 
	 * @param node
	 *            The node to remove
	 * @pre node != null
	 * @post The node is not in the HyPeerWeb
	 */
	public void removeNode(Node node) {
		nodes.remove(node);
	}

	/**
	 * Loads the HyPeerWeb from the database with the default name
	 * 
	 * @post All prievious nodes are removed and all nodes from the database are
	 *       added
	 */
	public void reload() {
		database.startTransaction();

		nodes = database.getAllNodes();

		database.endTransaction(true);
	}

	/**
	 * Loads the HyPeerWeb from the database with the given name
	 * 
	 * @param dbName
	 *            The name of the database file to load from
	 * @post All prievious nodes are removed and all nodes from the database are
	 *       added
	 */
	public void reload(java.lang.String dbName) {
		database.startTransaction(dbName);
		database.createTables();
		nodes = database.getAllNodes();

		database.endTransaction(true);
	}

	/**
	 * @return The single instance of the HyPeerWebDatabase
	 */
	public HyPeerWebDatabase getHyPeerWebDatabase() {
		return database;
	}

	/**
	 * Saves the state of the HyPeerWeb to the database
	 * 
	 * @post Prievious database information is replaced with the current state
	 *       of the HyPeerWeb
	 */
	public void saveToDatabase() {
		database.startTransaction();
		database.dropTables();
		database.createTables();
		for (Node node : nodes) {
			database.createNode(node);
		}

		database.endTransaction(true);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Database: ");
		sb.append(database);
		sb.append('\n');
		sb.append("Nodes: ");
		for (int i = 0; i < size(); i++) {
			sb.append(nodes.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof HyPeerWeb)) {
			return false;
		}
		HyPeerWeb other = (HyPeerWeb) o;
		if (database != other.getHyPeerWebDatabase()) {
			return false;
		}
		if (nodes.size() != other.size()) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			if (nodes.get(i) != other.getNode(i)) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = database.hashCode();
		for (int i = 0; i < size(); i++) {
			result += nodes.get(i).hashCode();
		}
		return result;
	}
}
