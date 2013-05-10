package dbPhase.testing;

import org.junit.Test;

import dbPhase.hypeerweb.Node;
import dbPhase.hypeerweb.WebId;

public class NodeTest
{
	private Node node = new Node(0);
	
	@Test
	public void testGetSetWebId() 
	{
		assertNotNull("Checking if the node isn't null.",node.getWebId());
		
		WebId webId = new WebId(1);
		node.setWebId(webId);
		assertSame("Checking if the set webId was stored", webId, node.getWebId());
	}
	
	

}
