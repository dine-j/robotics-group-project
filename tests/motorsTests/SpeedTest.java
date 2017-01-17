package tests.motorsTests;

import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class SpeedTest {

	public static void main(String[] args) {		
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		
		motorL.setSpeed(360);
		motorR.setSpeed(360);
		
		motorR.forward();
		Delay.msDelay(2000);
		
		motorR.setSpeed(20);
		motorR.forward();
		
		
		motorL.forward();
		Delay.msDelay(4000);
	
		
		motorL.stop();
		motorR.stop();
	}

}
