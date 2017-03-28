package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.Sound;

/**
 * Localize Robot, follow path to in front of goal, follow path to wall at start location
 * Motors: left= A right = D
 * Sensors: Color= 4,lowerTouch=3, upperTouch=2,  Gyro = 1
 */
public class Challenge1 {

    public static void main(String[] args) {
        Robot r = new Robot();

        Button.waitForAnyPress();

        // Measure drift 
        if(r.isSensorDrifting())
            return;
        
        // Localize with Bayesian 'strip'
        int n = r.localize();
        System.out.println(n);
        if(n < 15)
            Sound.beepSequenceUp();
        else if(n < 22)
            Sound.beep();
        else
            Sound.beepSequence();

        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
        r.moveDistance(distToMoveOnDiagonal); 
        
        // Goal using A * (doesn't have to go inside)
        Grid model = new Grid();
        double[] goalCoordinates = model.initialiseClosedList1();
        Node goalNode = model.aStarSearch(firstNodePosition, goalCoordinates);
        LinkedList<Node> list = model.findPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
        double nodeSize = model.getNodeSize();
        double nodeDiagonalLength = RobotMovement.SQRT2 * nodeSize;

        r.followInstructions(actionList, nodeSize, nodeDiagonalLength);

        Sound.beep(); // Goal found
        
        // Going back to starting point
        list = model.findPath(goalNode);
        int wallDirection = RobotMovement.SW;
        actionList = RobotMovement.parsePathToMovements(list, wallDirection);
        r.followInstructions(actionList, nodeSize, nodeDiagonalLength);

        // Move to wall to finish task
        r.moveToWall();
        Sound.beep();
    }
}
