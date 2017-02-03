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

public class BackwardMotion {

    private static RegulatedMotor motorL;
    private static RegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    private static final int DEGREES_PER_METER = 2100;

    public static void main(String[] args) {
        motorL = new EV3LargeRegulatedMotor(MotorPort.D);
        motorR = new EV3LargeRegulatedMotor(MotorPort.A);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Button.waitForAnyPress();
        
        gyro.reset();
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        for(int i = 0; i < 10; ++i) {
            moveBackward();
            Delay.msDelay(2000);
            // Record error
            sampleProvider.fetchSample(sample, 0);
            float angle = sample[0];
            System.out.println(angle);
        }
    }

    /*
    * Make robot move backward on 2 cm
     */
    private static void moveBackward() {
        int distance = - (DEGREES_PER_METER / 50);
        motorL.rotate(distance, true);
        motorR.rotate(distance);
    }
}