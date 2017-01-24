package tests.sensorsTests;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import main.Robot;

import java.io.IOException;

public class GyroPidTest {
    public static void main(String args[]) throws IOException {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
        EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);

        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
        EV3GyroSensor gyro = new EV3GyroSensor((Port) SensorPort.S1);

        Robot r = new Robot(motorL, motorR, visionMotor, null, ultrasonicSensor, gyro);
        Button.waitForAnyPress();

        r.start();
        r.goForward();

        r.stop();

        r.shutdown();
    }
}
