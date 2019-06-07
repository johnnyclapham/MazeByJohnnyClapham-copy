package generation;


import static org.junit.Assert.*;
import gui.Constants;
import generation.Order.Builder;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class MazeFactoryTest {

	
	// initialize test variables
	private MazeFactory mazeFactory;
	// can be fetched and compared.
	private MazeConfiguration configuration;
	// Stub object created for testing purposes
	private DummyOrder dummyOrder;
	
	@Before
	public void setUp() throws Exception {
		boolean det = true;
		mazeFactory = new MazeFactory(det);
		dummyOrder = new DummyOrder(1, Builder.DFS, det);
		mazeFactory.order(dummyOrder);
		
		/*
		 * "the MazeFactory operates a background thread
			such that you need to make the test wait for the termination of the MazeBuilder
			thread before the test proceeds."
		 */
		mazeFactory.waitTillDelivered();
		
		configuration = dummyOrder.getConfiguration();
		
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isThereAnExit(){
		int whereTheExit = 10;    // initialize (any number will do)
		Distance mazeDistance = configuration.getMazedists(); // retrieve distanc
		
		//nested for loop to navigate through maze matrix
		for (int k = 0; k < configuration.getWidth(); k++) {
			for (int m = 0; m < configuration.getHeight(); m++) {
				int distance = mazeDistance.getDistanceValue(k, m); // adjust distance value for each iteration
				if (distance == 1){  // distance ==1 signifies we have reached an exit
					whereTheExit ++; // ad 1 to our "whereTheExit" int to see if we are correct		
				}
			}
			
		}
		assertEquals(11,whereTheExit); // 10(arbitrary #) + 1 (exit) = 11
	}
	
	@Test 
	public void dimensionChecker() {
		int skillLevel = dummyOrder.getSkillLevel();
		int x = Constants.SKILL_X[skillLevel];//dimension based on skill level
		int y = Constants.SKILL_Y[skillLevel];// --
		int width = configuration.getWidth();//dimension of the mazegame created
		int height = configuration.getHeight();// -- 
		
		
		//check if the actual vs. theoretical dimension match
		assertEquals(x, width);
		assertEquals(y, height);
		
	}
	
	@Test
	public void isThereAnExitPath(){
		
		//Distance mazeDistance = configuration.getMazedists(); // retrieve distance
		int width = configuration.getWidth();
		int height = configuration.getHeight();
		
		//create 3 variables to act as tools in ending the test as either pass or fail
		boolean answer = true;
		boolean wrong = false;
		boolean correct = true;
		
		int low = 1; // shortest pathlength	
		int high = width * height; // longest path length
		
		//nested for loop to navigate through maze matrix (same as before)
		for (int k = 0; k < configuration.getWidth(); k++) {
			for (int m = 0; m < configuration.getHeight(); m++) {
				int distanceToExit = configuration.getDistanceToExit(k, m); // get path length to exit
				if (distanceToExit > high){  // if distance is over what we set as high threshold, it fails the test
					assertTrue(wrong);
				}
				else if (distanceToExit < low ){// if distance is lower what we set as low threshold, it fails the test
					assertTrue(wrong);
				}
				else { // if neither failures occur, it passes the test
					assertTrue(correct);
			}
			
		}
		

	}
	
	
	
	}
	
}
