package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class Challenge1 {

	public static void main(String[] args) {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		
		EV3GyroSensor gyroSensor = new EV3GyroSensor((Port)SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor, touchSensor);
		Button.waitForAnyPress();

		// Measure drift 
		if(r.isSensorDrifting())
			return;
		
		// Localize with Bayesian 'strip'
		int n = r.localize();  
		System.out.println(n);
//		Delay.msDelay(6000); // found goal (hopefully)

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		//int n = 20; // stub - the last white square within two lines
		
		//Calculate where the robot center was based on where it finished reading.
		double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
		// startPosition should be about 2 cells behind the colour sensor in current build
		
		//debugging - Prints where center of robot was immediately after localization
		System.out.printf("%.1f , %.1f", startPosition[0], startPosition[1]);
		
		double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
		// calculate diagonal-distance to move based of x coordinate difference
		double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
		r.moveDistance(distToMoveOnDiagonal); 
		
		Grid model = new Grid();
		double[] goalCoords = model.initClosedList1();
		Node goalNode = model.aStarSearch(firstNodePosition, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
		double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        //TODO: find way so always faces goal
        
        Delay.msDelay(3000); // found goal (hopefully)
        
        //turn 180 -- could do easier way
        List<RobotMovement> l = new LinkedList<RobotMovement>();
        l.add( RobotMovement.RIGHT180); 
        r.followInstructions(l, 1, 1);
        
		// Going back to starting point
        list = model.findBackwardPath(goalNode);
        // get direction robot is facing now (below is stub) - should make new method?
        int directionRobotFacingNow = RobotMovement.S; //TODO: find better way to do this
        actionList = RobotMovement.parsePathToMovements(list, directionRobotFacingNow);
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);

	}
	
	private void planToGoal(){
		
	}
	private void planBackToStart(){
		
	}

}
