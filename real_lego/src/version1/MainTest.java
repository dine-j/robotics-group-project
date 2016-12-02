package version1;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class MainTest {
	
	public static void main(String args[]) throws IOException {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor);
		Button.waitForAnyPress();
		
//		for(int i= 0; i < 6; i++){
//			r.turnAngle(r.scanHead());
//			Delay.msDelay(100);
//			r.lookAhead();
//			Delay.msDelay(500);
//		}
//		
		r.curtain();
		
		
		for(int i = 0; i < 4; i++){
			r.followingLine();
			r.stop();
			r.turnAngle(r.scanHead2());
			r.goFoward();
			r.avoidObstacle();
			r.turnRight();
			r.lookAhead();
		}
		
		//r.turnRight();

		r.followingLine();
		
		
		
//		r.turnAngle(90);
//		Delay.msDelay(1000);
//		r.turnAngle(60);
//		Delay.msDelay(1000);
//		r.turnAngle(30);
		
		r.stop();
		r.shutdown();
	}
}