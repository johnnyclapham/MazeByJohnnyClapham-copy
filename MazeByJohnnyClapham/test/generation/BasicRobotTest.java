package generation;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gui.BasicRobot;
import gui.MazeApplication;
import gui.Robot.Direction;
import generation.SingleRandom;




public class BasicRobotTest {
	BasicRobot robot;
	MazeApplication app;
	
	@Before
	public void setUp() throws Exception{
		SingleRandom.setSeed(17);
		robot = new BasicRobot();
		app = new MazeApplication("Prim"); // Prim for testing scenario
		app.repaint();	
	}

	

	@Test
	public void testGetEnergyForFullRotation() {
		
		assertTrue(12 == robot.getEnergyForFullRotation());
	}

	@Test
	public void testGetEnergyForStepForward() {
		
		assertTrue(robot.getEnergyForStepForward() == 4);
	}
	
	
	@Test
	public void testHasDistanceSensorRight(){

		assertTrue(robot.hasDistanceSensor(Direction.RIGHT));
	}
	
	@Test
	public void testHasRoomSensor(){
		assertTrue(robot.hasRoomSensor());
	}
	
	@Test
	public void testHasDistanceSensorLeft(){

		assertTrue(robot.hasDistanceSensor(Direction.LEFT));
	}
	
	@Test
	public void testHasDistanceSensorForward(){

		assertTrue(robot.hasDistanceSensor(Direction.FORWARD));
	}
	
	@Test
	public void testHasDistanceSensorBackward(){
		assertTrue(robot.hasDistanceSensor(Direction.BACKWARD));
	}
}
