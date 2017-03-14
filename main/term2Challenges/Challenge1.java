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
		Delay.msDelay(3000); // found goal (hopefully)

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		Grid model = new Grid();
		//int n = 7; // stub  (sensor over 8th cell(position 7), is farthest back possible
		
		
		int  cellOffset = 2; // center of robot is 2 cells behind colour sensor reader.
		double[] startPosition = GridGeo.BayesianCoordinate(n - cellOffset);
		double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
		double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
		
		r.moveDistance(distToMoveOnDiagonal);
		Node goalNode = model.aStarSearch(firstNodePosition);
		
		
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

}
