package generation;

import static org.junit.Assert.*;
import generation.Order.Builder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class MazeBuilderKruskalTest extends MazeFactoryTest{
	private MazeFactory mazeFactory;
	private DummyOrder dummyOrder;
	private MazeConfiguration configuration;

	@Before
	public void setUp() {
		boolean det = true;
		dummyOrder = new DummyOrder(1, Builder.Kruskal, det);
		mazeFactory.order(dummyOrder);
		mazeFactory.waitTillDelivered();
		configuration = dummyOrder.getConfiguration();
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
    public void doesKruskalBuild() {
        assertNotNull(dummyOrder);
    }

}
