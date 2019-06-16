package gui;
import generation.Cells;
import generation.CardinalDirection;
import generation.MazeConfiguration;
import gui.Constants.StateGUI;
import gui.Constants.UserInput;

/**
 * @author Johnny Clapham
 *
 */
public class BasicRobot implements Robot{
	
	public boolean forwardDistSensor;
	public boolean backwardDistSensor;
	public boolean leftwardDistSensor;
	public boolean rightwardDistSensor;
	static int pathLength = 0;
	protected static float batteryLevel;
	protected CardinalDirection currentDirection;
	protected StateGUI statae;
	protected StatePlaying state;
	protected Controller controller;
	protected Cells cellValues;
	protected boolean hasStopped;
	protected int[] currentPosition;
	protected MazeConfiguration mazeConfig;
	

	public BasicRobot() {
		
		//this.statePlaying = controller.currentState();
	
		batteryLevel = 2000;
		
		this.currentPosition = new int[2];
		this.currentPosition[0] = 0;
		this.currentPosition[1] = 0;
		this.controller = null;
		this.state = null;
		
		currentDirection = CardinalDirection.East; 
		hasStopped = false;
		
		forwardDistSensor = true;
		backwardDistSensor = true;
		leftwardDistSensor = true;
		rightwardDistSensor = true;
		
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
				currentDirection = currentDirection.rotateClockwise();
				controller.keyDown(UserInput.Right, 0);
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
				currentDirection = currentDirection.rotateCounterClockwise();
				controller.keyDown(UserInput.Left, 0);
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
				currentDirection = currentDirection.oppositeDirection();
				controller.keyDown(UserInput.Left, 0);
				controller.keyDown(UserInput.Left, 0);
				batteryLevel -= 6;
			}
			// no battery to proceed
			else {
				hasStopped = true;
			}
			break;
	}
}
	//state.keyDown(UserInput.Left, 0);
	
	
	
	@Override
	public void move(int distance, boolean manual) {
		while (distance > 0) {
			this.currentPosition = this.controller.getCurrentPosition();
			
			
			if (batteryLevel >= 5) { // if enough battery, proceed
				if (manual == true) {
					distance = 1;
				}
				if (distanceToObstacle(Direction.FORWARD) > 0) {
					
					// switch for current direction so that you can make adjustments to position as necessary
					switch(currentDirection) {
					
						case West: // -x direction decreases x
							this.currentPosition[0]--;
							batteryLevel = batteryLevel -5;
							//controller.keyDown(UserInput.Up, 0);
							break;
						
						case East: //+x direction increases x
							this.currentPosition[0]++;
							batteryLevel = batteryLevel -5;
							//controller.keyDown(UserInput.Up, 0);
							break;
						
						case North: // -y direction decreases y
							this.currentPosition[1]--;
							batteryLevel = batteryLevel -5;
							//controller.keyDown(UserInput.Up, 0);
							break;
						
						case South: // +y direction increases y
							this.currentPosition[1]++;
							batteryLevel = batteryLevel -5;
							//controller.keyDown(UserInput.Up, 0);
							break;
					}
					// make robot camera position match the new maze positions by calling the 'Up' user 
					//input to move forward
					controller.keyDown(UserInput.Up, 0);
					batteryLevel -= 5; // move cost = 5
					pathLength ++;
					distance--; // counter for loop termination
					
				}
				else {
					hasStopped = true;
					
				}
			}
			else {
				hasStopped = true;
				exitScreen();
			}
		}
	}
		
	

	@Override
	public int[] getCurrentPosition() throws Exception {
		return this.currentPosition;
	}

	
	
	
	/**
	 * Provides the robot with a reference to the controller to cooperate with.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * @param controller is the communication partner for robot
	 * @precondition controller != null, controller is in playing state and has a maze
	 */
	@Override
	public void setMaze(Controller controller) {
		//establish maze properties and pull configuration so it can be accessed later
		this.controller = controller;
		
		this.cellValues = this.controller.getMazeConfiguration().getMazecells();
		this.currentPosition = this.controller.getCurrentPosition();
		
		int[] whichDir = new int [2];
		whichDir = currentDirection.getDirection();
		
			if (whichDir[0] == 0 && whichDir[1] == -1) {
				currentDirection = CardinalDirection.North;
			}
			
			else if (whichDir[0] == 1 && whichDir[1] == 0) {
				currentDirection = CardinalDirection.East;
			}
			
			else if (whichDir[0] == 0 && whichDir[1] == 1) {
				currentDirection = CardinalDirection.South;
			}
			
			else {
				currentDirection = CardinalDirection.West;
			}
		}

	@Override
	public boolean isAtExit() {
		currentPosition = this.controller.getCurrentPosition();
		    if(cellValues.isExitPosition(currentPosition[0], currentPosition[1])) {
		    	hasStopped = true;
		    }
		    return cellValues.isExitPosition(currentPosition[0], currentPosition[1]);
		}
	

	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		assert hasDistanceSensor(direction);
		if (!hasDistanceSensor(direction)) {
			throw new UnsupportedOperationException();
		}
		if (distanceToObstacle(direction) != Integer.MAX_VALUE) {
				return false;
		}	
		else {
				return true;
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
		return controller.getCurrentDirection();
	}

	@Override
	public float getBatteryLevel() {
		return batteryLevel;
	}

	@Override
	public void setBatteryLevel(float level) {
		batteryLevel = level;
		if(batteryLevel ==0) {
			System.out.printf('\n' + "Battery dies! You lose.");
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
	public int distanceToObstacle(Direction direction) throws 
								UnsupportedOperationException {
		
		assert hasDistanceSensor(direction);
		
		
		if(!hasDistanceSensor(direction)) {
				throw new UnsupportedOperationException();
		}
			int countedMoves = 0;
			currentPosition = controller.getCurrentPosition();
			int cellXcurrent = controller.getCurrentPosition()[0];
			int cellYcurrent = controller.getCurrentPosition()[1];
			
			batteryLevel = batteryLevel - 1;
			
			CardinalDirection SensDir;
			SensDir = getCurrentDirection();
			
			switch(direction) {
			case FORWARD:
				SensDir = getCurrentDirection();
				break;	
			
			case RIGHT:
				SensDir = getCurrentDirection().rotateCounterClockwise();
				break;
				
			case LEFT:
				SensDir = getCurrentDirection().rotateClockwise();
				break;
				
			case BACKWARD:
				SensDir = getCurrentDirection().oppositeDirection();
				break;
				
			}

			int mazeWidth = cellValues.width;
			int mazeHeight = cellValues.height;
			
			while(true) {
				if (cellXcurrent>= mazeWidth || cellYcurrent>=mazeHeight 
						|| cellXcurrent < 0 || cellYcurrent<0){
					return Integer.MAX_VALUE;	
				}
				
				else {
				switch(SensDir){
				
				case North:
					if(cellValues.hasWall(cellXcurrent, cellYcurrent,
							CardinalDirection.North)){
						return countedMoves;
					}
					cellYcurrent--;
					break;	
					
				case East:
					
					if(cellValues.hasWall(cellXcurrent, cellYcurrent,
							CardinalDirection.East)){
						return countedMoves;
					}
					cellXcurrent++;
					break;
					
				case South:
					if(cellValues.hasWall(cellXcurrent, cellYcurrent,
							CardinalDirection.South)){
						return countedMoves;
					}
					cellYcurrent++;
					break;
					
				case West:
					if(cellValues.hasWall(cellXcurrent, cellYcurrent,
							CardinalDirection.West)){
						return countedMoves;
					}
					cellXcurrent--;
					break;
					
				
					
				

				}
				countedMoves++;

			}	
		}		
	}

	@Override
	public boolean hasDistanceSensor(Direction direction) {
		if (direction == Direction.FORWARD) {
			return forwardDistSensor;
		}
		else if (direction == Direction.BACKWARD) {
			return backwardDistSensor;
		}
		else if (direction == Direction.LEFT) {
			return leftwardDistSensor;
		}
		else {
			return rightwardDistSensor;
		}
	
	}
	
	public static int getPathLength() {
		return pathLength;
		
	}
	
	public void exitScreen() {
		if(hasStopped()) {
			this.controller.switchToTitle();
			System.out.println("Game Over. Battery depleated.");
		}
		else {
			return;
		}
		
	}

}
