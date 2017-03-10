package main.term2Challenges;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Challenge2 {
    public static void main(String[] args) {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
        EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);


        EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);


        Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor, touchSensor);
        Button.waitForAnyPress();

        // Localize with Bayesian 'strip'
//		System.out.println(r.localize());

        // Make a sound

        // Goal using A * (doesn't have to go inside)

        // Going inside the tunnel
        r.enterTunnel();

        // Sensing color
        boolean green = r.getNextObstacle();

        // Moving back

        // Going to assigned obstacle then back starting point

    }
}
