package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Challenge1 {

	public static void main(String[] args) {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		
		EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		@SuppressWarnings({ "unused", "resource" })
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor);
		Button.waitForAnyPress();

		// Measure drift 
		
		// Localize with Bayesian 'strip'
		//int n = r.localize();  //stubbed out for 16 for the moment 
		//System.out.println(n);

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		Grid model = new Grid();
		int n = 7; // stub  (sensor over 8th cell(position 7), is more or furthest back
		
		
		int  cellOffset = 2; // center of robot is 2 cells behind colour sensor reader.
		double[] startPosition = GridGeo.BayesianCoordinate(n - cellOffset);
		double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
		double distToMove = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
		
		r.moveDistance(distToMove);
		AStarNode goalNode = model.aStarSearch(firstNodePosition);
		
		
        LinkedList<AStarNode> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
		double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        
		// Going back to starting point
        list = model.findBackwardPath(goalNode);
        actionList = RobotMovement.parsePathToMovements(list);
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);

	}

}
