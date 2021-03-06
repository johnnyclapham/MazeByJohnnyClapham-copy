/**
 * 
 */
package gui;

import generation.Order;

import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;


/**
 * This class is a wrapper class to startup the Maze game as a Java application
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 * 
 * TODO: use logger for output instead of Sys.out
 */
public class MazeApplication extends JFrame {

	// not used, just to make the compiler, static code checker happy
	private static final long serialVersionUID = 1L;

	private KeyListener kl ;
	private Controller controller ;
	private RobotDriver robotDriver;
	private BasicRobot robot;

	/**
	 * Constructor
	 */
	public MazeApplication() {
		super() ;
		System.out.println("MazeApplication: maze will be generated with a randomized algorithm.");
		controller = new Controller() ;
		init() ;
	}
	
		
	/**
	 * Constructor that loads a maze from a given file or uses a particular method to generate a maze
	 * @param parameter can identify a generation method (Prim, Kruskal, Eller)
     * or a filename that stores an already generated maze that is then loaded, or can be null
	 */
	public MazeApplication(String parameter) {
		super() ;
		// scan parameters
		
		// Case 1: Prim
		if ("Prim".equalsIgnoreCase(parameter))
		{
			System.out.println("Generating Maze from Prim.");
			controller = createController("Prim");
			init() ;
			return ;
		}
		
		// Case 2: Kruskal
		if ("Kruskal".equalsIgnoreCase(parameter))
		{
			System.out.println("Generating Maze from Kruskal.");
			controller = createController("Kruskal");
			init();
			return;
		}
		
		// Case 3: a file
		File f = new File(parameter) ;
		if (f.exists() && f.canRead())
		{
			System.out.println("Generating maze from file: " + parameter);
	
			controller = createController(parameter);
			init();
			return ;
		}
		
		// Default case: 
		System.out.println("Default Mode.");
		controller = new Controller();
		init() ;
	}

	

	public MazeApplication(String builder, String driver) 
	{
		if ("Prim".equalsIgnoreCase(builder)) {
			System.out.println("Generating Maze from Prim.");
			controller = createController("Prim");
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("Running Wallfollower Robot");
				this.robotDriver = new WallFollower();
			
				this.robot = new BasicRobot();
				
				this.robotDriver.setRobot(this.robot);
				
				controller.setRobotAndDriver(this.robot, this.robotDriver);
			}
			init();
			return;
		}
		
		if ("Kruskal".equalsIgnoreCase(builder)) {
			System.out.println("Generating Maze from Kruskal.");
			controller = createController("Kruskal");
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("Running Wallfollower Robot");
				this.robotDriver = new WallFollower();
				
				this.robot = new BasicRobot();
				
				this.robotDriver.setRobot(this.robot);
				
				controller.setRobotAndDriver(this.robot, this.robotDriver);
			}
			init();
			return;
		}
		
		File f = new File(builder) ;
		if (f.exists() && f.canRead())
		{
			System.out.println("Generating Maze from file: " + builder);
			controller = createController(builder);
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("Running Wallfollower Robot");
				this.robotDriver = new WallFollower();
				
				this.robot = new BasicRobot();
				
				this.robotDriver.setRobot(this.robot);
				
				controller.setRobotAndDriver(this.robot, this.robotDriver);
			}
			init();
			return ;
		}
	}
	
	
	
	
	
	/**
	 * Instantiates a controller with settings according to the given parameter.
	 * @param parameter can identify a generation method (Prim, Kruskal, Eller)
	 * or a filename that contains a generated maze that is then loaded,
	 * or can be null
	 * @return the newly instantiated and configured controller
	 */
	 Controller createController(String parameter) {
	    // need to instantiate a controller to return as a result in any case
	    Controller result = new Controller() ;
	    String msg = null; // message for feedback
	    // Case 1: no input
	    if (parameter == null) {
	        msg = "MazeApplication: maze will be generated with a randomized algorithm."; 
	    }
	    // Case 2: Prim
	    else if ("Prim".equalsIgnoreCase(parameter))
	    {
	        msg = "MazeApplication: generating random maze with Prim's algorithm.";
	        result.setBuilder(Order.Builder.Prim);
	    }
	    // Case 3 a and b: Eller, Kruskal or some other generation algorithm
	    else if ("Kruskal".equalsIgnoreCase(parameter))
	    {
	    	msg = "MazeApplication: generating random maze with Kruskal's algorithm. Implementation by Johnny Clapham";
	    	result.setBuilder(Order.Builder.Kruskal);
	    }
	    else if ("Eller".equalsIgnoreCase(parameter))
	    {
	    	// TODO: for P2 assignment, please add code to set the builder accordingly
	        throw new RuntimeException("Don't know anybody named Eller ...");
	    }
	    // Case 4: a file
	    else {
	        File f = new File(parameter) ;
	        if (f.exists() && f.canRead())
	        {
	            msg = "MazeApplication: loading maze from file: " + parameter;
	            result.setFileName(parameter);
	            return result;
	        }
	        else {
	            // None of the predefined strings and not a filename either: 
	            msg = "MazeApplication: unknown parameter value: " + parameter + " ignored, operating in default mode.";
	        }
	    }
	    // controller instanted and attributes set according to given input parameter
	    // output message and return controller
	    System.out.println(msg);
	    return result;
	}


	 
	 
	/**
	 * Initializes some internals and puts the game on display.
	 * @param parameter can identify a generation method (Prim, Kruskal, Eller)
     * or a filename that contains a generated maze that is then loaded, or can be null
	 */
	private void init() {
	    // instantiate a game controller and add it to the JFrame
	    //Controller controller = createController(parameter);
		add(controller.getPanel()) ;
		// instantiate a key listener that feeds keyboard input into the controller
		// and add it to the JFrame
		KeyListener kl = new SimpleKeyListener(this, controller) ;
		addKeyListener(kl) ;
		// set the frame to a fixed size for its width and height and put it on display
		setSize(400, 400) ;
		setVisible(true) ;
		// focus should be on the JFrame of the MazeApplication and not on the maze panel
		// such that the SimpleKeyListener kl is used
		setFocusable(true) ;
		// start the game, hand over control to the game controller
		controller.start();
	//	WallFollower.drive2Exit();
	}
	
	/**
	 * Main method to launch Maze game as a java application.
	 * The application can be operated in three ways. 
	 * 1) The intended normal operation is to provide no parameters
	 * and the maze will be generated by a randomized DFS algorithm (default). 
	 * 2) If a filename is given that contains a maze stored in xml format. 
	 * The maze will be loaded from that file. 
	 * This option is useful during development to test with a particular maze.
	 * 3) A predefined constant string is given to select a maze
	 * generation algorithm, currently supported is "Prim".
	 * @param args is optional, first string can be a fixed constant like Prim or
	 * the name of a file that stores a maze in XML format
	 */
	public static void main(String[] args) {
	    JFrame app ; 
	//    MazeApplication app ; 
		switch (args.length) {
		case 4: 
			app = new MazeApplication(args[1], args[3]);
			break;
		case 3: 
			app = new MazeApplication(args[0], args[2]);
			break;
		case 2 :
			app = new MazeApplication(args[1]);
			break ;
		case 1: 
			app = new MazeApplication(args[0]);
			break;
		case 0 : 
		default : 
			app = new MazeApplication() ;
			break ;
		}
		app.repaint() ;
	}

}
