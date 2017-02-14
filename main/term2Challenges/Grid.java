package main.term2Challenges;

import java.awt.Point;
import java.util.TreeSet;
import java.util.ArrayList;
/**
 * Represents the map of nodes, to use in the path finding algorithm
 *
 * The bottom right hand corner of the grid layout, is the closest corner in line with the bayesian strip
 */
public class Grid {
	
	
	// course is 122 cm x 122cm 
	final private int COURSE_WIDTH = 122;  // in cm
	final private int NUMBER_OF_NODES_PER_EDGE = 62; 
	final private int ROBOT_RADIUS = 18;   // Robot is 30x20cm 
	final private int CYLINDER_RADIUS = 3;  // 5.5 cm diameter
	final private float DISTANCE_BETWEEN_NODES;
	
	
	// goal   24 width by 20 depth   2cm walls
	
	// 3cm each side of robot in goal..
	
	// width of wheels 15cm..
	
	final private float OFFSET; // in cm
	
	private TreeSet<Point> closedList;
	private ArrayList<Point> frontier;
	
	public Grid(){
		
		closedList = new TreeSet<Point>();
		frontier = new ArrayList<Point>();
		
		DISTANCE_BETWEEN_NODES = (float) COURSE_WIDTH / (float) (NUMBER_OF_NODES_PER_EDGE - 1);
		 
		
		// get rid of unreachable nodes
		int tmp =  (int) ROBOT_RADIUS / ROBOT_RADIUS;
		OFFSET = tmp * DISTANCE_BETWEEN_NODES;
				
		boolean[][] tmpGrid = new boolean[NUMBER_OF_NODES_PER_EDGE - 2 * tmp][NUMBER_OF_NODES_PER_EDGE - 2 * tmp];
		
	}
	
	/**
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @param cylinderPos distance in cm from bottom left corner
	 * @return A list of Actions for robot to follow
	 */
	public ArrayList<RobotMovement> calculatePath(int xStart, int yStart, int xEnd, int yEnd, float cylinderPos){
		inputCylinderPosition(cylinderPos / (Math.sqrt(2)), cylinderPos / (Math.sqrt(2)) );
		
		return null;
	}
	
	
	
	
	
	private int manhattanHeuristic(Point currentNode, Point goalNode){
		return Math.abs(currentNode.x - goalNode.x) + Math.abs(currentNode.y - goalNode.y);	
	}
	
	
	/**
	 * Updates the map so that it won't navigate though the unknown object
	 * @param d cm from LEFT wall
	 * @param e cm from BOTTOM wall
	 */
	private void inputCylinderPosition(double d, double e){ 
		Point center = closestNode(d,e);
		int distanceInNodes = (int) ((CYLINDER_RADIUS + ROBOT_RADIUS) / DISTANCE_BETWEEN_NODES) + 1;
		
		// remove a square from map .. for now
		// there are (2n-1) * (2n-1) nodes to remove
		
		int startx =  center.x - distanceInNodes; 
		int endx = center.x + distanceInNodes;
		int starty =  center.y - distanceInNodes; 
		int endy = center.y + distanceInNodes;
		
		for(int i = startx; i < endx; ++i ){
			for(int j = starty; i < endy; ++i ){
				closedList.add(new Point(i,j));
			}
		}
	}
	
	private Point closestNode(double x, double y){
		int xIndex =  (int) ((x - OFFSET) / DISTANCE_BETWEEN_NODES);
		int yIndex =  (int) ((x - OFFSET) / DISTANCE_BETWEEN_NODES);
		
		return new Point(xIndex, yIndex);
		
	}
	

}
