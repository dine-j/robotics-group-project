package tests;

import java.util.LinkedList;
import java.util.List;

import lejos.utility.Delay;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;
import main.term2Challenges.Node;
import main.term2Challenges.RobotMovement;

/**
 * Class to test path-planning methods in debugger without using EV3Robot
 */
public class test {
    public static void main(String args[]){

        Grid model = new Grid();
        double[] goalCoords = model.initClosedList1();
        int n = 20; // stub 

        int  cellOffset = 2; // center of robot is 2 cells behind colour sensor reader.
        double[] startPosition = GridGeo.BayesianCoordinate(n - cellOffset);
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;

        Node goalNode = model.aStarSearch(firstNodePosition, goalCoords);

        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();

        //Delay.msDelay(3000); // found goal (hopefully)

        //turn 180 -- could do easier way
        List<RobotMovement> l = new LinkedList<RobotMovement>();
        l.add( RobotMovement.RIGHT180); 

        list.clear();
        // Going back to starting point
        list = model.findBackwardPath(goalNode);
        // get direction robot is facing now (below is stub) - should make new method?
        actionList = RobotMovement.parsePathToMovements(list);

    }
}
