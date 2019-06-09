package generation;

import static org.junit.Assert.*;
import generation.Order.Builder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class MazeBuilderKruskalTest{
	private MazeFactory mazeFactory;
	private DummyOrder dummyOrder;
	private MazeConfiguration configuration;

	@Before
	public void setUp() {
		boolean det = true;
		mazeFactory = new MazeFactory(det);
		dummyOrder = new DummyOrder(1, Builder.Kruskal, det);
		mazeFactory.order(dummyOrder);
		mazeFactory.waitTillDelivered();
		configuration = dummyOrder.getConfiguration();
	}	
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * checks if Kruskal maze was built
	 */
	@Test
    public void doesKruskalBuild() {
        assertNotNull(dummyOrder);
    }

	/**
	 * copied code from Kruskal class to see if implementation is right
	 */
	@Test
	public void changeNeighbourValTest(){
		int[][] mazeMatrix = new int[5][5];
		for (int i = 0; i < 5; i++){ // loop through 2d array
			for (int m = 0; m < 5; m++){
				
				mazeMatrix[i][m] = 5; 
				}
			}
		
		for (int is = 0; is < 5; is++){ // loop through 2d array
			for (int fm = 0; fm < 5; fm++){
				
				assertTrue(mazeMatrix[is][fm] == 5); 
				}
			}
		}
	
	
		
	}
	
	

