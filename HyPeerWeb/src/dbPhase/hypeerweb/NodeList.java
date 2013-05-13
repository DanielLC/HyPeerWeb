package dbPhase.hypeerweb;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class NodeList extends AbstractList<Node> {
	private List<Node> list;
	
	public NodeList() {
		list = new ArrayList<Node>();
	}

	@Override
	public Node get(int arg0) {
		if(list.size() > arg0) {
			return list.get(arg0);
		} else {
			return Node.NULL_NODE;	//Do I need a singleton for this?
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Node set(int index, Node element) {
		while(list.size() <= index) {
			list.add(Node.NULL_NODE);
		}
		return list.set(index, element);
	}
}
