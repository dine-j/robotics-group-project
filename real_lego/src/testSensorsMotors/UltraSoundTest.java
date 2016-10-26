package testSensorsMotors;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class UltraSoundTest {

	public static void main(String[] args) {
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
		EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
		
		motorL.setSpeed(360);
		motorR.setSpeed(360);
		
		ultraSonicSensor.enable();
		
		SampleProvider sampleProvider = ultraSonicSensor.getDistanceMode();
		float[] sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		
//		for(int i = 0; i < 100; i++) {
//			sampleProvider.fetchSample(sample, 0); 
//
//			GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
//			g.drawString("" + sample[0], 0, 0, GraphicsLCD.VCENTER);
//			Delay.msDelay(1000);
//			g.clear();
//		}
		
		while(sample[0] > 0.1) {
			motorL.forward();
			motorR.forward();
			sampleProvider.fetchSample(sample, 0);
		}
		
		motorL.stop();
		motorR.stop();
		
		ultraSonicSensor.disable();
		ultraSonicSensor.close();
	}

}
