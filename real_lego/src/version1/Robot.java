package version1;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

/**
 * 
 * @author k1502837
 *
 *
 * Sensor A:
 */

public class Robot  {

	private EV3LargeRegulatedMotor motorL, motorR;
	private EV3MediumRegulatedMotor visionMotor;
	
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor ultrasonicSensor;
	private SensorMode colorMode;
	
	private float[] colourSample;
	
	private Image face;
	
	public Robot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor) {
		this.motorL = motorL;
		this.motorR	= motorR;
		this.visionMotor = visionMotor;
		this.colorSensor = colorSensor;
		this.ultrasonicSensor = ultrasonicSensor;

		this.ultrasonicSensor.getDistanceMode();
	}

	public void followingLine() {

		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		
		float kp = 800f; //was 500 but worked for slow speed only
		float ki = 0f;
		float kd = 0f;
		float offset = 0.3f;
		int tp = 250;  //was 20 in last commit but very slow
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;
		
		while (ultrasonicSample[0] > 0.1)
		{
			// takes sample
			colorMode.fetchSample(colourSample, 0);
			
			float lightVal = colourSample[0];
			float error = lightVal - offset;
			integral += error;
			derivative = error - lastError;

			setSpeed(kp, ki, kd, tp, integral, derivative, error);
			
			lastError = error;

			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			Delay.msDelay(5);
		}
		
	}

	public  void avoidObstacle() {
		// Position Robot
		setSpeed(90, 90);
		motorL.backward();
		motorR.forward();
		Delay.msDelay(1000);
		visionMotor.rotate(90);

		// Start avoiding obstacle
		float[] ultrasonicSample = new float[1];

		float kp = 500f;
		float ki = 0f;
		float kd = 0f;
		float offset = 0.12f;
		int tp = 20;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;

		while(true) {
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

			float distance = ultrasonicSample[0];
			float error = distance - offset;
			integral += error;
			derivative = error - lastError;

			setSpeed(kp, ki, kd, tp, integral, derivative, error);

			lastError = error;
			Delay.msDelay(5);
		}
	}

	private void setSpeed(float kp, float ki, float kd, int tp, float integral, float derivative, float error) {
		float turn = kp * error + ki * integral + kd * derivative;
		float powerL = tp + turn;
		float powerR = tp - turn;

		setSpeed(powerL, powerR);

		if(powerL > 0)
            motorL.forward();
        else
            motorL.backward();
		if(powerR > 0)
            motorR.forward();
        else
            motorR.backward();
	}

	public boolean senseLine()
	{
		colorMode.fetchSample(colourSample, 0);
		
		return colourSample[0] < RobotMath.ON_LINE_MAX;
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
