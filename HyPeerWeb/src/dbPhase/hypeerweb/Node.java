package dbPhase.hypeerweb;

import java.util.HashSet;

public class Node {
	private WebId id;
	private HashSet<Node> neighbors;
	private HashSet<Node> upPointers;
	private HashSet<Node> downPointers;
	private Node fold;
	private Node surrogateFold;
	private Node inverseSurrogateFold;
	
	/*public static final Node NULL_NODE = new Node() {	//Why did it ask for two curly braces?
		public Node() {
			id = WebId.NULL_WEB_ID;
			neighbors = new HashSet<Node>();
			upPointers = new HashSet<Node>();
			downPointers = new HashSet<Node>();
			fold = NULL_NODE;
			surrogateFold = NULL_NODE;
			inverseSurrogateFold = NULL_NODE;
		}
	};*/
	
	public static final Node NULL_NODE = new Node(WebId.NULL_WEB_ID);
	
	//private Node(){};//I seem to need this to make the inner class work.
	
	public Node(WebId id) {	//I'm just using this for the NULL_NODE, but it could be useful.
		this.id = id;
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;
	}
	
	public Node(int id) {
		this.id = new WebId(id);
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;
	}
	
	public Node(int id, int height) {
		this.id = new WebId(id, height);
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;
	}
	
	public SimplifiedNodeDomain constructSimplifiedNodeDomain() {
	    HashSet<Integer> intNeighbors = new HashSet<Integer>();
	    for(Node neighbor : neighbors) {
	    	intNeighbors.add(neighbor.getId());
	    }
	    HashSet<Integer> intUpPointers = new HashSet<Integer>();
	    for(Node upPointer : upPointers) {
	    	intUpPointers.add(upPointer.getId());
	    }
	    HashSet<Integer> intDownPointers = new HashSet<Integer>();
	    for(Node downPointer : downPointers) {
	    	intDownPointers.add(downPointer.getId());
	    }
	    return new SimplifiedNodeDomain(this.getId(), id.getHeight(), intNeighbors, intUpPointers, intDownPointers,
	    		fold.getId(), surrogateFold.getId(), inverseSurrogateFold.getId());
	}
	
	public WebId getWebId() {
		return id;
	}
	
	public dbPhase.spec.Connections getConnections(){
		
	}
	
	public void setWebId(WebId webId) {
		id = webId;
	}
	
	public void setConnections(dbPhase.spec.Connections connections) {
		
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	public void removeNeighbor(Node neighbor) {
		neighbors.remove(neighbor);
	}
	
	public void addUpPointer(Node upPointer) {
		upPointers.add(upPointer);
	}
	
	public void removeUpPointer(Node upPointer) {
		upPointers.remove(upPointer);
	}
	
	public void addDownPointer(Node downPointer) {
		downPointers.add(downPointer);
	}
	
	public void removeDownPointer(Node downPointer) {
		downPointers.remove(downPointer);
	}
	
	public void setFold(Node newFold) {
		fold = newFold;
	}
	
	public void setSurrogateFold(Node newSurrogateFold) {
		surrogateFold = newSurrogateFold;
	}
	
	public void setInverseSurrogateFold(Node newInverseSurrogateFold) {
		inverseSurrogateFold = newInverseSurrogateFold;
	}

	private int getId() {
		return id.getValue();
	}
}
