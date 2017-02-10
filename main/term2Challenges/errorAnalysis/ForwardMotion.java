package main.term2Challenges.errorAnalysis;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ForwardMotion {
	
	
	private static boolean leftFirst = true ;

    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    private static SampleProvider sampleProvider;
    private static float[] sample;

    private static final int DEGREES_PER_METER = 2100;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.D);
        motorR = new EV3LargeRegulatedMotor(MotorPort.A);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Button.waitForAnyPress();
        
        gyro.reset();
        sampleProvider = gyro.getAngleMode();
        sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        for(int i = 0; i < 10; ++i) {
            moveForward();
            Delay.msDelay(2000);
            // Record error
            sampleProvider.fetchSample(sample, 0);
            float angle = sample[0];
            System.out.println(angle);
        }
    }

    /*
    * Make robot move forward for 2 cm
     */
    private static void moveForward() {
        int distance = DEGREES_PER_METER / 50;
        
        if (leftFirst){
        	 motorL.rotate(distance, true);
             motorR.rotate(distance);
        }else {
        	 motorR.rotate(distance, true);
             motorL.rotate(distance);
        }
       
        
        
        // TODO: Needs to change the speed of motors when robot is still

//        while(motorL.isMoving() && motorR.isMoving()) {
//        	System.out.println("entered loop");
//            float kp = 0.7f;
//            float ki = 0f;
//            float kd = 0f;
//            int tp = 120;
//            float integral = 0f;
//            float derivative = 0f;
//
//            while(sample[0] != 0) {
//                float angle = sample[0];
//                float error = angle - 00;
//
//                float turn = kp * error + ki * integral + kd * derivative;
//                float powerL = tp + turn;
//                float powerR = tp - turn;
//
//                motorL.setSpeed(powerL);
//                motorR.setSpeed(powerR);
//
//                sampleProvider.fetchSample(sample, 0);
//                Delay.msDelay(2);
//            }
//        }
    }
}