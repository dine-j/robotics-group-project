package version1;

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

		UnknownRobot r = new UnknownRobot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor);
		Button.waitForAnyPress();
			
		r.curtain();
		
		
		for(int i = 0; i < 10; i++){
			r.followingLineScan();
			r.stop();
			r.turnAngle(r.scanHead());
			r.goForward();
			r.avoidObstacle();
			r.turnRight();
			r.lookAhead();
			r.followingLine();
			r.stop();
			Button.waitForAnyPress();
		}
		
//		for(int i = 0; i < 10; i++){
//			r.turnAngle(r.scanHead());
//			Button.waitForAnyPress();
//		}
		

		r.stop();
		r.shutdown();
	}
}