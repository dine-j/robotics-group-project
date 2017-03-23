package main.term2Challenges.errorAnalysis;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Initial testing for rotating robot using PID algorithm
 */
public class RotationMotion {
    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        gyro = new EV3GyroSensor(SensorPort.S1);

        Delay.msDelay(60);

        for(int i = 0; i < 4; ++i) {
            rotatePID(-90);
            Delay.msDelay(3000);
        }
    }

    /*
    * Make robot rotate 90 degrees anticlockwise using PID
     */
    private static void rotatePID(int rotationValue) {
        gyro.reset();
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 2f;
        int tp = 30;
        int threshold = 10;

        if (rotationValue > 0) {
            while (sample[0] < rotationValue) {
                float angle = sample[0];
                float error = angle - rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorR.forward();
                motorL.backward();

                sampleProvider.fetchSample(sample, 0);

                if(rotationValue - angle <= threshold) {
                    kp = 0.7f;
                    tp = 10;
                }
            }
        } else {
            while (Math.abs(sample[0]) < -rotationValue) {
                float angle = Math.abs(sample[0]);
                float error = angle + rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorL.forward();
                motorR.backward();

                sampleProvider.fetchSample(sample, 0);

                if(-rotationValue - angle <= threshold) {
                    kp = 0.7f;
                    tp = 10;
                }
            }
        }

        // Debugging
        float angle = sample[0];
        System.out.println(angle);

        motorR.stop();
        motorL.stop();
    }

    private static void rotate(int value) {
        gyro.reset();
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(sample[0] < value) {
            motorR.rotate(10, true);
            motorL.rotate(-10);
            sampleProvider.fetchSample(sample, 0);
//            Delay.msDelay(2);
        }

        // Debugging
//        float angle = sample[0];
//        System.out.println(angle);
    }
}
