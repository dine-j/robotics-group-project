package main.term2Challenges.errorAnalysis;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class RotationMotion {
    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Delay.msDelay(2000);

        gyro.reset();

        Delay.msDelay(2000);

        rotatePID(90);
    }

    /*
    * Make robot rotate 90 degrees anticlockwise using PID
     */
    private static void rotatePID(int rotationValue) {
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 0.7f;
        float ki = 0f;
        float kd = 0f;
        int tp = 10;
        float integral = 0f;
        float derivative = 0f;

        while(sample[0] < rotationValue) {
            float angle = sample[0];
            float error = angle - rotationValue;

            float turn = kp * error + ki * integral + kd * derivative;
            float powerL = tp + turn;
            float powerR = tp - turn;

            motorL.setSpeed(powerL);
            motorR.setSpeed(powerR);

            motorR.forward();
            motorL.backward();

            sampleProvider.fetchSample(sample, 0);
//            Delay.msDelay(2);
        }

        // Debugging
        float angle = sample[0];
        System.out.println(angle);
    }
}
