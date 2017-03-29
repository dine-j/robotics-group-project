package tests;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Sound;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;
import main.term2Challenges.Node;
import main.term2Challenges.RobotMovement;

/**
 * Class to test path-planning methods in debugger without using EV3Robot
 */
public class test {
    public static void main(String args[]){

    	int n = 20;
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        
        // Goal using A * (doesn't have to go inside)
        Grid model = new Grid();
        double[] goalCoordinates = model.initialiseClosedList1(true);
        Node goalNode = model.aStarSearch(firstNodePosition, goalCoordinates);
        LinkedList<Node> list = model.findFowardPath(goalNode);
        int tunnelDirection = RobotMovement.E;
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list, tunnelDirection);

        
        // Going back to starting point
        list = model.findBackwardPath(goalNode);
        int wallDirection = RobotMovement.SW;
        actionList = RobotMovement.parsePathToMovements(list, wallDirection);

    }
}
