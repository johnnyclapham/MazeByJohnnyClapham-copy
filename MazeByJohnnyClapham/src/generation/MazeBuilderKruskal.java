package generation;
import generation.CardinalDirection;
import java.util.ArrayList;

/*
 * @author Johnny Clapham
 */

public class MazeBuilderKruskal extends MazeBuilder implements Runnable {
	
	private int[][] mazeMatrix;
	
	public MazeBuilderKruskal() {
		super();
		System.out.println("MazeBuilderPrim uses Kruskal's algorithm to generate maze.");
	}
	
	public MazeBuilderKruskal(boolean det) {
		super();
		System.out.println("MazeBuilderPrim uses Kruskal's algorithm to generate maze.");
	}
	
	
	protected void generatePathways() {
		// initialize 2d array with desired dimensions
		
		// create int filler value that will be filled in to empty cell
		// loop through maze and assign a unique number (filler) for each cell
		
				 // set position equal to filler
				 // increase filler to have different value for each cell
		
		//create array list for number of walls (mutable)
		// fill the list using method that also checks for illegal additions
		
		 //while we still have cells to alter
			
		// select random wall to work on
		//current cell x & y coords
		
			//neighbor x & y coords
			  
			// check if neighbor is in bounds of legal moves (no borders)
				// if so
					// check if the chosen cell and its neighbor have different values
						// make them the same value
						
		}
				
	
	
	/*
	 * @param walls
	 */

    private void listTheWalls(ArrayList<Wall> walls){
    	// loop through maze and assign a unique number (filler) for each cell
			
					  // attribute new values to wall object at the specified position
					  // if wall is valid (not border) add it to our wall list
					
				
	}
	
	private void changeNeighbourVal(Wall wall, int cellX, int cellY, int nX, int nY){
	//TODO method that changes the neighbor value to the same as the current cell
	}
	
	private void getRandomCellToWorkOn(ArrayList<Wall> cellsToAlter) {
	//TODO method that chooses a random cell out of our mutable list to work on
	}
	

}

