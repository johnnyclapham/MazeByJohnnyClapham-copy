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
		
		direction = CardinalDirection.East; 
		this.hasStopped = false;
		
		this.forwardDistSensor = true;
		this.backwardDistSensor = true;
		this.leftwardDistSensor = true;
		this.rightwardDistSensor = true;
		
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
	//state.keyDown(UserInput.Left, 0);
	@Override
	public void move(int distance, boolean manual) {
		this.state = (StatePlaying) controller.states[2];
		int[] startPosition = controller.getCurrentPosition();
		System.out.println("Start Position: " + controller.getCurrentPosition()[0] + " " + controller.getCurrentPosition()[1]);
	System.out.println("The move method in BasicRobot is called");
		while (distance > 0 && !hasStopped) {
			state.keyDown(UserInput.Up, 0);
			if (startPosition[0] != controller.getCurrentPosition()[0] || startPosition[1] != controller.getCurrentPosition()[1]) {
				batteryLevel -= 5;
				pathLength++;
			}
			distance--;
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
	public void setMaze(Controller maze) {
		this.controller = maze;
		
		this.cellValues = this.controller.getMazeConfiguration().getMazecells();
		this.currentPosition = this.controller.getCurrentPosition();
		
		int[] directional = new int [2];
		directional = this.direction.getDirection();
		if(directional[0] == 1 && directional[1] == 0){
			this.direction = CardinalDirection.East;
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
			int cellX = this.controller.getCurrentPosition()[0];
			int cellY = this.controller.getCurrentPosition()[1];
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
		}	}	

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
