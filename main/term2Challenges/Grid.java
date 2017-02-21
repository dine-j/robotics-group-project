package main.term2Challenges;

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
	final private int ROBOT_WIDTH = 10 ;   // Robot is 30x20cm 
	final private int ROBOT_LENGTH = 15;
	final private int ROBOT_RADIUS = (int) Math.sqrt(ROBOT_WIDTH*ROBOT_WIDTH + ROBOT_LENGTH*ROBOT_LENGTH) + 1;
	
	final private int NUMBER_OF_NODES_PER_EDGE; 
	final private double DISTANCE_BETWEEN_NODES; //in cm
	final private int BORDER_NODE_WIDTH; // in # of nodes
	
	
	//private enum GridState {EMPTY, OPEN, CLOSED};
	
	private AStarNode[][] grid;
	
	//final private int CYLINDER_RADIUS = 3;  // 5.5 cm diameter

	private TreeSet<AStarNode> closedList;
	private TreeSet<AStarNode> openList;
	
	
	public Grid(){
		this(62); //calculated to be 2cm between each node
	}
	

	public Grid(int numberOfNodesPerEdge){
		
		closedList = new TreeSet<AStarNode>();
		openList = new TreeSet<AStarNode>();
		
		NUMBER_OF_NODES_PER_EDGE = numberOfNodesPerEdge;
		DISTANCE_BETWEEN_NODES = (double) COURSE_WIDTH / (double) (NUMBER_OF_NODES_PER_EDGE - 1);
		BORDER_NODE_WIDTH = (int) ((ROBOT_RADIUS + 1) / DISTANCE_BETWEEN_NODES);
		
		//initialise empty grid matrix
//		grid = new GridState[NUMBER_OF_NODES_PER_EDGE][NUMBER_OF_NODES_PER_EDGE];
//		for(int i = 0; i < grid.length; ++i){
//			for(int j = 0; j< grid.length; ++j) grid[i][j] = GridState.EMPTY;
//		}
		grid = new AStarNode[numberOfNodesPerEdge][numberOfNodesPerEdge]; // pointers stored in grid for x,y access
	}
	
	
	/**
	 *  Does A* search after initializing closed list
	 * @param xStart 
	 * @param yStart
	 * @return Either goalNode, with parent chain to root,  or null in result of failure
	 */
	public AStarNode findGoalNodeFromRoot(int xStart, int yStart){
		AStarNode result = null;
		
		// 1. Add closed list stuff
		inputCylinderPosition(50, 50); // we don't know yet
		double[] goalTmp = inputTunnelPosition(40, 122 - 40, 0); // not sure yet
		
		// 1b. Add goal node
		int goalCoord[] = findClosestNode(goalTmp[0], goalTmp[1]);
		AStarNode goal = new AStarNode(goalCoord[0], goalCoord[1]);
		grid[goalCoord[0]][goalCoord[1]] = goal; //add to grid
		
		// 2. Add initial pos to open list
		AStarNode init = new AStarNode(xStart, yStart, manhattanHeuristic(xStart, yStart, goal), 0, null, true);
		openList.add(init);
		grid[xStart][yStart] = init; //add to grid
		
		
		//TODO: write a test to look at the closed list... // also grid
		
		// Array of 'actions'
		final int[][] ACTION = new int[8][2];
		ACTION[0] = new int[]{0,1};
		ACTION[1] = new int[]{1,1};
		ACTION[2] = new int[]{1,0};
		ACTION[3] = new int[]{1,-1};
		ACTION[4] = new int[]{0,-1};
		ACTION[5] = new int[]{-1,-1};
		ACTION[6] = new int[]{-1,0};
		ACTION[7] = new int[]{-1,1};
		
		while (!openList.isEmpty()){
			//for  ...
			AStarNode toExpand = openList.first(); //find node with minimum value
			for (int i = 0; i < ACTION.length; ++i){
				int x = toExpand.getX() + ACTION[i][0];
				int y = toExpand.getY()+ ACTION[i][1];
				double actionCost = (i % 2 == 0) ? 1.0  : RobotMovement.SQRT2;
				
				//AStarNode tmpNodeExamining = new AStarNode(x, y);
				
				
				
				if (isInsideBorder(x , y ) && (grid[x][y] == null || grid[x][y].isOpen()) ){
						//!closedList.contains(tmpNodeExamining) ){
					
					
					// check first if node is already in openlist.
					//if(openList.contains(tmpNodeExamining)){
					if (grid[x][y] != null && grid[x][y].isOpen() ){
						
						// TODO: not sure if contains use   'compareTo or equals'  ... 
						
						// find a way of looking at node in list   that is at position x,y
						double newGn = toExpand.getGn() + actionCost; 
						//if (newGn < /* actualNodeExaming.getGn() */)
					}
					
					
				}
			}
			// expand minimum value node ... in openList
			
			//expansion based on closed list & isInsideBorders() 
			
			// if expansion contains goal, add parent and quickly break;
		}
		
		
		
		
		
		return result;
	}
	
	
	private int[] findClosestNode(double x, double y){
		double tmpx = x / DISTANCE_BETWEEN_NODES;
		double tmpy = y / DISTANCE_BETWEEN_NODES;
		tmpx = Math.round(tmpx);
		tmpy = Math.round(tmpy);
		return new int[]{(int) tmpx, (int) tmpy};
	}
	
	
	//TODO: may cut out below method..(calculatePath)
	
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
	
	
	
	
	
	private int manhattanHeuristic(int x, int y, AStarNode goalNode){
		return Math.abs(x - goalNode.getX()) + Math.abs(y - goalNode.getY());	
	}
	
	private boolean isInsideBorder(int x, int y){
		int tmp1 = NUMBER_OF_NODES_PER_EDGE - BORDER_NODE_WIDTH;
		// TODO: get rid of 'triangle corners'
		// 29.3 cm width of a 'right-angle' triangle
		if ( x > BORDER_NODE_WIDTH && y > BORDER_NODE_WIDTH && x < tmp1 && y < tmp1  ){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Updates the map so that it won't navigate though a CYLINDER OBJECT
	 * CYCLINDER is hardcoded to be 5.5cm diameter
	 * @param x cm from LEFT wall
	 * @param y cm from BOTTOM wall
	 */
	public void inputCylinderPosition(double x, double y){ 
		final double cr = 2.25; // the cylinderRadius
		addWallToClosedList(x - cr, y, x + cr, y);
		addWallToClosedList(x, y + cr, x, y - cr);
	}
	
	/**
	 *  Updates the map so that it won't navigate though a TUNNEL OBJECT
	 * TUNNEL is hardcoded to be 24cm width by 20cm depth   2cm walls
	 * @param x x coordinate of centre of tunnel in cm
	 * @param y x coordinate of centre of tunnel in cm
	 * @param degreesFromSouth  degrees clockwise from south
	 * @return A two element array, containing x & y, of ideal goal position
	 */
	public double[] inputTunnelPosition(double x, double y, double degreesFromSouth){
		double[] result = new double[2];
		/*
		 * use  rotation matrix as reference  : R(xold, yold) to R(xnew, ynew)
		 * [ cos , -sin ]
		 * [ sin , cos  ]
		 */
		//add left wall (centre calculated to be (24 + 2)/2 = 13 away from middle line of wall
		double tmpx, tmpy;
		double tmpxend, tmpyend;
		
		degreesFromSouth = degreesFromSouth * Math.PI / 180.0; // convert to radians to work with math functions.
		double sin = Math.sin(degreesFromSouth);
		double cos = Math.cos(degreesFromSouth);
		tmpx = x - 13;
		tmpy = y - 10;
		tmpxend = tmpx;
		tmpyend = y + 11; // extra cm to go to middle line of back wall
		addWallToClosedList( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend  );
		
		// add back wall
		tmpx = tmpxend;
		tmpy = tmpyend;
		tmpxend = x + 13;
		tmpyend = y + 11;
		addWallToClosedList( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend  );
		
		// add right wall
		tmpx = tmpxend;
		tmpy = tmpyend;
		tmpxend = x + 13;
		tmpyend = y - 10;
		addWallToClosedList( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend  );
		
		// compute a sensible ideal goal to plan to..
		final int DIST_FROM_ENTRANCE_OPENING = 10;
		tmpx = x;
		tmpy = y - 10 - DIST_FROM_ENTRANCE_OPENING;
		
		result[0] = cos * tmpx - sin * tmpy; // x coord 
		result[1] = sin * tmpx + cos * tmpy; // y coord
		
		return result;

	}

	
	private void addWallToClosedList(double xStart, double yStart, double xEnd, double yEnd){
		//double angle = Math.atan( (yEnd - yStart) / (xEnd - xStart));
		
		// work on the 'nodes' scale
		double scale = DISTANCE_BETWEEN_NODES;
		xStart = xStart / scale;
		yStart = yStart / scale;
		xEnd = xEnd / scale;
		yEnd = yEnd / scale;
		
		double gradient = (yEnd - yStart) / (xEnd - xStart);
		double C = yStart - gradient * xStart; //c = y' - mx'
		
		double radiusOfCoverInNodes = (ROBOT_RADIUS + 1) / scale; 
		
		for (int i = (int) Math.ceil(xStart); i < (int) Math.ceil(xEnd); ++i){
			// for each node within radiusOfCover on y-axis, add node to closed list.
			double y = gradient * i + C; 
			for (int j = (int) Math.ceil(y - radiusOfCoverInNodes); j < (int) Math.ceil(y + radiusOfCoverInNodes); ++j){
				closedList.add(new AStarNode(i, j));
			}
		}
		
		for (int i = (int) Math.ceil(yStart); i < (int) Math.ceil(yEnd); ++i){
			// for each node within radiusOfCover on x-axis, add node to closed list.
			double x = (i - C) / gradient; 
			for (int j = (int) Math.ceil(x - radiusOfCoverInNodes); j < (int) Math.ceil(x + radiusOfCoverInNodes); ++j){
				closedList.add(new AStarNode(j, i));
			}
		}
		
	}
	
}
