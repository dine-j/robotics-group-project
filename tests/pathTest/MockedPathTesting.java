package tests.pathTest;

import java.util.List;

import lejos.hardware.Button;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;
import main.term2Challenges.Robot;
import main.term2Challenges.RobotMovement;

/**
 * A class for testing abitary RobotMovement lists
 */
public class MockedPathTesting {

    public static void main(String[] args) {

        Robot r = new Robot();
        Button.waitForAnyPress();
        Grid model = new Grid();
        double nodeSize = GridGeo.NODE_SIZE;
        double nodeDiagonal = RobotMovement.SQRT2 * GridGeo.NODE_SIZE;

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
        
        //tests moves forward 4 meters
        List<RobotMovement> egActions = MockedPathGenerator.testPath5(); //TODO:
        
        //tests moves forward 1.414 * 100 = 1.414 meters
        //List<RobotMovement> egActions = MockedPathGenerator.testPath6(); //TODO:
        
        
        
         r.followInstructions(egActions, nodeSize, nodeDiagonal);

    }
}
