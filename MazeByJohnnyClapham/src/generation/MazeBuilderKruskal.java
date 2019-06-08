package generation;
import generation.CardinalDirection;
import java.util.ArrayList;

/*
  * This class has the responsibility to create a maze of given dimensions (width, height) 
 * together with a solution based on a distance matrix.
 * The MazeBuilder implements Runnable such that it can be run a separate thread.
 * The MazeFactory has a MazeBuilder and handles the thread management.   
 * The maze is built with a randomized version of Kruskal's algorithm. 
 * Algorithm leaves walls in tact that carry the border flag.
 * Borders are used to keep the outside surrounding of the maze enclosed and 
 * to make sure that rooms retain outside walls and do not end up as open stalls. 
 *   

 */

public class MazeBuilderKruskal extends MazeBuilder implements Runnable {
	
	protected int[][] mazeMatrix;
	
	public MazeBuilderKruskal() {
		super();
		System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
	}
	
	public MazeBuilderKruskal(boolean det) {
		super();
		System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
	}
	
	
	
	protected void generatePathways() {
		mazeMatrix = generateMazeMatrix(); // initialize 2d array with desired dimensions
		
		
		final ArrayList<Wall> cellsToAlter = new ArrayList<Wall>();//create array list for number of walls (mutable to account for skill level)
		listTheWalls(cellsToAlter); // fill the list using method that also checks for illegal additions
		
		while(!cellsToAlter.isEmpty()) {
			
			Wall currentWall = getRandomCellToWorkOn(cellsToAlter); // select random wall to work on
			int cellY = currentWall.getY();   //current cell x & y coords
			int cellX = currentWall.getX();  

			int nY = currentWall.getNeighborY();	//neighbor x & y coords
			int nX = currentWall.getNeighborX();   

			if (twoCellsDifferentValues( cellX, nX, cellY, nY)){ 
				cells.deleteWall(currentWall);
				changeNeighbourVal(cellX, nX, cellY, nY); 
			}
		}
	}
	
	private int[][] generateMazeMatrix(){
		mazeMatrix = new int[width][height]; //new 2d array	
		int filler = 0; // value to be assigned is initialized at 0
		for (int x = 0; x < width; x++){ // loop through 2d array
			for(int y = 0; y < height; y++){
				mazeMatrix[x][y] = filler; // assign the found cell to the filler value
				filler++; // increase filler to get unique number for each cell
			}
		}return mazeMatrix;
	}
	
	private void changeNeighbourVal(int cellX, int nX, int cellY, int nY){
		int currCell = mazeMatrix[cellX][cellY];// find the current cell
		int nCell = mazeMatrix[nX][nY]; // find the NEIGHBOR cell
		for (int i = 0; i < width; i++){ // loop through 2d array
			for (int m = 0; m < height; m++){
				if (mazeMatrix[i][m]== nCell){ // if a value is the same as currCell
					mazeMatrix[i][m] = currCell; 
				}
			}
		}
		mazeMatrix[nX][nY] = mazeMatrix[cellX][cellY]; // update neighbor to same value 
	}
	
	private boolean twoCellsDifferentValues(int cellX, int nX, int cellY, int nY){
		if(mazeMatrix[cellX][cellY] == mazeMatrix[nX][nY]){
			return false;
		}return true;
}
	
	
	

    private void listTheWalls(ArrayList<Wall> walls){
    	for (int i = 0; i < width; i++) {// loop through maze and assign a unique number (filler) for each cell
			for (int m = 0;  m< height; m++) {
				for (CardinalDirection currentDirection : CardinalDirection.values()) {
					Wall wall = new Wall(i, m, currentDirection);     // attribute new values to wall object at the specified position
					if (cells.canDelete(wall)){        // if wall is valid (not border) add it to our wall list
						walls.add(wall); 
					}
				}
			}
				
				
			}
	}
	

	


	private Wall getRandomCellToWorkOn(ArrayList<Wall> cellsToAlter) {
		// fetch a random cell to work on
		return cellsToAlter.remove(random.nextIntWithinInterval(0, cellsToAlter.size()-1)); 
	}
	

}

