package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.Sound;

/**
 * Localize Robot, follow path to tunnel, enter tunnel and read color,
 * follow path to wall at start location using other half of course and
 * red/green cylinder location.
 * Motors: left= A right = D
 * Sensors: Color= 4,lowerTouch=3, upperTouch=2,  Gyro = 1
 */
public class Challenge2 {

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

        // int n = 20; // stub - the last white square within two lines

        //TODO: not reliably going to first node : keep adjusting OFFSET_CORRECTION
        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = GridGeo.OFFSET_CORRECTION + 
        		(firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
        r.moveDistance(distToMoveOnDiagonal); 
        
        // Goal using A * (doesn't have to go inside)
        double[] goalCoords = planToGoal(firstNodePosition,r);

        // Going inside the tunnel
        r.moveToWall();
        
        // Sensing color
        boolean isGreen = r.getNextObstacle();

        if (isGreen)
            Sound.beep();
        else
            Sound.twoBeeps();

        // Moving back
        r.exitTunnel();
        r.turnToGoAway();

        // Going to assigned obstacle then back starting point
        planBackToStart(goalCoords, r, isGreen);

        // Move until reaches wall
        r.moveToWall();
        Sound.beep();
    }
    
    private static double[] planToGoal(double[] start, Robot r){
        Grid model = new Grid();
        double[] goalCoords = model.initClosedList1();
        Node goalNode = model.aStarSearch(start, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        int tunnelWallDirection = RobotMovement.E;
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list, tunnelWallDirection);
        
//        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
//        actionList.add(RobotMovement.dirChange(tunnelWallDirection));
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        return goalCoords;
    }
    
    private static void planBackToStart(double[] start, Robot r, boolean isGreen){
        Grid model = new Grid();
        model.initClosedList2(isGreen);
        //Node goalNode = model.aStarSearch(start, GridGeo.CHALLENGE2_BACK_TO_START);
        
        //TODO: unhardcode this
        Node goalNode = model.aStarSearch(new double[]{110,62} ,GridGeo.CHALLENGE2_BACK_TO_START );
        LinkedList<Node> list = model.findForwardPath(goalNode);
        int wallDirection = RobotMovement.SW;
        
//        List<RobotMovement> actionList =RobotMovement.parsePathToMovements(list);
//        actionList.add(RobotMovement.dirChange(wallDirection));
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list, wallDirection);
        
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
    }
}
