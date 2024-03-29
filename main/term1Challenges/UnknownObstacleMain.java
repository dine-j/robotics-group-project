package main.term1Challenges;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UnknownObstacleMain {
	
	public static void main(String args[]) throws IOException {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, null);
		float distanceAroundObstacle = 0.07f;
		int delayForPositioning = 1600;

		Button.waitForAnyPress();

		for(int i = 0; i < 10; i++){
			r.start();
			r.followLineScan();
			r.stop();
			r.turnAngle(r.findClosestPoint());
			r.adjustToMinimumDistance();
			r.goForward();
			r.positionToTurnAround(delayForPositioning);
			r.avoidObstacle(distanceAroundObstacle);
			r.turnRight();
			r.lookAhead();
			r.followLine();
			r.stop();
			Button.waitForAnyPress();
		}

		r.stop();
		r.shutdown();
	}
}