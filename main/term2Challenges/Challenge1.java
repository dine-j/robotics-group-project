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
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor);
		Button.waitForAnyPress();

		// Measure drift 
		
		// Localize with Bayesian 'strip'
//		System.out.println(r.localize());

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		Grid model = new Grid();

		//calculate tunnel position
		double[] goal = model.inputTunnelPosition(90, 90, 90);

		//find path
		AStarNode goalNode = model.findGoalNodeFromRoot(20, 20, (int) goal[1], (int) goal[0]);

		//get list containing path
        LinkedList<AStarNode> list = model.getListPathFromGoalNode(goalNode);

        //get action list
        List<RobotMovement> actionList = model.calculatePath(list);
        
        //r.followInstructions(actionList);

        //reverse through path provided

		//find reverse path
		AStarNode startNode = model.findGoalNodeFromRoot((int) goal[1], (int) goal[0], 20, 20);

		//get list with reverse path
		LinkedList<AStarNode> reverseList = model.getListPathFromGoalNode(startNode);

		//get reverse action list
		List<RobotMovement> reverseActionList = model.calculatePath(reverseList);

		//r.followInstructions(reverseActionList);
		
		
		
		// Update A* plan if it see obstacle in the way.
		
		// Going back to starting point

	}

}
