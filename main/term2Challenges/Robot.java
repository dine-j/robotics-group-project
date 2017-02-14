package main.term2Challenges;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.*;

/*
 * Port A: MotorL (works best this configuration - week 4)
 * Port D: MotorR
 * 
 * Port B: HeadRotatingMotor
 * 
 * Port 1: Gyro
 * Port 2: Ultrasound
 * Port 3: Touch
 * Port 4: Color
 */

public class Robot  {

	private EV3LargeRegulatedMotor motorL, motorR;
	private EV3MediumRegulatedMotor visionMotor;
	
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor ultrasonicSensor;
	private EV3GyroSensor gyroSensor;

	private static final int DEGREES_PER_METER = 2100;
	
	public Robot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor, EV3GyroSensor gyroSensor) {
		this.motorL = motorL;
		this.motorR	= motorR;
		this.visionMotor = visionMotor;
		this.colorSensor = colorSensor;
		this.ultrasonicSensor = ultrasonicSensor;
		this.gyroSensor = gyroSensor;

		this.ultrasonicSensor.getDistanceMode();
		this.visionMotor.rotateTo(0);
	}

	/**
	 * 
	 * @return  A distance of how far along the 'Strip the robot is'
	 */
	public int localize() {
		LocalizationStrip localizationStrip = new LocalizationStrip();

		double sensorProbability = 0.95;

		SensorMode colorMode = colorSensor.getRGBMode();
		float[] sample = new float[colorMode.sampleSize()];

		for(int i = 0; i < 10; ++i) {
			colorMode.fetchSample(sample, 0);
			boolean isBlue = false;

			if(sample[2] < 0.1) // if robot senses blue
				isBlue = true;

			moveDistance(2);
			localizationStrip.updateProbs(true, isBlue, sensorProbability, 1);
		}

		if(localizationStrip.getHighestProbability() < 0.5) { // if by any chance the probabilities are too low, take one more step to correct it
			colorMode.fetchSample(sample, 0);
			boolean isBlue = false;

			if(sample[0] == 2) // if robot senses blue
				isBlue = true;

			moveDistance(2);
			localizationStrip.updateProbs(true, isBlue, sensorProbability, 1);
		}
		return localizationStrip.getLocation();
	}
	
	/**
	 *  TODO: decide on some parameters
	 */
	public void rotateToDirection(){
		
		
	}
	
	/**
	 * Move the robot forward or backward given a certain distance
	 * @param distance	Distance for movement, can be positive or negative
	 */
	public void moveDistance(int distance) {
		int angle = distance * DEGREES_PER_METER / 100;
		motorL.rotate(angle, true);
		motorR.rotate(angle);
	}
}
