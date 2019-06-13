package gui;
import generation.Cells;
import generation.CardinalDirection;
import generation.MazeConfiguration;
import gui.Constants.StateGUI;

/**
 * @author Johnny Clapham
 *
 */
public class BasicRobot implements Robot{
	
	/*
	 * Class handles the user interaction.
 * It implements a state-dependent behavior that controls the display and reacts to key board input from a user.
 * At this point user keyboard input is first dealt with a key listener (SimpleKeyListener)
 * and then handed over to a MazeController object by way of the keyDown method.
 *
	 */
	public boolean forwardDistSensor;
	public boolean backwardDistSensor;
	public boolean leftwardDistSensor;
	public boolean rightwardDistSensor;
	static int pathLength = 0;
	protected static float batteryLevel;
	protected CardinalDirection direction;
	protected StateGUI statae;
	protected StatePlaying state;
	protected Controller maze;
	protected Cells cellValues;
	protected boolean hasStopped;
	protected int[] currentPosition;
	protected MazeConfiguration mazeConfig;
	

	public BasicRobot() {
		this.forwardDistSensor = true;
		this.backwardDistSensor = true;
		this.leftwardDistSensor = true;
		this.rightwardDistSensor = true;
		
		this.currentPosition = new int[2];
		this.currentPosition[0] = 0;
		this.currentPosition[1] = 0;
		this.hasStopped = false;
		direction = CardinalDirection.East; 
		this.maze = null;
	
		batteryLevel = 2000;
		
	}
	
	/**
	 *makes a rotation (turn) in the desired direction
	 */
	@Override
	public void rotate(Turn turn) {
		
		switch(turn) {
		case RIGHT:
			// check battery, if level is OK proceed
			if (batteryLevel >= 3) {
				direction = direction.rotateClockwise();
				batteryLevel -= 3;
			}
			// no battery to proceed
			else {
				hasStopped = true;
			}
			break;
		
		case LEFT:
			// check battery, if level is OK proceed
			if (batteryLevel>= 3) {
				direction = direction.rotateCounterClockwise();
				batteryLevel -= 3;
			}
			// no battery to proceed
			else {
				hasStopped = true;
			}
			break;
		
		case AROUND:
			// check battery, if level is OK proceed
			if (batteryLevel >= 6) {
				direction = direction.oppositeDirection();
				batteryLevel -= 6;
			}
			// no battery to proceed
			else {
				hasStopped = true;
			}
			break;
	}
}

	@Override
	public void move(int distance, boolean manual) {
		this.currentPosition = maze.getCurrentPosition();
		
		while(distance > 0){
			this.currentPosition = this.state.getCurrentPosition();
			if(batteryLevel >= 4){
				if(manual == true){
					distance = 1;
				}
				if (distanceToObstacle(Direction.FORWARD) > 0) {
				
				// switch to see which direction the robot is facing so it can make appropriate move
				switch(direction) {
				
					case West: // decrease X
						this.currentPosition[0]--;
						break;
					
					case East: // increase X
						this.currentPosition[0]++;
						break;
					
					case North: // decrease Y
						this.currentPosition[1]--;
						break;
					
					case South: // increase Y
						this.currentPosition[1]++;
						break;
				}
				// set new maze properties after move has occurred
				pathLength ++; // increases after every move
				distance--; // counter decrement so loop terminates eventually
				batteryLevel -= 4; // -4 battery level after a move
				this.state.setCurrentPosition(this.currentPosition[0], this.currentPosition[1]);
				
		
				
			}
			else {
				hasStopped = true;
			}
		}
		else {
			hasStopped = true;
			}
		}
	}

		
	

	@Override
	public int[] getCurrentPosition() throws Exception {
		return this.currentPosition;
	}

	@Override
	public void setMaze(Controller controller) {
		this.maze = controller;
		this.cellValues = this.mazeConfig.getMazecells();
		this.currentPosition = this.maze.getCurrentPosition();
		cellValues = new Cells(this.mazeConfig.getWidth(), this.mazeConfig.getHeight());
		cellValues = this.mazeConfig.getMazecells();

		
		int[] whichDirection = new int [2];
		whichDirection = this.direction.getDirection();
		if(whichDirection[0] == 1 && whichDirection[1] == 0){
			this.direction = CardinalDirection.East;
		}
		else if (whichDirection[0] == 0 && whichDirection[1] == -1) {
			this.direction = CardinalDirection.North;
		}
		else if (whichDirection[0] == 0 && whichDirection[1] == 1) {
			this.direction = CardinalDirection.South;
		}
		
		else {
			this.direction = CardinalDirection.West;
		}
	}

	@Override
	public boolean isAtExit() {
		return cellValues.isExitPosition(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		if(this.hasDistanceSensor(direction) == true){
			if(this.distanceToObstacle(direction) == Integer.MAX_VALUE){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		return cellValues.isInRoom(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean hasRoomSensor() {
		return true;
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		return maze.getCurrentDirection();
	}

	@Override
	public float getBatteryLevel() {
		return batteryLevel;
	}

	@Override
	public void setBatteryLevel(float level) {
		batteryLevel = level;
		if(batteryLevel ==0) {
			System.out.printf("Sorry, you lost. Battery is dead!");
		}
		
	}

	@Override
	public int getOdometerReading() {
		return pathLength;
	}

	@Override
	public void resetOdometer() {
		pathLength = 0;
		
	}

	@Override
	public float getEnergyForFullRotation() {
		
		return 12;
	}

	@Override
	public float getEnergyForStepForward() {
		return 4;
	}

	@Override
	public boolean hasStopped() {
		return this.hasStopped;
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		if (this.hasDistanceSensor(direction)) {
			
			
			// -1 battery for checking dist sensor
			this.setBatteryLevel(batteryLevel - 1);
			// retrieve card direction to see if cell has wall
			CardinalDirection currDir;
			if (direction == Direction.RIGHT) {
				currDir = this.direction.rotateClockwise();
			
			}
			else if (direction == Direction.BACKWARD) {
				currDir = this.direction.oppositeDirection();
			}
			else if (direction == Direction.LEFT) {
				currDir = this.direction.rotateCounterClockwise();
				
			}
			else {
				currDir = this.direction;
			}
			
			// make counter for stepcount
			int count = 0;
			int cellX = this.maze.getCurrentPosition()[0];
			int cellY = this.maze.getCurrentPosition()[1];
			//start looping
			while (true) {
				if (cellX < 0 || cellX >= cellValues.width || cellY < 0 || cellY >= cellValues.height) {
					return Integer.MAX_VALUE;
				}
	
				else {
					//switch for various directions
					switch (currDir) {
					
					case East:
						if (this.cellValues.hasWall(cellX, cellY, CardinalDirection.East)) {
							return count;
						}
						cellX++;
						break;
					case West:
						if (this.cellValues.hasWall(cellX, cellY, CardinalDirection.West)) {
							return count;
						}
						cellX--;
						break;
					case North:
						if (this.cellValues.hasWall(cellX, cellY, CardinalDirection.North)) {
							return count;
						}
						cellY--;
						break;
					case South:
						if (this.cellValues.hasWall(cellX, cellY, CardinalDirection.South)) {
							return count;
						}
						cellY++;
						break;
					}
					count++;
				}
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
	}	

	@Override
	public boolean hasDistanceSensor(Direction direction) {
		if (direction == Direction.FORWARD) {
			return this.forwardDistSensor;
		}
		else if (direction == Direction.BACKWARD) {
			return this.backwardDistSensor;
		}
		else if (direction == Direction.LEFT) {
			return this.leftwardDistSensor;
		}
		else {
			return this.rightwardDistSensor;
		}
	
	}

}
