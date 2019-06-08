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
		mazeMatrix = new int[width][height]; // initialize 2d array with desired dimensions
		
		int filler = 1;// create int filler value that will be filled in to empty cell
		for (int i = 0; i < height; i++) {// loop through maze and assign a unique number (filler) for each cell
			for (int m = 0;  m< width; m++) {
				mazeMatrix[i][m] = filler; // set position equal to filler
				filler++; // increase filler to have different value for each cell
				
			}
		}
		
		final ArrayList<Wall> cellsToAlter = new ArrayList<Wall>();//create array list for number of walls (mutable)
		listTheWalls(cellsToAlter); // fill the list using method that also checks for illegal additions
		
		while(!cellsToAlter.isEmpty()) {
			
			Wall currentWall = getRandomCellToWorkOn(cellsToAlter); // select random wall to work on
			int cellY = currentWall.getY();   //current cell x & y coords
			int cellX = currentWall.getX();  

			int nY = currentWall.getNeighborY();	//neighbor x & y coords
			int nX = currentWall.getNeighborX();   

			if (nY >= 0 && nX >= 0) { // check if neighbor is in bounds of legal moves (no borders)
				if (nX< width && nY< height) {// if so
					if (mazeMatrix[cellX][cellY] != mazeMatrix[nX][nY]) {// check if the chosen cell and its neighbor have different values
						changeNeighbourVal(currentWall, cellY, cellX, nY, nX); // make them the same value
						
					}
				}
			}
		}
	}
	
	
	/*
	 * @param walls
	 */

    private void listTheWalls(ArrayList<Wall> walls){
    	for (int i = 0; i < height; i++) {// loop through maze and assign a unique number (filler) for each cell
			for (int m = 0;  m< width; m++) {
				for (CardinalDirection aa : CardinalDirection.values()) {
					Wall wall = new Wall(i, m, aa);     // attribute new values to wall object at the specified position
					if (cells.canDelete(wall)){        // if wall is valid (not border) add it to our wall list
						walls.add(wall); 
					}
				}
			}
				
				
			}
	}
	
	private void changeNeighbourVal(Wall wall, int cellX, int cellY, int nX, int nY){
	//TODO method that changes the neighbor value to the same as the current cell
	}
	
	private void getRandomCellToWorkOn(ArrayList<Wall> cellsToAlter) {
	//TODO method that chooses a random cell out of our mutable list to work on
	}
	

}

