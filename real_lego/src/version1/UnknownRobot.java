package version1;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class UnknownRobot  {

	private EV3LargeRegulatedMotor motorL, motorR;
	private EV3MediumRegulatedMotor visionMotor;
	
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor ultrasonicSensor;
	private SensorMode colorMode;
	
	private float[] colourSample;
	private int counter = 0;
	
	
	public UnknownRobot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor) {
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
	
	public void goFoward(){
		
		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
		
		motorL.forward();
		motorR.forward();
		setSpeed(120, 120);
		
		while (ultrasonicSample[0] > 0.09){
			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			Delay.msDelay(5);
		}
	}

	public void followingLine() {
		

		colorMode = colorSensor.getRedMode();
		colourSample = new float[colorMode.sampleSize()];

		float[] ultrasonicSample = new float[1];
		ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

		
		float kp = 500f;//500f;//800f;//750f; //was 500 but worked for slow speed only
		float ki = 0f;
		float kd = 10f;//0f; 
		float offset = 0.3f;
		int tp = 180;//250;  //was 20 in last commit but very slow
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;
		
		final int HEAD_MS_DELAY = 80;
		final int HEAD_ROTATE_ANGLE = 25;
		while (ultrasonicSample[0] > 0.15)//0.09)
		{
			
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
			derivative = error - lastError;//if (error > 0.05) derivative = error - lastError; else derivative = 0f; //trying this

			setSpeed(kp, ki, kd, tp, integral, derivative, error);
			
			lastError = error;

			ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
			Delay.msDelay(5);
		}
		
		//experiment
		visionMotor.rotateTo(0, true);
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
		float kd = 10f;
		float offset = 0.07f;// 0.08f; //0.07f;
		int tp = 130; //70;
		float integral = 0f;
		float derivative = 0f;
		float lastError = 0f;

		while(colourSample[0] > 0.4){ //RobotMath.ON_BORDER_MAX) {
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
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		g.clear();
		g.drawString("Stepping over line", 0, 0, GraphicsLCD.LEFT);
		
		
		// move forward over the line
		setSpeed(100, 100);
		motorR.forward();
		motorL.forward();
		Delay.msDelay(700); 
		
		colorMode = colorSensor.getRedMode(); 
        colourSample = new float[colorMode.sampleSize()];
        colourSample[0] = 1.0f;
        
        
		while(colourSample[0] > 0.4){//RobotMath.ON_BORDER_MAX) {
			setSpeed(180, 50);
	        motorL.forward();   
	        motorR.backward();
	    	Delay.msDelay(5);
	    	colorMode.fetchSample(colourSample, 0);
	    	g.clear();
	    	g.drawString("RIGHT", 0, 0, GraphicsLCD.HCENTER);
		}
		
		g.clear();
    	g.drawString("2ndStepping over line", 0, 0, GraphicsLCD.LEFT);
		
    	while(colourSample[0] < 0.4) { //RobotMath.ON_BORDER_MAX) {
    		setSpeed(120, 50);
    		motorL.forward();   
    		motorR.backward();
    		colorMode.fetchSample(colourSample, 0);
    	}
		setSpeed(120, 50);// turn a bit more just in case
		motorL.forward();   
		motorR.backward();
		Delay.msDelay(200); 
		
		setSpeed(0, 180);
		motorL.forward();   
		motorR.forward();
		Delay.msDelay(100);
		
		g.clear();
		  
    }

    public void lookAhead() {
        visionMotor.rotateTo(0);
    }
    
    public void headTwitching(){
    	visionMotor.rotate(90);
		Delay.msDelay(400);
		visionMotor.rotate(-90);
		Delay.msDelay(400);
		visionMotor.rotate(45);
		Delay.msDelay(400);
		visionMotor.rotate(-45);
		Delay.msDelay(400);
		visionMotor.rotate(120);
		Delay.msDelay(400);
    }
    
    /**
     * 
     * @return The angle that it thinks give the closest distance
     */
    public int scanHead(){
    	
    	GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();	
    	
    	final int ANGLE_START = 70;
    	final int NUMBER_OF_SEGMENTS = 10;
    	int segmentSize = (ANGLE_START * 2 ) / NUMBER_OF_SEGMENTS; 
    	
    	float[] results = new float[NUMBER_OF_SEGMENTS + 1];
    	visionMotor.rotateTo(-ANGLE_START);
    	
    	float[] ultrasonicSample = new float[1];
    	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
    	
    	results[0] = ultrasonicSample[0]; 
    	for(int i = 1; i< NUMBER_OF_SEGMENTS + 1; i++)
    	{
        	visionMotor.rotate(segmentSize);
        	Delay.msDelay(20);
        	
        	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
        	results[i] = ultrasonicSample[0]; 
        	
        	g.clear();
			Delay.msDelay(5);
			g.drawString(Float.toString(ultrasonicSample[0]), 0, 0, GraphicsLCD.HCENTER);
    	}
    	
    	// find index of minimum
    	
    	float currentMin = results[0];
    	int indexOfMin = 0;
    	for ( int i = 0; i < results.length; i++ )
    	{
    		if ( results[i] < currentMin) { currentMin = results[i]; indexOfMin = i; }
    	}
    	
    	// calculate angle
    	
    	int angle = indexOfMin * segmentSize - ANGLE_START;
    	visionMotor.rotateTo(0); 
    	
    	
    	return angle;
    }
    /**
     * 
     * @return The angle that it thinks give the closest distance
     */
    public int scanHead2(){
    	
    	GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();	
    	
    	final int ANGLE_START = 70;
    	final int NUMBER_OF_SEGMENTS = 6;
    	int segmentSize = (ANGLE_START * 2 ) / NUMBER_OF_SEGMENTS; 
    	
    	float[] results = new float[NUMBER_OF_SEGMENTS + 1];
    	visionMotor.rotateTo(-ANGLE_START);
    	
    	float[] ultrasonicSample = new float[1];
    	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
    	
    	results[0] = ultrasonicSample[0]; 
    	for(int i = 1; i< NUMBER_OF_SEGMENTS + 1; i++)
    	{
        	visionMotor.rotate(segmentSize);
        	Delay.msDelay(20);
        	
        	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
        	results[i] = ultrasonicSample[0]; 
        	
        	g.clear();
			Delay.msDelay(5);
			g.drawString(Float.toString(ultrasonicSample[0]), 0, 0, GraphicsLCD.HCENTER);
    	}
    	
    	// find index of minimum
    	
    	float currentMin = results[0];
    	int indexOfMin = 0;
    	for ( int i = 0; i < results.length; i++ )
    	{
    		if ( results[i] < currentMin) { currentMin = results[i]; indexOfMin = i; }
    	}
    	
    	// calculate angle
    	
    	int angle = indexOfMin * segmentSize - ANGLE_START;
    	
    	// do a second pass
    	
    	final int SMALL_ERROR = 6;
    	
    	visionMotor.rotateTo(angle+SMALL_ERROR); Delay.msDelay(200);
    	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0); Delay.msDelay(50);
    	float result1 = ultrasonicSample[0]; 
    	visionMotor.rotate(-SMALL_ERROR); Delay.msDelay(200);
    	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0); Delay.msDelay(50);
    	float resultCenter = ultrasonicSample[0]; 
    	visionMotor.rotate(-SMALL_ERROR); Delay.msDelay(200);
    	ultrasonicSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);Delay.msDelay(50);
    	float result2 = ultrasonicSample[0]; 
    	
    	if( result1 < resultCenter && result1 < result2) angle -= 6;
    	else if(result2 < resultCenter && result2 < result1) angle += 6;
    	
    	
    	visionMotor.rotateTo(0); 
    	
    	
    	return angle;
    }
 
    
    
    public void turnAngle(int angle){
    	setSpeed(120,120);
    	if (angle >= 0){
    		motorR.backward();
        	motorL.forward();
    	}
    	else
    	{
    		motorR.forward();
        	motorL.backward();
    	}
    	
    	if (angle < 0 ) angle = - angle;
    	
    	final int NINETY = 1300;
    	Delay.msDelay(  (int)(angle/90.0 * NINETY));
    	stop();
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