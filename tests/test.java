package tests;

import java.util.LinkedList;
import java.util.List;

import lejos.utility.Delay;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;
import main.term2Challenges.Node;
import main.term2Challenges.RobotMovement;

public class test {
	public static void main(String args[]){
		
		/*
		double[] coords = GridGeo.actualRobotCenterSW(new double[]{55,55});
		System.out.println(coords[0]);
		System.out.println(coords[1]);
		*/
		
		
		/*
		 * Calculating results of the stub 20
		 */
		/*
		int n = 20; // stub - the last white square within two lines
		//Calculate where the robot center was based on where it finished reading.
		double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
		// startPosition should be about 2 cells behind the colour sensor in current build
		
		double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
		
		
		//debugging - Prints where center of robot was immediately after localization
		System.out.printf("%.1f , %.1f\n", startPosition[0], startPosition[1]);
		
		//debugging - Prints where robot plans from
		System.out.printf("%.1f , %.1f\n", firstNodePosition[0], firstNodePosition[1]);
		*/
		/*
		 * Results are: 41.7 , 41.7
		 * Results are: 42.0 , 42.0
		 */
		
		
		
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
