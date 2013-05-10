package testing;

import static org.junit.Assert.*;

import org.junit.Test;
import dbPhase.hypeerweb.*;

public class HyPeerWebDatabaseTest {
	HyPeerWebDatabase database;

	@Test
	public void test() {
		 database = HyPeerWebDatabase.getSingleton();
		 setUpDatabaseTest();
		 clearTablesTest();
		 
		 getNodeTest();
		 getAllNodesTest();
		 
		 insertNodeTest();
		 
	}

	@Test
	public void getNodeTest() {
		
	}

	@Test
	public void setUpDatabaseTest() {
		database.dropTables();
		database.createTables();
	}

	@Test
	public void getAllNodesTest() {

	}

	@Test
	public void insertNodeTest() {

	}

	@Test
	public void clearTablesTest() {
		
	}
}
