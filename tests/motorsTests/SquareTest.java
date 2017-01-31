package tests.motorsTests;

import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class SquareTest {
	public static void main(String[] args) {		
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
		
		
		float[] touchSample = new float[1];
		while(true){
			SensorMode sm = touchSensor.getTouchMode();
			sm.fetchSample(touchSample, 0);
			if (touchSample[0] == 1.0) break;
		}

		final int DEGREES = 2100;
		//final int NINETY = 254;  //calculated from 7.7cm radius from center
		
		final int NINETY = 270;

		for (int i = 0; i < 3; i++){
			for (int square =0 ; square < 4; square++){
				motorL.rotate(DEGREES/2,true);
				motorR.rotate(DEGREES/2);
				Delay.msDelay(1000);
				motorL.rotate(NINETY,true);
				motorR.rotate(-NINETY);
				Delay.msDelay(1000);
			}
		}
		
	
	}
	

}