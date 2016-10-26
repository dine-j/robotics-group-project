package testSensorsMotors;

//package testSensorsMotors;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
import lejos.robotics.Color;



public class Test2 {

	public static void main(String[] args) {		
		EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);

		//EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		
		motorL.setSpeed(360);
		motorR.setSpeed(360);

		//SensorMode colorMode = colorSensor.getRedMode();
		//float[] sample = new float[colorMode.sampleSize()];
		
		motorR.forward();
		Delay.msDelay(2000);
		
		motorR.setSpeed(20); //doesn't change speed? unless forward envoked after
		motorR.forward();
		
		
		motorL.forward();
		Delay.msDelay(4000);
	
		
		motorL.stop();
		motorR.stop();
	}

}
