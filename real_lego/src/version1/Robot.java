package version1;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

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
	}
	
	public void curtain() {
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
		while (ultrasonicSample[0] < 0.15)
		{
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			g.drawString(Float.toString(ultrasonicSample[0]), 0, 0, GraphicsLCD.HCENTER);
			Delay.msDelay(200);
			g.clear();
		}
		//followingLineSlow(2);
	}

	public void followingLine() {

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		
		float kp = 500f;//800f;//750f; //was 500 but worked for slow speed only
		float ki = 0f;
		float kd = 10f;//0f; 
		float offset = 0.3f;
		int tp = 180;//250;  //was 20 in last commit but very slow
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;
		
		while (ultrasonicSample[0] > 0.09)
		{
			// takes sample
			colorMode.fetchSample(colourSample, 0);
			
			float lightVal = colourSample[0];
			float error = lightVal - offset;
			integral += error;
			derivative = error - lastError;//if (error > 0.05) derivative = error - lastError; else derivative = 0f; //trying this

			setSpeed(kp, ki, kd, tp, integral, derivative, error);
			
			lastError = error;

			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			Delay.msDelay(5);
		}
		
	}

	public  void avoidObstacle() {

        GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

		// Position Robot
		setSpeed(120, 120);
		motorR.backward();
		motorL.forward();
		Delay.msDelay(1000);
		visionMotor.rotate(-90);

        colorMode = colorSensor.getRedMode();  // changed from getIDMode
        colourSample = new float[colorMode.sampleSize()];
        colourSample[0] = 1.0f;

		// Start avoiding obstacle
		float[] ultrasonicSample = new float[1];

		float kp = 500f;
		float ki = 0f;
		float kd = 0f;
		float offset = 0.07f;
		int tp = 70;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;

		while(colourSample[0] > RobotMath.ON_BORDER_MAX) {
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

			float distance = ultrasonicSample[0];
            if(distance > 0.3f) distance = 0.3f;
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

	public void turnRight() {
//		setSpeed(100, 100);
//		motorR.backward();
//		motorL.backward();
//		Delay.msDelay(2000);
		
		
//		setSpeed(180, 20);
//        motorL.forward();   //L motor now going forward!!
//        motorR.forward();
//        Delay.msDelay(3000); //added this
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		g.clear();
		g.drawString("Stepping over line", 0, 0, GraphicsLCD.HCENTER);
		
		setSpeed(100, 100);
		motorR.forward();
		motorL.forward();
		Delay.msDelay(700);
		
		colorMode = colorSensor.getRedMode();  // changed from getIDMode
        colourSample = new float[colorMode.sampleSize()];
        colourSample[0] = 1.0f;
        
        
		while(colourSample[0] > RobotMath.ON_BORDER_MAX) {
			setSpeed(180, 20);
	        motorL.forward();   //L motor now going forward!!
	        motorR.forward();
	    	Delay.msDelay(5);
	    	colorMode.fetchSample(colourSample, 0);
	    	g.clear();
	    	g.drawString("RIGHT", 0, 0, GraphicsLCD.HCENTER);
		}
		g.clear();
    	g.drawString("2ndStepping over line", 0, 0, GraphicsLCD.HCENTER);
		
		setSpeed(120, 50);
		motorL.forward();   
		motorR.backward();
		Delay.msDelay(800); 
		
		setSpeed(0, 180);
		motorL.forward();   
		motorR.forward();
		Delay.msDelay(400);
		
		g.clear();
		  
    }

    public void lookAhead() {
        visionMotor.rotate(90);
    }

	private void setSpeed(float kp, float ki, float kd, int tp, float integral, float derivative, float error) {
		float turn = kp * error + ki * integral + kd * derivative;
		float powerL = tp + turn;
		float powerR = tp - turn;

		setSpeed(powerR, powerL); // setSpeed(powerL, powerR); for the moment.....

		if(powerL > 0)
            motorL.forward();
        else
            motorL.backward();
		if(powerR > 0)
            motorR.forward();
        else
            motorR.backward();
	}

//	public boolean senseLine()
//	{
//		colorMode.fetchSample(colourSample, 0);
//		
//		return colourSample[0] < RobotMath.ON_LINE_MAX;
//	}
	
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
	
	public void followingLineSlow(int secs) {

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		
		float kp = 500f; //was 500 but worked for slow speed only
		float ki = 0f;
		float kd = 0f;
		float offset = 0.3f;
		int tp = 80;  //was 20 in last commit but very slow
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;
		
		int loops = 200 * secs;
		for(int i = 0; i < loops; i++)
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
	
}
