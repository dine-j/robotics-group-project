package tests.sensorsTests;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import java.io.IOException;

public class GyroPidTest {

    private static EV3LargeRegulatedMotor motorL;
    private static EV3LargeRegulatedMotor motorR;
    private static EV3GyroSensor gyro;

    public static void main(String args[]) throws IOException {
        motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Button.waitForAnyPress();

        gyro.reset();
        goForward();

        motorL.stop();
        motorR.stop();

        motorL.close();
        motorR.close();
    }

    private static void goForward() {
        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 4f;
        float ki = 0f;
        float kd = 0f;
        float offset = 0f;
        int tp = 80;
        float integral = 0f;
        float derivative = 0f;
        float lastError = 0f;

        int counter = 0;

        while (true) {
            // takes sample
            sampleProvider.fetchSample(sample, 0);

            float angleVal = sample[0];
            if(counter % 10 == 0)
                System.out.println(angleVal);
            float error = angleVal - offset;
            integral += error;
            derivative = error - lastError;

            adjustSpeed(kp, ki, kd, tp, integral, derivative, error);

            lastError = error;

            Delay.msDelay(5);
            ++counter;
        }
    }

    private static void adjustSpeed(float kp, float ki, float kd, int tp, float integral, float derivative, float error) {
        float turn = kp * error + ki * integral + kd * derivative;
        float powerL = tp + turn;
        float powerR = tp - turn;

        setSpeed(powerR, powerL);

        motorL.forward();
        motorR.forward();
    }

    private static void setSpeed(float powerL, float powerR) {
        motorL.setSpeed(powerL);
        motorR.setSpeed(powerR);
    }
}
