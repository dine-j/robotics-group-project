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
    public final static double SQRT2 = Math.sqrt(2);
    
    // Numeric constants for directions
    public final static int W = 0;
    public final static int NW = 1;
    public final static int N = 2;
    public final static int NE = 3;
    public final static int E = 4;
    public final static int SE = 5;
    public final static int S = 6;
    public final static int SW = 7;

    public static int direction;

    /**
     * @param path The sequence of nodes that represents the path
     * @return A list of RobotMovement's for robot to follow
     */
    public static List<RobotMovement> parsePathToMovements(LinkedList<Node> path) {
        List<RobotMovement> list = new ArrayList<RobotMovement>();
        Node startNode = path.remove();
        int x = startNode.getX();
        int y = startNode.getY();

        while (!path.isEmpty()) {
            Node nextNode = path.remove();
            int changeInX = nextNode.getX() - x;
            int changeInY = nextNode.getY() - y;

            int newDirection = getDirectionToGoal(changeInX, changeInY);
            if(newDirection == -1)
                throw new IllegalArgumentException("Wrong direction");
            if(direction != newDirection) {
                list.add(dirChange(newDirection));
            }
            direction = newDirection;
            if(direction == NE || direction == RobotMovement.NW 
                    || direction == RobotMovement.SW || direction == RobotMovement.SE )
                list.add(FORWARD_ON_DIAGONAL);
            else
                list.add(FORWARD);
            x = nextNode.getX();
            y = nextNode.getY();
        }
        return list;
    }

    // precondition is: direction != newDirection
    public static RobotMovement dirChange(int newDirection) {
    	int dirChangeValue = (direction - newDirection + 12) % 8 - 4;
        switch (dirChangeValue) {
            case 3:
                return LEFT135;
            case 2:
                return LEFT90;
            case 1:
                return LEFT45;
            case -1:
                return RIGHT45;
            case -2:
                return RIGHT90;
            case -3:
                return RIGHT135;
            case -4:
                return RIGHT180;
//            case 7:
//                return RIGHT45;
//            case -7:
//                return LEFT45;
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
            else{
                return S;
            }
        }
        if(xChange == 1 && yChange == 1)
            return NE;
        if(xChange == -1 && yChange == 1)
            return SE;
        if(xChange == 1 && yChange == -1)
            return NW;
        if(xChange == -1 && yChange == -1)
            return SW;

        return -1;
    }
}
