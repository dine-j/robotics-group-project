package main.term2Challenges.errorAnalysis;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class SquareRotation {

    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    private static final int DEGREES_PER_METER = 2100;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.D);
        motorR = new EV3LargeRegulatedMotor(MotorPort.A);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Button.waitForAnyPress();

        for(int i = 0; i < 4; ++i) {
            travel();
            Delay.msDelay(1000);
            rotatePID();
            Delay.msDelay(1000);
        }
    }

    /*
    * Make robot travel for 50 cm
     */
    private static void travel() {
        motorL.setSpeed(120);
        motorR.setSpeed(120);
        int distance = DEGREES_PER_METER / 2;
        motorL.rotate(distance, true);
        motorR.rotate(distance);
    }

    /*
    * Make robot rotate 90 degrees anticlockwise
     */
    private static void rotate() {
        gyro.reset();
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(sample[0] < 90) {
            motorR.rotate(10, true);
            motorL.rotate(-10);
            sampleProvider.fetchSample(sample, 0);
//            Delay.msDelay(2);
        }

        // Debugging
//        float angle = sample[0];
//        System.out.println(angle);
    }

    /*
    * Make robot rotate 90 degrees anticlockwise using PID
     */
    private static void rotatePID() {
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

        while(sample[0] < 90) {
            float angle = sample[0];
            float error = angle - 90;

            float turn = kp * error + ki * integral + kd * derivative;
            float powerL = tp + turn;
            float powerR = tp - turn;

            motorL.setSpeed(powerL);
            motorR.setSpeed(powerR);

            motorR.forward();
            motorL.backward();

            sampleProvider.fetchSample(sample, 0);
            Delay.msDelay(2);
        }

        // Debugging
        float angle = sample[0];
        System.out.println(angle);
    }
}
