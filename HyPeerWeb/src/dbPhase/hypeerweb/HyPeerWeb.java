package dbPhase.hypeerweb;

import java.util.ArrayList;

public class HyPeerWeb {

	private static HyPeerWeb singleton = null;
	
	private ArrayList<Node> nodes;
	private HyPeerWebDatabase database;
	
	private HyPeerWeb()
	{
		nodes = new ArrayList<Node>();
		database = HyPeerWebDatabase.getSingleton();
	}
	
	public static HyPeerWeb getSingleton() 
	{
		if (singleton == null)
		{
			singleton = new HyPeerWeb();
		}
		return singleton;
	}
	
	public int size() {
		return nodes.size();
	}
	
	public Node getNode(int i) {
		if (i >= size())
		{
			return null;
		}
		else
		{
			return nodes.get(i);
		}
	}
	
	public boolean contains(Node node) {
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).equals(node))
			{
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		nodes.clear();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void removeNode(Node node) {
		nodes.remove(node);
	}
	
	public void reload() {
		
	}
	
	public void reload(java.lang.String dbName) {
		
	}
	
	public HyPeerWebDatabase getHyPeerWebDatabase() {
		return database;
	}
	
	public void saveToDatabase() {
		
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Database: ");
		sb.append(database);
		sb.append('\n');
		sb.append("Nodes: ");
		for (int i = 0; i < size(); i++)
		{
			sb.append(nodes.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (!(o instanceof HyPeerWeb))
		{
			return false;
		}
		HyPeerWeb other = (HyPeerWeb) o;
		if (database != other.getHyPeerWebDatabase())
		{
			return false;
		}
		if (nodes.size() != other.size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			if (nodes.get(i) != other.getNode(i))
			{
				return false;
			}
		}
		return true;
	}

	public int hashCode()
	{
		int result = database.hashCode();
		for (int i = 0; i < size(); i++)
		{
			result += nodes.get(i).hashCode();
		}
		return result;
	}
}
