package version1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * 
 * @author k1502837
 *
 *
 * Sensor A:
 */

public class Robot  {
	
	private final int SPEED = 200;
	private final int TURN_SPEED = 80;
	
	private EV3LargeRegulatedMotor motorL, motorR;
	private EV3MediumRegulatedMotor visionMotor;
	
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor ultrasonicSensor;
	private EV3GyroSensor gyro;
	private SensorMode colorMode;
	private SampleProvider gyroMode;
	
	private float[] colourSample;
	private float[] gyroSample;
	
	private Image face;
	
	public Robot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor, EV3GyroSensor gyro) {
		this.motorL = motorL;
		this.motorR	= motorR;
		this.visionMotor = visionMotor;
		this.colorSensor = colorSensor;
		this.ultrasonicSensor = ultrasonicSensor;
		this.gyro = gyro;
		
		setSpeed(SPEED,SPEED);
	}
	
	public void followingLine(int seconds) throws IOException {
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		InputStream s = new FileInputStream(new File("Toxic.bmp"));
		face = Image.createImage(s);
		g.drawImage(face, 15, 15, g.HCENTER);
		
		colorMode = colorSensor.getRedMode();
		gyroMode = gyro.getAngleAndRateMode();
		colourSample = new float[colorMode.sampleSize()];
		
		//The sample contains two elements.
		//The first element contains angular velocity (in degrees / second). The second element contain angle (in degrees). 
		gyroSample = new float[gyroMode.sampleSize()];
		
		float kp = 210f; // to set to 500(for next time)
		float ki = 0f;
		float kd = 0f;
		float offset = 0.3f;
		int tp = 80;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;
		
		while (true)
		{
			// takes sample
			colorMode.fetchSample(colourSample, 0);
			
			float lightVal = colourSample[0];
			float error = lightVal - offset;
			integral += error;
			derivative = error - lastError;
			
			float turn = kp * error + ki * integral + kd * derivative;
			float powerL = tp + turn;
			float powerR = tp - turn;
			
			setSpeed((int)powerL, (int)powerR);
			motorL.forward();
			motorR.forward();
			
			lastError = error;
			//g.drawString(sample[0] + " PR:" + (int)powerR + " PL:" + (int)powerL , 0, 0, GraphicsLCD.VCENTER);
			Delay.msDelay(5);
			//g.clear();
		}
		
		
		
		
		
		//for(int i = 0; i < seconds * 10; ++i) {
		/*while (true){
			// takes sample
			colorMode.fetchSample(sample, 0);
			
			// if sample says white turn right
			if(sample[0] >= RobotMath.ON_WHITE_MIN) {
				g.drawString("RIGHT" + sample[0], 0, 0, GraphicsLCD.VCENTER);
				Delay.msDelay(10);
				g.clear();
				
				turnRight();
			} else if(sample[0] <= RobotMath.ON_LINE_MAX) { // if says black turn left
				g.drawString("LEFT" + sample[0], 0, 0, GraphicsLCD.VCENTER);
				Delay.msDelay(10);
				g.clear();
				
				turnLeft();
			} else {
				g.drawString("FORWARD" + sample[0], 0, 0, GraphicsLCD.VCENTER);
				Delay.msDelay(10);
				g.clear();
				forward();
			
		}}*/
		
		
		//Delay.msDelay(100);
	}
	
	/*private void turnLeft() {
		setSpeed(TURN_SPEED,SPEED);
		motorL.forward();
		motorR.forward();
	}
	
	private void turnRight() {
		setSpeed(SPEED,TURN_SPEED);
		motorL.forward();
		motorR.forward();
	}
	
	private void forward() {
		setSpeed(SPEED, SPEED);
		motorL.forward();
		motorR.forward();
	}*/
	
	public void goInSemiCircle() {
		int t = 8;
		motorR.setSpeed(202);
		motorL.setSpeed(106);
		for(int i = 0; i < t; ++i) {
			forward();
			Delay.msDelay(5);
		}
		stop();
	}
	
	public boolean senseLine()
	{
		colorMode.fetchSample(colourSample, 0);
		
		return colourSample[0] < RobotMath.ON_LINE_MAX;
	}
	
	public void forward() {
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
	
	private void setSpeed(int speedL, int speedR) {
		motorL.setSpeed(speedL);
		motorR.setSpeed(speedR);
	}


	
	
}
