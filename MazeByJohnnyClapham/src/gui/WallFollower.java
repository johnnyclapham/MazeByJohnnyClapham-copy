package gui;
import generation.Order;
import generation.Distance;
import generation.CardinalDirection;
import gui.Robot.Direction;
import gui.Robot.Turn;
import gui.Constants.UserInput;


public class WallFollower implements RobotDriver {
	protected int[][] cellValues;
	protected BasicRobot robot;
	protected Distance distance;
	protected int pathLength;
	protected Controller controller;
	protected CardinalDirection direction;
	protected StatePlaying state;
	
	
	public WallFollower() {
		this.cellValues = null;
		this.robot = null;
		this.distance = null;
		this.pathLength = 0;
		this.controller = new Controller();
		this.state = new StatePlaying();
	}
	
	public WallFollower(Order.Builder builder) {
		this.cellValues = null;
		this.robot = null;
		this.distance = null;
		this.pathLength = 0;
		this.controller = new Controller(builder);
		//this.state = new StatePlaying();
	}
	
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
		//robot.setMaze(this.controller); // Gives a nullpointer-- taken out
	}
	@Override
	public void setDimensions(int width, int height) {
		this.cellValues = new int[width][height];
		
	}
	@Override
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	@Override
	public boolean drive2Exit() throws Exception {
		this.state = (StatePlaying) controller.states[2];
		this.state.showMaze = true;
		this.state.showSolution = true;
		this.state.mapMode = true;
		
		System.out.printf("-drive starts");
		
		while (!robot.isAtExit()) {
			if (BasicRobot.batteryLevel != 0) {
				if (robot.distanceToObstacle(Direction.FORWARD) > 0) {
					System.out.printf("going FORWARD");
					robot.move(1, false);
					Thread.sleep(100);
				}
				else if (robot.distanceToObstacle(Direction.LEFT) > 0) {
					    System.out.printf("going LEFT");
						robot.rotate(Turn.LEFT);
						state.keyDown(UserInput.Left, 0);
						robot.move(1, false);
					
					}
					else if (robot.distanceToObstacle(Direction.RIGHT) > 0) {
						System.out.printf("going RIGHT");
						robot.rotate(Turn.RIGHT);
						state.keyDown(UserInput.Right, 0);
						robot.move(1, false);
						Thread.sleep(100);
						
					}
					else {
						System.out.printf("going AROUND");
						robot.rotate(Turn.AROUND);
						state.keyDown(UserInput.Right, 0);
						state.keyDown(UserInput.Right, 0);
						robot.move(1, false);
						Thread.sleep(100);
					}
				
				this.pathLength++;
			}
			else {
				return false;
			}
		}
		
		
		// if robot reaches one of the following conditions (sees exit L, R, B, F) 
		// FINISH GAME
		
		if (robot.canSeeExit(Direction.LEFT)) {
			robot.statae = Constants.StateGUI.STATE_FINISH;
		
		}
		else if (robot.canSeeExit(Direction.RIGHT)) {
			robot.statae = Constants.StateGUI.STATE_FINISH;
			
		}
		else if (robot.canSeeExit(Direction.BACKWARD)) {
			robot.statae = Constants.StateGUI.STATE_FINISH;
		
		}
		else if (robot.canSeeExit(Direction.FORWARD)) {
			robot.statae = Constants.StateGUI.STATE_FINISH;
	
		}
		BasicRobot.batteryLevel = 2000;
		BasicRobot.pathLength = 0;
		
		System.out.println("Game has ended. " + BasicRobot.batteryLevel + "battery remaining.");
		System.out.println(getEnergyConsumption() + " battery was used.");
		System.out.println("Goodbye!");
		return true;
	}
	
	
	@Override
	public float getEnergyConsumption() {
		return 2000 - BasicRobot.batteryLevel;
	}
	@Override
	public int getPathLength() {
		return this.pathLength;
	}
	

}


