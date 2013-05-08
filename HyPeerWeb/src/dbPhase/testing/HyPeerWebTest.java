package dbPhase.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import dbPhase.hypeerweb.HyPeerWeb;
import dbPhase.hypeerweb.Node;

public class HyPeerWebTest 
{

	private HyPeerWeb hyPeerWeb;
	
	@Test
	public void testHyPeerWebSetUp() 
	{
		hyPeerWeb = HyPeerWeb.getSingleton();
		
		assertNotNull("getSingleton() used to initialize a HyPeerWeb cannot return null.", hyPeerWeb);
		assertNotNull("getHyPeerWebDatabase() cannot return null.", hyPeerWeb.getHyPeerWebDatabase());
	}

	@Test
	public void testAddRemoveNode() 
	{
		Node newNode = new Node(0);
		
		hyPeerWeb.addNode(newNode);
		assertTrue("Checking if an added node is contianed in the HyPeerWeb.", hyPeerWeb.contains(newNode));
		assertEquals("Checking if size of HyperWeb was incremented  to 1.", 1, hyPeerWeb.size());
		
		hyPeerWeb.removeNode(newNode);
		assertFalse("Checking if removed node is not contained in the HyPeerWeb.", hyPeerWeb.contains(newNode));
		assertEquals("Checking if size of HyperWeb was decremented to 0.", 0, hyPeerWeb.size());
	}
	
	@Test
	public void testClearNodes() 
	{
		Node newNode1 = new Node(0);
		Node newNode2 = new Node(1);
		
		hyPeerWeb.addNode(newNode1);
		hyPeerWeb.addNode(newNode2);
		
		hyPeerWeb.clear();
		assertFalse("Checking if cleared node1 is not contained in the HyPeerWeb.", hyPeerWeb.contains(newNode1));
		assertFalse("Checking if cleared node2 is not contained in the HyPeerWeb.", hyPeerWeb.contains(newNode2));
		assertEquals("Checking if size of HyperWeb was set to 0.", 0, hyPeerWeb.size());
	}
	
	@Test
	public void testGetNode() 
	{
		Node newNode = new Node(0);
		
		hyPeerWeb.addNode(newNode);
		assertNotNull("Node asked for cannot be null.", hyPeerWeb.getNode(0));
	}
}
