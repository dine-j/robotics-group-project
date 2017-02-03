package main.term2Challenges;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class PathNavigationErrorAnalysis {

    private static RegulatedMotor motorL;
    private static RegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    private static final int DEGREES_PER_METER = 2100;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.D);
        motorR = new EV3LargeRegulatedMotor(MotorPort.A);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Button.waitForAnyPress();
        
        moveForward(DEGREES_PER_METER * 80 / 100);
        Delay.msDelay(1000);
        turn(90);
        Delay.msDelay(1000);
        moveForward(DEGREES_PER_METER * 50 / 100);
        Delay.msDelay(1000);
        turn(-45);
        Delay.msDelay(1000);
        moveForward(DEGREES_PER_METER * 50 / 100);
    }

    /*
    * Make robot move forward for given distance
     */
    private static void moveForward(int distance) {
        motorL.rotate(distance, true);
        motorR.rotate(distance);
    }
    
    /*
    * Make robot turn by given angle
     */
    private static void turn(int angle) {
        gyro.reset();
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        if(angle > 0) {
        		while(sample[0] < angle) {
            	motorR.rotate(10, true);
            	motorL.rotate(-10);
            	sampleProvider.fetchSample(sample, 0);
            	Delay.msDelay(2);
        		}
        } else {
        		while(sample[0] > angle) {
            	motorR.rotate(10, true);
            	motorL.rotate(-10);
            	sampleProvider.fetchSample(sample, 0);
            	Delay.msDelay(2);
        		}
        }
    }
}