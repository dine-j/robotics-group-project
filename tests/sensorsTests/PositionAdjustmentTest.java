package sensorsTests;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import main.term2Challenges.Robot;

public class PositionAdjustmentTest {

    public static void main(String[] args) {
        Robot r = new Robot();

        Button.waitForAnyPress();

        r.adjustPosition();
    }
}
