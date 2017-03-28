package tests;

import java.util.LinkedList;
import java.util.List;

import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;
import main.term2Challenges.Node;
import main.term2Challenges.RobotMovement;

/**
 * Class to test path-planning methods in debugger without using EV3Robot
 */
public class test {
    public static void main(String args[]){

//        int n = r.localize();  
//        System.out.println(n);
        int n = 20; // stub - the last white square within two lines

        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
        
        // Goal using A * (doesn't have to go inside)
        Grid model = new Grid();
        double[] goalCoords = model.initialiseClosedList1();
        Node goalNode = model.aStarSearch(firstNodePosition, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);


        list = model.findBackwardPath(goalNode);
        actionList = RobotMovement.parsePathToMovements(list);

        // Add extra movement to face wall
        int wallDirection = RobotMovement.SW;
        
//        RobotMovement badValue = RobotMovement.dirChange(wallDirection);
//        actionList.add(badValue);

        //Sound.beep();

    }
}
