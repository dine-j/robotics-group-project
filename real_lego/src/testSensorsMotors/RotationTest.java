package testSensorsMotors;

import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;

/**
 * Rotates head a few times
 *
 */
public class RotationTest {

	public static void main(String[] args) {		
		EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.C);
		visionMotor.setSpeed(360);
		visionMotor.rotate(45);
		for(int i = 0; i < 20; ++i) {
			visionMotor.rotate(-90);
			visionMotor.rotate(90);
			//test
			if (i == 2) System.exit(0);
		}
		
		visionMotor.close();
	}

}
