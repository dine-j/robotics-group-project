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

public class RotationSquare {

    private static RegulatedMotor motorL;
    private static RegulatedMotor motorR;
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
            rotate();
            Delay.msDelay(1000);
        }
    }

    /*
    * Make robot travel for 50 cm
     */
    private static void travel() {
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
            Delay.msDelay(2);
        }

//        float angle = sample[0];
//        System.out.println(angle);
    }
}
