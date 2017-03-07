package main.term2Challenges.errorAnalysis;

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
import main.term2Challenges.AStarNode;
import main.term2Challenges.Grid;
import main.term2Challenges.Robot;
import main.term2Challenges.RobotMovement;
import tests.aStarTest.HandMadeOptimalPaths;

public class PathMotionTesting {

	public static void main(String[] args) {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		
		EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor);
		Button.waitForAnyPress();

		// Measure drift 
		
		// Localize with Bayesian 'strip'
//		System.out.println(r.localize());
		
		//TODO: measurement from r.localize()  + how to convert

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		Grid model = new Grid();
//		AStarNode goalNode = model.findGoalNodeFromRoot(32, 32);
//        LinkedList<AStarNode> list = model.getListPathFromGoalNode(goalNode);
//        List<RobotMovement> actionList = model.calculatePath(list);

		double nodeSize = model.getNodeSize();
		double nodeDiagonal = Math.sqrt(nodeSize * nodeSize + nodeSize * nodeSize);
		
		//TODO: forgotten what nodeDiagonal 'is'.  Why is it in the interface
		
		
		//tests moves forward 60cm
		//List<RobotMovement> egActions = HandMadeOptimalPaths.testPath1(); //PASSED
		
		//tests moves forward 1.414 * 40= 56.5cm
		//List<RobotMovement> egActions = HandMadeOptimalPaths.testPath2(); //PASSSED
		
		//tests infront of tunnel
		// List<RobotMovement> egActions = HandMadeOptimalPaths.testPath3(); //PASSED?
		
		
		/**
		 * I would say distance moved is alot less accurate then the final angle
		 * 
		 * Accuracy of final location is about within 2.5cm
		 * 
		 * Accuracy of angle is within 5 to 10 degrees, perhaps maybe 2 degrees
		 */
		//tests inside of tunnel
		List<RobotMovement> egActions = HandMadeOptimalPaths.testPath4(); //PASSED
		r.followInstructions(egActions, model.getNodeSize(), nodeDiagonal);
		
		// Going back to starting point

	}
}
