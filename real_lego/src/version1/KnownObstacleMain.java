package version1;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * Motors: left= A right = D  Rotation= B
 * Sensors: Vision= 2,  Color = 1,   TAKEN OUT FOR NOW: Gyro = 4
 *
 */

public class KnownObstacleMain {
	
	public static void main(String args[]) throws IOException {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

		Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor);
		Button.waitForAnyPress();

		float distanceAroundObstacle = 0.07f;
		
		r.start();
		
		r.followLine();
		r.avoidObstacle(distanceAroundObstacle);
		r.turnRight();
		r.lookAhead();

		r.followLine();
		

		r.stop();
		r.shutdown();
	}
}
