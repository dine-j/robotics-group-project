package main.term2Challenges.errorAnalysis;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class PathNavigationErrorAnalysis {

    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
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
        turn(45 - 180);
        Delay.msDelay(1000);
        moveForward(DEGREES_PER_METER * 50 / 100);
    }

    /*
    * Make robot move forward for given distance
     */
    private static void moveForward(int distance) {
        motorL.setSpeed(180);
        motorR.setSpeed(180);
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

        float kp = 0.7f;
        float ki = 0f;
        float kd = 0f;
        int tp = 10;
        float integral = 0f;
        float derivative = 0f;

        while(Math.abs(sample[0]) < Math.abs(angle)) {
            float measuredAngle = sample[0];
            float error = measuredAngle - angle;

            float turn = kp * error + ki * integral + kd * derivative;
            float powerL = tp + turn;
            float powerR = tp - turn;

            motorL.setSpeed(powerL);
            motorR.setSpeed(powerR);

            if(angle > 0) {
                motorR.forward();
                motorL.backward();
            } else {
                motorL.forward();
                motorR.backward();
            }

            sampleProvider.fetchSample(sample, 0);
            Delay.msDelay(2);
        }

        // Debugging
        float measuredAngle = sample[0];
        System.out.println(measuredAngle);
    }
}