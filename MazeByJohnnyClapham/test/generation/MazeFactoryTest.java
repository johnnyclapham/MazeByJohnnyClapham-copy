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
		mazeFactory.waitTillDelivered();
		configuration = dummyOrder.getConfiguration();
		
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isThereAnExit(){
		int whereTheExit = 10;    // initialize (any number will do)
		Distance mazeDistance = configuration.getMazedists(); // retrieve distance from exit (completion)
		
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
	
	
	
	
	
	
}
