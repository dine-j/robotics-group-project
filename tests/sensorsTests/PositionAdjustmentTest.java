package tests.sensorsTests;

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
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
        EV3TouchSensor upperTouchSensor = new EV3TouchSensor(SensorPort.S2);
        EV3TouchSensor bottomTouchSensor = new EV3TouchSensor(SensorPort.S3);
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);

        Robot r = new Robot(motorL, motorR, colorSensor, upperTouchSensor, gyroSensor, bottomTouchSensor);

        Button.waitForAnyPress();

        r.adjustPosition();
    }
}
