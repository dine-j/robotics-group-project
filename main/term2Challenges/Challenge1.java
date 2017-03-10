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

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor, touchSensor);
		Button.waitForAnyPress();

		// Measure drift
		if(r.isSensorDrifting())
			return;
		
		// Localize with Bayesian 'strip'
//		System.out.println(r.localize());
		
		//TODO: measurement from r.localize()  + how to convert

		// Make a sound

		// Goal using A * (doesn't have to go inside)
		Grid model = new Grid();
		AStarNode goalNode = model.findGoalNodeFromRoot(32, 32);
        LinkedList<AStarNode> list = model.getListPathFromGoalNode(goalNode);
        List<RobotMovement> actionList = model.calculatePath(list);

		double nodeSize = model.getNodeSize();
		double nodeDiagonal = Math.sqrt(nodeSize * nodeSize + nodeSize * nodeSize);
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
		
		// Going back to starting point

	}

}
