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
	private Node(WebId id) {	//I'm just using this for the NULL_NODE, but it could be useful.
		this.id = id;
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;
	}

	/**
	 * Constructs a node with the given webID. The height is the
	 * position of the most significant bit in the ID.
	 * <p>
	 * The specs said that the height should be based on the position of the most
	 * significant bit of the webID, but in class we were told that it should be
	 * based on the number of neighbors. I went with the specs, because while the
	 * Node(id, height) constructer still doesn't make much sense, at least it
	 * doesn't actually break the code.
	 * 
	 * @param id	The value of the WebId
	 */
	public Node(int id) {
		this(new WebId(id));
		
		
		//This seems silly but is required to pass phase 1
		//For phase 2 we will want to call setFold every time we create a new node
		fold = this;
		
		
		/*this.id = new WebId(id);
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;*/
	}

	/**
	 * Constructs a node with the given webID that has the given value and height.
	 * 
	 * @param id		value of the WebId
	 * @param height	height of the WebId
	 */
	public Node(int id, int height) {
		this(new WebId(id, height));
		/*this.id = new WebId(id, height);
		neighbors = new HashSet<Node>();
		upPointers = new HashSet<Node>();
		downPointers = new HashSet<Node>();
		fold = NULL_NODE;
		surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;*/
	}
	
	/**
	 * This gives a SimplifiedNodeDomain, which just gives the values of the
	 * webIDs of the connections rather than the nodes themselves. This makes it
	 * easier to test.
	 * 
	 * @return	simplified version of the node
	 */
	public SimplifiedNodeDomain constructSimplifiedNodeDomain() {
	    HashSet<Integer> intNeighbors = new HashSet<Integer>();
	    for(Node neighbor : neighbors) {
	    	intNeighbors.add(neighbor.getWebIdValue());
	    }
	    HashSet<Integer> intUpPointers = new HashSet<Integer>();
	    for(Node upPointer : upPointers) {
	    	intUpPointers.add(upPointer.getWebIdValue());
	    }
	    HashSet<Integer> intDownPointers = new HashSet<Integer>();
	    for(Node downPointer : downPointers) {
	    	intDownPointers.add(downPointer.getWebIdValue());
	    }
	    return new SimplifiedNodeDomain(this.getWebIdValue(), id.getHeight(), intNeighbors, intUpPointers, intDownPointers,
	    		fold.getWebIdValue(), surrogateFold.getWebIdValue(), inverseSurrogateFold.getWebIdValue());
	}
	
	public WebId getWebId() {
		return id;
	}
	
	/*public dbPhase.spec.Connections getConnections(){
		
	}*/
	
	public void setWebId(WebId webId) {
		id = webId;
	}
	
	/*public void setConnections(dbPhase.spec.Connections connections) {
		
	}*/
	
	/**
	 * Adds a neighbor. Notably, this does not increment the height. There is no
	 * way to increment the height, because I decided to go with the specs, where
	 * it doesn't change.
	 * 
	 * @param neighbor
	 */
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
		//id = new WebId(id.getValue(), id.getHeight()+1);//???
	}
	
	/**
	 * Removes the given neighbor if it exists. Does nothing if it does not.
	 * 
	 * @param neighbor
	 */
	public void removeNeighbor(Node neighbor) {
		neighbors.remove(neighbor);
	}
	
	/**
	 * Adds an up pointer, also known as an inverse surrogate neighbor.
	 * 
	 * @param upPointer
	 */
	public void addUpPointer(Node upPointer) {
		upPointers.add(upPointer);
	}
	
	/**
	 * Removes the given up pointer if it exists. Does nothing if it does not.
	 * 
	 * @param upPointer
	 */
	public void removeUpPointer(Node upPointer) {
		upPointers.remove(upPointer);
	}
	
	/**
	 * Adds a down pointer, also known as an surrogate neighbor.
	 * 
	 * @param downPointer
	 */
	public void addDownPointer(Node downPointer) {
		downPointers.add(downPointer);
	}
	
	/**
	 * Removes the given down pointer if it exists. Does nothing if it does not.
	 * 
	 * @param upPointer
	 */
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

	public int getWebIdValue() {
		return id.getValue();
	}
	
	public int getHeight() {
		return id.getHeight();
	}
	
	public boolean equals(Node node) {
		return id.equals(node.getWebId());
	}
}
