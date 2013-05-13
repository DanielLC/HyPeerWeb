package dbPhase.hypeerweb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Wrapper class for SimplifiedNodeDomain
 * 
 * @author Daniel
 */
public class Node {
	private WebId id;
	private NodeList neighbors;
	private NodeList upPointers;
	private int trueNeighborMask;
	//private List<Node> downPointers;
	private Node fold;
	private boolean isFoldTrue;	//True if it's a true fold, false if it's a surrogate fold.
	//private Node surrogateFold;
	private Node inverseSurrogateFold;
	private NodeState state;

	/*
	 * public static final Node NULL_NODE = new Node() { //Why did it ask for
	 * two curly braces? public Node() { id = WebId.NULL_WEB_ID; neighbors = new
	 * HashSet<Node>(); upPointers = new HashSet<Node>(); downPointers = new
	 * HashSet<Node>(); fold = NULL_NODE; surrogateFold = NULL_NODE;
	 * inverseSurrogateFold = NULL_NODE; } };
	 */

	public static final Node NULL_NODE = new Node(WebId.NULL_WEB_ID);

	// private Node(){};//I seem to need this to make the inner class work.
	private Node(WebId id) {
		this.id = id;
		neighbors = new NodeList();
		upPointers = new NodeList();
		//downPointers = new ArrayList<Node>();
		trueNeighborMask = 0;
		fold = NULL_NODE;
		//surrogateFold = NULL_NODE;
		inverseSurrogateFold = NULL_NODE;
		state = NodeState.StandardNodeState;
	}

	/**
	 * Constructs a node with the given webID. The height is the position of the
	 * most significant bit in the ID.
	 * <p>
	 * The specs said that the height should be based on the position of the
	 * most significant bit of the webID, but in class we were told that it
	 * should be based on the number of neighbors. I went with the specs,
	 * because while the Node(id, height) constructer still doesn't make much
	 * sense, at least it doesn't actually break the code.
	 * 
	 * @param id
	 *            The value of the WebId
	 */
	public Node(int id) {
		this(new WebId(id));
		
		fold = this;
		
		/*
		 * this.id = new WebId(id); neighbors = new HashSet<Node>(); upPointers
		 * = new HashSet<Node>(); downPointers = new HashSet<Node>(); fold =
		 * NULL_NODE; surrogateFold = NULL_NODE; inverseSurrogateFold =
		 * NULL_NODE;
		 */
	}

	/**
	 * Constructs a node with the given webID that has the given value and
	 * height.
	 * 
	 * @param id
	 *            value of the WebId
	 * @param height
	 *            height of the WebId
	 */
	public Node(int id, int height) {
		this(new WebId(id, height));
		/*
		 * this.id = new WebId(id, height); neighbors = new HashSet<Node>();
		 * upPointers = new HashSet<Node>(); downPointers = new HashSet<Node>();
		 * fold = NULL_NODE; surrogateFold = NULL_NODE; inverseSurrogateFold =
		 * NULL_NODE;
		 */
	}

	/**
	 * This gives a SimplifiedNodeDomain, which just gives the values of the
	 * webIDs of the connections rather than the nodes themselves. This makes it
	 * easier to test.
	 * 
	 * @return simplified version of the node
	 */
	public SimplifiedNodeDomain constructSimplifiedNodeDomain() {
		HashSet<Integer> intNeighbors = new HashSet<Integer>();
		HashSet<Integer> intDownPointers = new HashSet<Integer>();
		for(int i=0; i<neighbors.size(); ++i) {
			int neighborId = this.getWebIdValue()^(1 << i);
			if((trueNeighborMask & (1 << i)) != 0) {
				intNeighbors.add(neighborId);
			} else if(neighbors.get(i) != NULL_NODE) {
				intDownPointers.add(neighborId);
			}
		}
		HashSet<Integer> intUpPointers = new HashSet<Integer>();
		if(state == NodeState.UpPointingNodeState) {
			int childMask = (1 << this.getHeight());
			for(int i=0; i<upPointers.size(); ++i) {
				if(upPointers.get(i) != NULL_NODE) {
					intUpPointers.add((this.getWebIdValue()^(1 << i)) | childMask);
				}
			}
		}
		/*for (Node neighbor : neighbors) {
			intNeighbors.add(neighbor.getWebIdValue());
		}
		HashSet<Integer> intUpPointers = new HashSet<Integer>();
		for (Node upPointer : upPointers) {
			intUpPointers.add(upPointer.getWebIdValue());
		}
		for (Node downPointer : downPointers) {
			intDownPointers.add(downPointer.getWebIdValue());
		}*/
		int intFold = 0;
		int intSurrogateFold = 0;
		if(isFoldTrue) {
			intFold = fold.getWebIdValue();
		} else {
			intSurrogateFold = fold.getWebIdValue();
		}
		return new SimplifiedNodeDomain(this.getWebIdValue(), id.getHeight(),
				intNeighbors, intUpPointers, intDownPointers, intFold,
				intSurrogateFold, inverseSurrogateFold.getWebIdValue());
	}

	public WebId getWebId() {
		return id;
	}

	/*
	 * public dbPhase.spec.Connections getConnections(){
	 * 
	 * }
	 */

	public void setWebId(WebId webId) {
		id = webId;
	}

	/*
	 * public void setConnections(dbPhase.spec.Connections connections) {
	 * 
	 * }
	 */
	
	private void addNeighborOrDownPointer(Node neighbor, boolean isTrueNeighbor) {
		int xor = neighbor.getWebIdValue()^this.getWebIdValue();
		int significantBit = Integer.numberOfTrailingZeros(xor);
		/*if(Integer.bitCount(xor) != 1) {
			throw new Exception("Error: WebId " + neighbor.getWebIdValue() +
					" is not a neighbor of WebId " + this.getWebIdValue() + ".");
		}*/
		neighbors.set(significantBit, neighbor);
		if(isTrueNeighbor) {
			trueNeighborMask |= xor;
		} else {
			trueNeighborMask &= ~xor;
		}
	}

	/**
	 * Adds a neighbor. Notably, this does not increment the height. There is no
	 * way to increment the height, because I decided to go with the specs,
	 * where it doesn't change.
	 * 
	 * @param neighbor
	 * @throws Exception 
	 */
	public void addNeighbor(Node neighbor) {
		this.incrementHeight();
		addNeighborOrDownPointer(neighbor, true);
		
		//neighbors.add(neighbor);
		// id = new WebId(id.getValue(), id.getHeight()+1);//???
	}

	/**
	 * Removes the given neighbor if it exists.
	 * Adds the neighbor's parent as a surrogate neighbor if it exists.
	 * 
	 * @param neighbor
	 */
	public void removeNeighbor(Node neighbor) {
		this.decrementHeight();
		int xor = neighbor.getWebIdValue()^this.getWebIdValue();
		/*if(Integer.bitCount(xor) != 1) {
			throw new Exception("Error: WebId " + neighbor.getWebIdValue() +
					" is not a neighbor of WebId " + this.getWebIdValue() + ".");
		}*/
		trueNeighborMask &= ~xor;
		int significantBit = Integer.numberOfTrailingZeros(xor);
		neighbors.set(significantBit, neighbor.getParent());	//If the neighbor has no parent, it sets it to NODE_NULL, which is what we want.
	}

	/**
	 * Adds an up pointer, also known as an inverse surrogate neighbor.
	 * 
	 * @param upPointer
	 */
	public void addUpPointer(Node upPointer) {
		int xor = upPointer.getWebIdValue()^this.getWebIdValue();
		int significantBit = Integer.numberOfTrailingZeros(xor);
		/*xor ^= 1 << this.getHeight();	//Alters the leading bit, so there's only one other altered bit.
		if(Integer.bitCount(xor) != 1) {
			throw new Exception("Error: WebId " + neighbor.getWebIdValue() +
					" is not a neighbor of WebId " + this.getWebIdValue() + ".");
		}*/
		upPointers.set(significantBit, upPointer);
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
		addNeighborOrDownPointer(downPointer, false);
	}

	/**
	 * Removes the given down pointer if it exists. Does nothing if it does not.
	 * 
	 * @param upPointer
	 */
	public void removeDownPointer(Node downPointer) {
		neighbors.remove(downPointer);
	}

	/**
	 * Sets the fold to the given fold. Automatically removes the surrogateFold, if it exists.
	 * 
	 * @param newFold
	 */
	public void setFold(Node newFold) {
		fold = newFold;
		isFoldTrue = true;
	}

	public void setSurrogateFold(Node newSurrogateFold) {
		fold = newSurrogateFold;
		isFoldTrue = false;
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

	/*public HashSet<Node> getNeighbors() {
		return neighbors;
	}

	public HashSet<Node> getUpPointers() {
		return upPointers;
	}

	public HashSet<Node> getDownPointers() {
		return downPointers;
	}*/

	public Node getFold() {
		if(isFoldTrue) {
			return fold;
		} else {
			return NULL_NODE;
		}
	}

	public Node getSurrogateFold() {
		if(isFoldTrue) {
			return NULL_NODE;
		} else {
			return fold;
		}
	}

	public Node getInverseSurrogateFold() {
		return inverseSurrogateFold;
	}
	
	public Node getParent() {
		int mask = 1 << this.getHeight();
		if((this.getWebIdValue() & mask) == 0) {
			return NULL_NODE;
		} else {
			return neighbors.get(Integer.numberOfTrailingZeros(mask));
		}
	}
	
	public Node getChild() {
		int mask = 1 << this.getHeight();
		if((this.getWebIdValue() & mask) != 0) {
			return NULL_NODE;
		} else {
			return neighbors.get(Integer.numberOfTrailingZeros(mask));
		}
	}

	/*public void setNeighbors(HashSet<Node> neighbors) {
		this.neighbors = neighbors;
	}

	public void setUpPointers(HashSet<Node> upPointers) {
		this.upPointers = upPointers;
	}

	public void setDownPointers(HashSet<Node> downPointers) {
		this.downPointers = downPointers;
	}*/
	
	public NodeState getNodeState() {
		return state;
	}
	
	public void addToHyPeerWeb(Node newNode) {
		getInsertionNode().addChild(newNode);
	}
	
	/*public void addToHyPeerWeb() {
		getInsertionNode().addChild();
	}*/
	
	public Node getDeletionNode() {
		if(this.getWebIdValue() == 0) {
			return getDeletionNode(this.getHeight());
		} else {
			return getNode(0).getDeletionNode();
		}
	}
	
	private Node getDeletionNode(int i) {
		if(i < 0) {
			return this;
		}
		int mask = 1 << i;
		if((trueNeighborMask & mask) == 0) {
			return this.getDeletionNode(i-1);
		} else {
			return neighbors.get(i).getDeletionNode(i-1);
		}
	}
	
	public Node getInsertionNode() {
		int insertionId = getDeletionNode().getWebIdValue()+1;
		return getNode(insertionId^(1 << (31-Integer.numberOfLeadingZeros(insertionId))));
	}
	
	public Node getNode(int id) {
		int xor = id^this.getWebIdValue();
		if(xor == 0) {
			return this;
		}
		int bitCount = Integer.bitCount(xor);
		if(bitCount > this.getHeight()/2) {
			return fold.getNode(id);
		}
		int significantBit = Integer.numberOfTrailingZeros(xor);
		if(bitCount == 1) {		//If there's only one one bit
			return neighbors.get(significantBit).getNode(id);
		} else {
			return neighbors.get(significantBit);	//Normally I wouldn't bother with this, and just let it recurse until it returns itself. However, due to the fact that this can involve an internet connection, saving one step is well worth it.
		}
	}

	/*private void addChild() {
		Node child = new Node(this.getWebIdValue() + (2 << this.getHeight()));
		//It's 2 instead of 1 because we havn't incremented the height yet.
		this.addNeighbor(child);
		for(int i=0; i<this.getHeight(); ++i) {
			if(upPointers.get(i) == NULL_NODE) {
				child.addDownPointer(neighbors.get(i));
			} else {
				child.addNeighbor(neighbors.get(i));
			}
		}
		upPointers = new NodeList();
		if(isFoldTrue) {
			child.setFold(fold.getChild());
			child.getFold().setFold(child);
		} else {
			child.setFold(fold);
		}
	}*/
	
	private void addChild(Node child) {
		child.setWebId(new WebId(this.getWebIdValue() + (2 << this.getHeight())));
		//Node child = new Node(this.getWebIdValue() + (2 << this.getHeight()));
		//It's 2 instead of 1 because we havn't incremented the height yet.
		this.addNeighbor(child);
		for(int i=0; i<this.getHeight(); ++i) {
			if(upPointers.get(i) == NULL_NODE) {
				child.addDownPointer(neighbors.get(i));
			} else {
				child.addNeighbor(neighbors.get(i));
			}
		}
		upPointers = new NodeList();
		if(isFoldTrue) {
			child.setFold(fold.getChild());
			child.getFold().setFold(child);
		} else {
			child.setFold(fold);
		}
	}
	
	private void incrementHeight() {
		id = new WebId(id.getValue(), id.getHeight()+1);
	}
	
	private void decrementHeight() {
		id = new WebId(id.getValue(), id.getHeight()-1);
	}
}
