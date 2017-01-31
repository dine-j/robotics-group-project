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
		

		for (int i = 0; i < 7; i++){
			
			motorR.setSpeed(20);
			motorL.setSpeed(20);
			motorR.forward();
			motorL.forward();
			Delay.msDelay(500);
			motorR.setSpeed(400);
			motorL.setSpeed(400);
			motorR.forward();
			motorL.forward();
			Delay.msDelay(5000);
			
//			motorR.stop();
//			motorL.stop();
//			Delay.msDelay(500);
			
			motorR.setSpeed(20);
			motorL.setSpeed(20);
			motorR.backward();
			motorL.backward();
			Delay.msDelay(500);
			motorR.setSpeed(400);
			motorL.setSpeed(400);
			motorR.backward();
			motorL.backward();
			Delay.msDelay(5000);
			
//			motorR.stop();
//			motorL.stop();
//			Delay.msDelay(500);

		}
	
		
		motorL.stop();
		motorR.stop();
	}

}


