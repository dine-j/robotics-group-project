package tests.motorsTests;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class DistanceTest {
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



		//motorR.forward();
		//motorL.forward();

		//motorR.setSpeed(speed);
		final int DEGREES = 2100;
		final int MORE = 1000;
		
		motorL.rotate(DEGREES,true);
		motorR.rotate(DEGREES);
		
		Delay.msDelay(20000);
		
		motorL.rotate(DEGREES,true);
		motorR.rotate(DEGREES);
//		
		Delay.msDelay(20000);
		
		motorL.rotate(DEGREES,true);
		motorR.rotate(DEGREES);
		
//		motorL.rotate(-DEGREES,true);
//		motorR.rotate(-DEGREES);
		
//		Delay.msDelay(3000);
//		
//		motorL.rotate(DEGREES + MORE,true);
//		motorR.rotate(DEGREES + MORE);
//		
//		Delay.msDelay(1000);
//		
//		motorL.rotate(-DEGREES - MORE,true);
//		motorR.rotate(-DEGREES - MORE);
		
		


		//motorR.rotate(3000, true);
		//motorL.rotate(3000, true);



		//motorL.stop();
		//motorR.stop();
	}

}


