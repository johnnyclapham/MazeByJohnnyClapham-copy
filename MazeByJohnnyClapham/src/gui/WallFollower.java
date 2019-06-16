//Release3.0
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
	protected Controller controller;
	protected CardinalDirection direction;
	protected StatePlaying state;
	protected Distance distance;
	protected int pathLength;

	
	
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
		//PRESS 'b' to initiate the drive2exit (lowercase)
		
		
		this.state = (StatePlaying) controller.states[2];
		this.state.showMaze = true;
		this.state.showSolution = true;
		this.state.mapMode = true;
		
		System.out.printf("-START DRIVING");
		
		while (!robot.isAtExit()) {// while not at an exit
			if (BasicRobot.batteryLevel != 0) {
				// if senses wall on left and forward space, keep going forward
				if (robot.distanceToObstacle(Direction.FORWARD) > 0 && 
						robot.distanceToObstacle(Direction.LEFT) == 0 ) {
					System.out.printf("going FORWARD."+ '\n'+ "battery:"+ 
						BasicRobot.batteryLevel+ '\n');
					robot.move(1, false);
					//Thread.sleep(600);
				}
				else {
					// if space on left, rotate and move forward
					if (robot.distanceToObstacle(Direction.LEFT) > 0) {
				
				
				    System.out.printf("going LEFT."+ '\n'+ "battery:"+ 
				    BasicRobot.batteryLevel+ '\n');
					robot.rotate(Turn.LEFT);
					//state.keyDown(UserInput.Left, 0);
					robot.move(1, false);
				//	Thread.sleep(600);
					}
					
				
					//if space right, turn right and move forward
				else if (robot.distanceToObstacle(Direction.RIGHT) > 0) {
					System.out.printf("going RIGHT."+ '\n'+ "battery:"+ 
				BasicRobot.batteryLevel+ '\n');
					robot.rotate(Turn.RIGHT);
					//state.keyDown(UserInput.Right, 0);
					robot.move(1, false);
				//	Thread.sleep(600);
					
				}
				
					// if something else (all other cases) turn around 
				else {//if (robot.distanceToObstacle(Direction.RIGHT) == 0 && robot.distanceToObstacle(Direction.FORWARD) == 0 &&  robot.distanceToObstacle(Direction.LEFT)==0) {
					System.out.printf("Turning AROUND."+ '\n'+ "battery:"+
				BasicRobot.batteryLevel+ '\n');
					robot.rotate(Turn.AROUND);
					//robot.rotate(Turn.RIGHT);
					//state.keyDown(UserInput.Right, 0);
					//state.keyDown(UserInput.Right, 0);
					robot.move(1, false);
				//	Thread.sleep(600);
					
				}
				}
				
			pathLength++;
			} 
			else {
				return false;
			}
		}
		
		
		

		System.out.println("You won!");
		System.out.println("Game has ended. " + BasicRobot.batteryLevel 
				+ " battery remaining.");
		System.out.println(getEnergyConsumption() + " battery was used.");
		System.out.println("It took:" + pathLength + " moves to reach the exit.");
		System.out.println("Try a higher difficulty! Can you break the robot? If "
				+ "it runs out of battery before "+ "completion, you lose!");
		this.controller.switchFromPlayingToWinning(pathLength);
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


