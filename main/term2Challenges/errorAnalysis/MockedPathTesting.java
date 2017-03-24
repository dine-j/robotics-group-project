package main.term2Challenges.errorAnalysis;

import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import main.term2Challenges.Grid;
import main.term2Challenges.Robot;
import main.term2Challenges.RobotMovement;
import tests.aStarTest.MockedPathGenerator;

/**
 * A class for testing abitary RobotMovement lists
 */
public class MockedPathTesting {

    public static void main(String[] args) {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
        EV3UltrasonicSensor upperTouchSensor = new EV3UltrasonicSensor(SensorPort.S2);
        EV3TouchSensor bottomTouchSensor = new EV3TouchSensor(SensorPort.S3);
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);

        Robot r = new Robot(motorL, motorR, colorSensor, upperTouchSensor, gyroSensor, bottomTouchSensor);
        Button.waitForAnyPress();
        Grid model = new Grid();
        double nodeSize = model.getNodeSize();
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();

        //tests moves forward 60cm
        //List<RobotMovement> egActions = MockedPathGenerator.testPath1(); //PASSED

        //tests moves forward 1.414 * 40= 56.5cm
        //List<RobotMovement> egActions = MockedPathGenerator.testPath2(); //PASSSED

        //tests infront of tunnel
        // List<RobotMovement> egActions = MockedPathGenerator.testPath3(); //PASSED?

        /**
         * I would say distance moved is a lot less accurate then the final angle
         * 
         * Accuracy of final location is about within 2.5cm
         * 
         * Accuracy of angle is within 5 to 10 degrees, perhaps maybe 2 degrees
         */
        
        //tests inside of tunnel
        // List<RobotMovement> egActions = MockedPathGenerator.testPath4(); //PASSED
        
        //tests moves forward 2 meters
        List<RobotMovement> egActions = MockedPathGenerator.testPath5(); //TODO:
        
        //tests moves forward 1.414 * 100 = 1.414 meters
        //List<RobotMovement> egActions = MockedPathGenerator.testPath6(); //TODO:
        
        
        
         r.followInstructions(egActions, nodeSize, nodeDiagonal);

    }
}