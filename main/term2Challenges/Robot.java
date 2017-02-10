package main.term2Challenges;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

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
	private SensorMode colorMode;
	
	private float[] colourSample;
	
	public Robot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor) {
		this.motorL = motorL;
		this.motorR	= motorR;
		this.visionMotor = visionMotor;
		this.colorSensor = colorSensor;
		this.ultrasonicSensor = ultrasonicSensor;
		this.ultrasonicSensor.getDistanceMode();
		this.visionMotor.rotateTo(0);
	}
	
	
	
	
	
	/**
	 * 
	 * @return  A distance of how far along the 'Strip the robot is'
	 */
	public int localize(){
		return 0;
		
	}
	
	
	/**
	 *  TODO: decide on some parameters
	 */
	public void rotateToDirection(){
		
		
	}
	
	/**
	 * TODO:
	 * @param distance
	 */
	public void moveDistance(int distance){
		
	}
	
	
	
	
	
	//// OLD METHODS below
	
	
	public void start() {
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		float[] ultrasonicSample = new float[1];

		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		while (ultrasonicSample[0] < 0.15) {
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			g.drawString(Float.toString(ultrasonicSample[0]), 0, 0, GraphicsLCD.HCENTER);
			Delay.msDelay(200);
			g.clear();
		}
	}


	public void followLineScan() {
		int counter = 0;

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		float kp = 500f;
		float ki = 0f;
		float kd = 10f;
		float offset = 0.3f;
		int tp = 180;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;

		final int HEAD_MS_DELAY = 60;
		final int HEAD_ROTATE_ANGLE = 35;
		while (ultrasonicSample[0] > 0.20) {
			counter++;
			if(counter == 0) visionMotor.rotateTo(HEAD_ROTATE_ANGLE, true);
			else if(counter == HEAD_MS_DELAY) visionMotor.rotateTo(0, true);
			else if(counter == 2*HEAD_MS_DELAY) visionMotor.rotateTo(-HEAD_ROTATE_ANGLE, true);
			else if (counter > 3*HEAD_MS_DELAY) counter = -1;

			// takes sample
			colorMode.fetchSample(colourSample, 0);

			float lightVal = colourSample[0];
			float error = lightVal - offset;
			integral += error;
			derivative = error - lastError;

			adjustSpeed(kp, ki, kd, tp, integral, derivative, error);

			lastError = error;

			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			Delay.msDelay(5);
		}

		//experiment
		visionMotor.rotateTo(0, true);
	}

	public void positionToTurnAround(int delay) {
		visionMotor.rotate(-90);
		setSpeed(120, 120);
		motorR.backward();
		motorL.forward();
		Delay.msDelay(delay);
	}

	public void avoidObstacle(float offset) {
		int counter = 0;

		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];
		colourSample[0] = 1.0f;

		// Start avoiding obstacle
		float[] ultrasonicSample = new float[1];

		float kp = 500f;
		float ki = 0f;
		float kd = 10f;
		int tp = 130;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;

		int seconds = 2;

		while(seconds > 0) {
			++counter;
			if(counter % 200 == 0)
				--seconds;

			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

			float distance = ultrasonicSample[0];
			if(distance > 0.3f)
				distance = 0.3f;
			float error = distance - offset;
			integral += error;
			derivative = error - lastError;

			float turn = kp * error + ki * integral + kd * derivative;
			float powerR = tp + turn;
			float powerL = tp - turn;

			setSpeed(powerL, powerR);

			if(powerL > 0)
				motorL.forward();
			else
				motorL.backward();
			if(powerR > 0)
				motorR.forward();
			else
				motorR.backward();

			lastError = error;

			colorMode.fetchSample(colourSample, 0);
			g.drawString(Float.toString(colourSample[0]), 0, 0, GraphicsLCD.HCENTER);
			Delay.msDelay(5);
			g.clear();
		}

		kp = 850f;
		ki = 0f;
		kd = 10f;
		tp = 250;

		while(colourSample[0] > 0.4) {
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

			float distance = ultrasonicSample[0];
			if(distance > 0.3f)
				distance = 0.3f;
			float error = distance - offset;
			integral += error;
			derivative = error - lastError;

			float turn = kp * error + ki * integral + kd * derivative;
			float powerR = tp + turn;
			float powerL = tp - turn;

			setSpeed(powerL, powerR);

			if(powerL > 0)
				motorL.forward();
			else
				motorL.backward();
			if(powerR > 0)
				motorR.forward();
			else
				motorR.backward();

			lastError = error;

			colorMode.fetchSample(colourSample, 0);
			g.drawString(Float.toString(colourSample[0]), 0, 0, GraphicsLCD.HCENTER);
			Delay.msDelay(5);
			g.clear();
		}
	}

	public void goForward(){

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		motorL.forward();
		motorR.forward();
		setSpeed(120, 120);

		while (ultrasonicSample[0] > 0.09){
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
		}
	}
	
	public void adjustToMinimumDistance(){
		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		if(ultrasonicSample[0] < 0.05) {
			motorL.backward();
			motorR.backward();
			setSpeed(120, 120);
			Delay.msDelay(1000);
		}
		
	}

	public void turnAngle(int angle) {
		setSpeed(120,120);
		if (angle > 0) {
			motorR.backward();
			motorL.forward();
		} else {
			motorR.forward();
			motorL.backward();
		}

		if (angle < 0) angle = - angle;

		final int NINETY = 1100;
		Delay.msDelay((int)(angle/90.0 * NINETY));
		stop();
	}

	private void adjustSpeed(float kp, float ki, float kd, int tp, float integral, float derivative, float error) {
		float turn = kp * error + ki * integral + kd * derivative;
		float powerL = tp + turn;
		float powerR = tp - turn;

		setSpeed(powerR, powerL);

		motorL.forward();
		motorR.forward();
	}

	public void stop() {
		motorL.stop();
		motorR.stop();
	}
	
	public void shutdown() {
		motorL.close();
		motorR.close();
		visionMotor.close();
		colorSensor.close();
		ultrasonicSensor.close();
	}
	
	private void setSpeed(float powerL, float powerR) {
		motorL.setSpeed(powerL);
		motorR.setSpeed(powerR);
	}
}
