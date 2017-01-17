package tests.sensorsTests;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Test gyro sensor
 */
public class GyroTest {
    public static void main(String[] args) {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);

        SampleProvider sampleProvider = gyro.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        motorL.setSpeed(150);

        for(int i = 0; i < 100; i++) {
            sampleProvider.fetchSample(sample, 0);

            GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
            g.drawString("" + sample[0], 0, 0, GraphicsLCD.VCENTER);
            Delay.msDelay(1000);
            g.clear();

            motorL.forward();
        }

        gyro.fetchSample(sample, 0);

        motorL.stop();
        motorR.stop();
    }
}
