package testSensorsMotors;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
import lejos.robotics.Color;



public class Test {

	public static void main(String[] args) {		
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);

		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		
		motorL.setSpeed(360);
		motorR.setSpeed(360);

		/*int goal = 270; //degrees
		if(!motor.isMoving()){
			motor.rotate(goal); // serving to goal angle
		} 
		 
		else {
			motor.stop();
			motor.forward();
			try {
				Thread.sleep (1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			motor.backward();
			try {
				Thread.sleep (1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			motor.stop();
		}*/
		SensorMode colorMode = colorSensor.getRedMode();
		float[] sample = new float[colorMode.sampleSize()];

		/*for(int i = 0; i < 30; i++) {
			colorMode.fetchSample(sample, 0);

			GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
			g.drawString("" + sample[0], 0, 0, GraphicsLCD.VCENTER);
			Delay.msDelay(1000);
			g.clear();
		}*/
		
		colorMode.fetchSample(sample, 0);
		while(sample[0] > 0.1) {
			motorL.forward();
			motorR.forward();
			colorMode.fetchSample(sample, 0);
		}
		
		motorL.stop();
		motorR.stop();
	}

}
