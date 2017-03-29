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
public class Challenge2B {

    public static void main(String[] args) {
        boolean firstObstacle = false;
        Robot robot = new Robot();

        Button.waitForAnyPress();

        // Measure drift
        if(robot.isSensorDrifting())
            return;

        // Localize with Bayesian 'strip'
        int position = robot.localize();
        System.out.println(position);
        if(position < 15)
            Sound.beepSequenceUp();
        else if(position < 22)
            Sound.beep();
        else
            Sound.beepSequence();

        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(position));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * Math.sqrt(2);
        robot.moveDistance(distToMoveOnDiagonal);

        // Goal using A * (doesn't have to go inside)
        planToGoal(firstNodePosition, robot, firstObstacle);

        robot.tryToEnterTunnel();

        // Going inside the tunnel
        robot.moveToWall();

        // Sensing color
        boolean isGreen = robot.getNextObstacle();

        if (isGreen)
            Sound.beep();
        else
            Sound.twoBeeps();

        // Moving back
        robot.exitTunnel();
        RobotMovement.direction = RobotMovement.E;

        // Going to assigned obstacle then back starting point
        planBackToStart(new double[]{110,62}, robot, isGreen, firstObstacle);

        // Move until reaches wall
        robot.moveToWall();
        Sound.beep();
    }

    private static void planToGoal(double[] start, Robot r, boolean firstObstacle) {
        Grid model = new Grid();
        double[] goalCoordinates = model.initialiseClosedList1(firstObstacle);
        Node goalNode = model.aStarSearch(start, goalCoordinates);
        LinkedList<Node> list = model.findFowardPath(goalNode);
        int tunnelWallDirection = RobotMovement.E;
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list, tunnelWallDirection);

        r.followInstructions(actionList, GridGeo.NODE_SIZE, GridGeo.NODE_DIAGONAL);
    }

    private static void planBackToStart(double[] start, Robot r, boolean isGreen, boolean firstObstacle) {
        Grid model = new Grid();

        model.initialiseClosedList2(firstObstacle, isGreen);

        Node goalNode = model.aStarSearch(start, GridGeo.CHALLENGE2_BACK_TO_START);
        LinkedList<Node> list = model.findFowardPath(goalNode);
        int wallDirection = RobotMovement.SW;

        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list, wallDirection);

        r.followInstructions(actionList, GridGeo.NODE_SIZE, GridGeo.NODE_DIAGONAL);
    }
}
