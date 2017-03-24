package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;

/**
 * Localize Robot, follow path to infront of goal, follow path to wall at start location
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
//        int n = 20; // stub - the last white square within two lines

        //TODO: not reliably going to first node : keep adjusting OFFSET_CORRECTION
        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = //GridGeo.OFFSET_CORRECTION +
        		(firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
        r.moveDistance(distToMoveOnDiagonal); 
        
        // Goal using A * (doesn't have to go inside)
        Grid model = new Grid();
        double[] goalCoords = model.initClosedList1();
        Node goalNode = model.aStarSearch(firstNodePosition, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);

        Sound.beep(); // Goal found
        
        // Going back to starting point
        list = model.findBackwardPath(goalNode);
        int wallDirection = RobotMovement.SW;
        actionList = RobotMovement.parsePathToMovements(list, wallDirection);

        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        r.moveToWall();
        Sound.beep();
    }
}
