package main.term2Challenges;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Lists individual movements for robot to perform
 * Also lists and possible directions robot is facing 
 * & a parser to generate lists of actions from aStarNode path.
 */
public enum RobotMovement { 
	LEFT45,
	LEFT90,
	LEFT135,
	RIGHT45,
	RIGHT90,
	RIGHT135,
	RIGHT180, 
	FORWARD,
	FORWARD_ON_DIAGONAL;
	
	//Pre-computed value of square root of 2
	final static double SQRT2 = Math.sqrt(2);
	
	// Numeric constants for directions
    final static int W = 0;
    final static int NW = 1;
	final static int N = 2;
	final static int NE = 3;
	final static int E = 4;
	
	
    /**
     * @param path The sequence of nodes that represents the path
     * @return A list of RobotMovement's for robot to follow
     */
    public static List<RobotMovement> parsePathToMovements(LinkedList<AStarNode> path) {
        int direction = NW; //TODO: fix bug, robot is pointing NE
        List<RobotMovement> list = new ArrayList<RobotMovement>();
        AStarNode startNode = path.remove();
        int x = startNode.getX();
        int y = startNode.getY();

        while (!path.isEmpty()) {
            AStarNode nextNode = path.remove();
            int changeInX = nextNode.getX() - x;
            int changeInY = nextNode.getY() - y;

            int newDirection = getDirectionToGoal(changeInX, changeInY);
            if(newDirection == -1)
                throw new IllegalArgumentException("Wrong direction");
            if(direction != newDirection) {
                list.add(dirChange(direction, newDirection));
            }
            direction = newDirection;
            if(direction == NE || direction == RobotMovement.NW)
                list.add(FORWARD_ON_DIAGONAL);
            else
                list.add(FORWARD);
            x = nextNode.getX();
            y = nextNode.getY();
        }
        return list;
    }

    // precondition is: direction != newDirection
    private static RobotMovement dirChange(int direction, int newDirection) {
        switch (direction - newDirection) {
        	case -3:
        		return LEFT135;
            case -2:
                return LEFT90;
            case -1:
                return LEFT45;
            case 1:
                return RIGHT45;
            case 2:
                return RIGHT90;
            case 3:
                return RIGHT135;
            case 4:
                return RIGHT180;
        }
        return null; 
    }

    private static int getDirectionToGoal(int xChange, int yChange) {
//        System.out.println("Change in x: " + xChange + ", change in y: " + yChange);
        if(xChange == 0) { // Next square if at left or right
            if(yChange > 0) // Next square at right
                return E;
            else
                return W;
        }
        if(yChange == 0) { // Up or down
            if(xChange > 0) // Up
                return N;
        }
        if(xChange == 1 && yChange == 1)
            return NW;
        if(xChange == 1 && yChange == -1)
            return NE;
        return -1;
    }
}
