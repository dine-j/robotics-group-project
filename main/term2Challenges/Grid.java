package main.term2Challenges;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.ArrayList;
/**
 * Represents the map of nodes, to use in the path finding algorithm
 *
 * MAY HAVE FORGOTTON ORIENTATION
 * The bottom right hand corner of the grid layout, is the closest corner in line with the bayesian strip
 */
public class Grid {
	
	// course is 122 cm x 122cm 
	final private int COURSE_WIDTH = 122;  // in cm
	final private int ROBOT_WIDTH = 10 ;   // Robot is 30x20cm 
	final private int ROBOT_LENGTH = 15;
	final private int ROBOT_RADIUS = (int) Math.sqrt(ROBOT_WIDTH*ROBOT_WIDTH + ROBOT_LENGTH*ROBOT_LENGTH) + 1;
	
	// Array of 'actions'
	final int[][] ACTION = new int[][]{{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};
	
	final private int NUMBER_OF_NODES_PER_EDGE; 
	final private double DISTANCE_BETWEEN_NODES; //in cm
	final private int BORDER_NODE_WIDTH; // in # of nodes
	
	private AStarNode[][] grid;

	private TreeSet<AStarNode> closedList;
	private TreeSet<AStarNode> openList;
	
	
	public Grid(){
		this(62); //calculated to be 2cm between each node
	}
	
	public Grid(int numberOfNodesPerEdge){
		
		closedList = new TreeSet<AStarNode>(new AStarNode.positionComparator());   //DONE: closedList has comparator
		openList = new TreeSet<AStarNode>();
		
		NUMBER_OF_NODES_PER_EDGE = numberOfNodesPerEdge;
		DISTANCE_BETWEEN_NODES = (double) COURSE_WIDTH / (double) (NUMBER_OF_NODES_PER_EDGE - 1);
		BORDER_NODE_WIDTH = (int) ((ROBOT_LENGTH) / DISTANCE_BETWEEN_NODES);
		
		grid = new AStarNode[numberOfNodesPerEdge][numberOfNodesPerEdge]; // pointers stored in grid for x,y access
	} //UNSURE: grid may not be initialised fully...
	
	public int getSize(){
		return NUMBER_OF_NODES_PER_EDGE;
	}
	
	public AStarNode[][] getGrid(){
		return grid;
	}
	
	/**
	 * @param x in cm
	 * @param y in cm
	 * @return the closest node in 'node coordinates'
	 */
	public int[] findClosestNode(double x, double y){
		double tmpx = x / DISTANCE_BETWEEN_NODES;
		double tmpy = y / DISTANCE_BETWEEN_NODES;
		tmpx = Math.round(tmpx);
		tmpy = Math.round(tmpy);
		return new int[]{(int) tmpx, (int) tmpy};
	}
	
	/**
	 * @param x in cm
	 * @param y in cm
	 * @param diagonal if set to true, attempt to find closest 'forward' node on leading diagonal
	 * @return the closest node in 'node coordinates',  also in 3rd array index the distance needed to travel on diagonal in mm
	 */
	public int[] findClosestNode(double x, double y, boolean diagonal){
		if(diagonal){
			double tmpx = Math.ceil(x/DISTANCE_BETWEEN_NODES);
			double tmpy = Math.ceil(y/DISTANCE_BETWEEN_NODES);
			double remainder = (x - tmpx * DISTANCE_BETWEEN_NODES) * 10 * RobotMovement.SQRT2;
			return new int[]{(int) tmpx, (int) tmpy, (int) remainder};
		}else return findClosestNode(x, y);
	}
	
	/**
	 *  Does A* search after initialising closed list
	 * @param xStart 
	 * @param yStart
	 * @return Either goalNode, with parent chain to root,  or null in result of failure
	 */
	public AStarNode findGoalNodeFromRoot(int xStart, int yStart){
		AStarNode result = null;
		
		// 1. Add closed list stuff
		inputCylinderPosition(50, 50); // we don't know yet
		//addCornersToClosedList(); //not tested
		int[] goalTmp = inputTunnelPosition(40, 122 - 40, 0); // not sure yet
		
		// 1b. Add goal node
		int goalCoord[] = findClosestNode(goalTmp[0], goalTmp[1]);
		AStarNode goal = new AStarNode(goalCoord[0], goalCoord[1]);
		grid[goalCoord[0]][goalCoord[1]] = goal; //add to grid
		
		// 2. Add initial pos to open list
		AStarNode init = new AStarNode(xStart, yStart, manhattanHeuristic(xStart, yStart, goal), 0, null, true);
		openList.add(init);
		grid[xStart][yStart] = init; //add to grid
		
		while (!openList.isEmpty()){
			AStarNode toExpand = openList.first(); //find node with minimum value
			for (int i = 0; i < ACTION.length; ++i){
				final int x = toExpand.getX() + ACTION[i][0];
				final int y = toExpand.getY()+ ACTION[i][1];
				double actionCost = (i % 2 == 0) ? 1.0  : RobotMovement.SQRT2;
				
				//exit out method if found goal
				if (x == goal.getX() && y == goal.getY()){
					goal.setParent(toExpand);
					return goal;
				}
				// filter out so inside border and open/empty in grid
				else if (isInsideBorder(x , y ) && (grid[x][y] == null || grid[x][y].isOpen()) ){
					// check first if node is already in openlist.
					if (grid[x][y] != null && grid[x][y].isOpen() ){
						double newGn = toExpand.getGn() + actionCost; 
						
						if(grid[x][y].getGn() > newGn){
							AStarNode rmNode = grid[x][y];
							openList.remove(rmNode); //remove tempoarily so doesn't mess up tree-set structure
							rmNode.setGn(newGn);
							rmNode.setParent(toExpand);  //set the new parent with better costed path
							openList.add(rmNode); 
						}
					} else if(grid[x][y] == null){
						//add to openlist
						double hn = manhattanHeuristic(xStart, yStart, goal);
						AStarNode toAdd = new AStarNode(xStart, yStart, hn, 0, toExpand, true);
						openList.add(toAdd);
						grid[x][y] = toAdd; //add to grid
					}
					// otherwise if closed do nothing
				}// end of for loop
				AStarNode rmNode = grid[x][y];
				openList.remove(rmNode);
				closedList.add(rmNode);
				rmNode.setClosed(); // add to closed list
			}	
		}

		return result; //only gets here if result is null
	}
	
	/* helper method for calculatePath() */
	private LinkedList<AStarNode> getListPathFromGoalNode(AStarNode goal){
		LinkedList<AStarNode> list = new LinkedList<AStarNode>();
		list.add(goal);
		AStarNode current = goal;
		while(!current.isRoot()){
			current = current.getParent();
			list.addFirst(current);
		}
		return list;
	}
		
	/**
	 * After doing A* search, parses the path to a list of actions to follow
	 * @param xStart
	 * @param yStart
	 * @return A list of Actions for robot to follow
	 */
	public ArrayList<RobotMovement> calculatePath(int xStart, int yStart){
		LinkedList<AStarNode> path = getListPathFromGoalNode(findGoalNodeFromRoot(xStart,yStart));
		int direction = RobotMovement.NE;
		ArrayList<RobotMovement> list = new ArrayList<RobotMovement>();
		AStarNode startNode = path.remove();
		int x = startNode.getX();
		int y = startNode.getY();
		
		while(!path.isEmpty()){
			AStarNode nextNode = path.remove();
			int changeInX = nextNode.getX() - x;
			int changeInY = nextNode.getY() - y;
			
			int newDirection = 0; // default
			for(int i = 0; i < ACTION.length; ++i){
				if(changeInX - ACTION[i][0] == 0 && changeInY - ACTION[i][1] == 0) newDirection = i;
			}
			
			int directionChange = (newDirection - direction) % 8 - 3;
			switch (directionChange){
			case -3: list.add(RobotMovement.LEFT135);
				break;
			case -2: list.add(RobotMovement.LEFT90);
				break;
			case -1: list.add(RobotMovement.LEFT45);
				break;
			case 1: list.add(RobotMovement.RIGHT45);
				break;
			case 2: list.add(RobotMovement.RIGHT90);
				break;
			case 3: list.add(RobotMovement.RIGHT135);
				break;
			case 4: list.add(RobotMovement.RIGHT180);
			}
			
			if (newDirection % 2 == 0){
				list.add(RobotMovement.FORWARD);
			}else{
				list.add(RobotMovement.FORWARD_ON_DIAGONAL);
			}
		}	
		return list;
	}
	
	
	private int manhattanHeuristic(int x, int y, AStarNode goalNode){
		return Math.abs(x - goalNode.getX()) + Math.abs(y - goalNode.getY());	
	}
	
	
	public boolean isInsideBorder(int x, int y){
		int tmp1 = NUMBER_OF_NODES_PER_EDGE - BORDER_NODE_WIDTH;
		// 29.3 cm width of a 'right-angle' triangle
		if ( x > BORDER_NODE_WIDTH && y > BORDER_NODE_WIDTH && x < tmp1 && y < tmp1  ){
			return true;
		}
		return false;
	}
	
/////////////////////////////////////////////////////////////////////////////
// below are the object placing methods, for map/grid
/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates the map so that it won't navigate though a CYLINDER OBJECT
	 * CYCLINDER is hardcoded to be 5.5cm diameter
	 * @param x cm from LEFT wall
	 * @param y cm from BOTTOM wall
	 */
	public void inputCylinderPosition(double x, double y){ 
		final double cr = 2.25; // the cylinderRadius
		inputCirclePos(x/ DISTANCE_BETWEEN_NODES, y/ DISTANCE_BETWEEN_NODES, cr +ROBOT_LENGTH);
	}
	
	
	public void inputCorners(){
		double dist = 29.3/DISTANCE_BETWEEN_NODES; // in nodes
		double r = ROBOT_LENGTH/DISTANCE_BETWEEN_NODES;
		double w = COURSE_WIDTH/DISTANCE_BETWEEN_NODES;
		inputSlantRectangle(dist, 0, 0, dist, r);
		inputSlantRectangle(w - dist, w, w, w - dist, r);
		inputSlantRectangle(0, w - dist, dist, w, r);
		inputSlantRectangle(w - dist,0 , w, dist, r);
		
//		double dist = 29.3; // in nodes
//		inputWallPosition(dist, 0, 0, dist, ROBOT_LENGTH);
//		inputWallPosition(COURSE_WIDTH - dist, COURSE_WIDTH, COURSE_WIDTH, COURSE_WIDTH - dist, ROBOT_LENGTH);
//		inputWallPosition(0, COURSE_WIDTH - dist, dist, COURSE_WIDTH, ROBOT_LENGTH);
//		inputWallPosition(COURSE_WIDTH - dist,0 , COURSE_WIDTH, dist, ROBOT_LENGTH);
	}
	
	/**
	 *  Updates the map so that it won't navigate though a TUNNEL OBJECT
	 * TUNNEL is hardcoded to be 24cm width by 20cm depth   2cm walls
	 * @param x x coordinate of centre of tunnel in cm
	 * @param y x coordinate of centre of tunnel in cm
	 * @param degreesFromSouth  degrees clockwise from south
	 * @return A two element array, containing x & y, of ideal goal position
	 */
	//TODO: correct/debug this method ::: MATRIX ROTATION NOT WORKING
	public int[] inputTunnelPosition(double x, double y, double degreesFromSouth){
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
		inputWallPosition( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend, ROBOT_WIDTH);
		
		// add back wall
		tmpx = tmpxend;
		tmpy = tmpyend;
		tmpxend = x + 13;
		tmpyend = y + 11;
		inputWallPosition( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend ,ROBOT_WIDTH );
		
		// add right wall
		tmpx = tmpxend;
		tmpy = tmpyend;
		tmpxend = x + 13;
		tmpyend = y - 10;
		inputWallPosition( cos * tmpx - sin * tmpy, sin * tmpx + cos * tmpy,
				cos * tmpxend - sin * tmpyend, sin * tmpxend + cos * tmpyend ,ROBOT_WIDTH);
		
		// compute a sensible ideal goal to plan to..
		final int DIST_FROM_ENTRANCE_OPENING = 15;
		result[0] = x + sin * (10 + DIST_FROM_ENTRANCE_OPENING); // x coord 
		result[1] = y - cos * (10 + DIST_FROM_ENTRANCE_OPENING); // y coord
		
		//return findClosestNode(x,y - 20);//findClosestNode(x - sin * (10 - DIST_FROM_ENTRANCE_OPENING), y + cos * (10 - DIST_FROM_ENTRANCE_OPENING));
		int tmp[] = findClosestNode(result[0], result[1]);
		for(int i= 0; i < tmp.length; ++i){
			System.out.println(tmp[i]);
		}
		
		return findClosestNode(result[1], result[0]);

	}

	/**
	 * Updates the map so that it won't navigate though a WALL OBJECT
	 * @param xStart in cm
	 * @param yStart in cm
	 * @param xEnd in cm
	 * @param yEnd in cm
	 * @param radius
	 */
	public void inputWallPosition(double xStart, double yStart, double xEnd, double yEnd, double radius){
		// work on the 'nodes' scale
		double scale = DISTANCE_BETWEEN_NODES;
		xStart = xStart / scale;
		yStart = yStart / scale;
		xEnd = xEnd / scale;
		yEnd = yEnd / scale;
		radius = radius / scale; 
		inputSlantRectangle(xStart, yStart, xEnd, yEnd, radius);
		inputCirclePos(xStart, yStart, radius);
		inputCirclePos(xEnd, yEnd, radius);
	}

	//TODO: debug (fix trig, or write, rotate((centerx,centery), angle) & lineEq((x1,y1),(x2,y2)) method
	/**
	 * Works on 'NodeScale'
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @param radius
	 */
	/*private*/public void inputSlantRectangle(double xStart, double yStart, double xEnd, double yEnd, double radius){
		if (xStart == xEnd ||  yStart== yEnd){
			if(xStart == xEnd &&  yStart== yEnd){} //don't do anything
			else if (yStart == yEnd) {
				int top = (int) Math.floor(yStart + radius);
				int bottom = (int) Math.ceil(yStart - radius);
				for (int i = top; i >= bottom; --i){
					int begin = (int) Math.ceil(Math.min(xEnd,xStart));
					double end = Math.max(xEnd, xStart);
					for (int j = begin; j < end; ++j){
						addClosedListNode(i, j);
					}
				} //end of for loop
			}else {
				int top = (int) Math.floor(Math.max(yStart,yEnd));
				int bottom = (int) Math.ceil(Math.min(yStart,yEnd));
				for (int i = top; i >= bottom; --i){
					int begin = (int) Math.ceil(xStart - radius);
					double end = xStart + radius;
					for (int j = begin; j < end; ++j){
						addClosedListNode(i, j);
					}
				} //end of for loop
			}// end of grid-aligned condition
		}
		else{
			if(yEnd < yStart){
				double tmp=yStart; yStart=yEnd; yEnd=tmp; 
				tmp=xStart; xStart=xEnd; xEnd=tmp;
			}
			double m = (yEnd - yStart) / (xEnd - xStart); //calculate the gradient
			double angle = Math.atan(m);
			double xDelta = Math.abs(radius * Math.sin( angle));
			double yDelta = Math.abs(radius * Math.cos( angle));
			double c = yStart - m * xStart; //c = y - mx
			double c1 = c + yDelta;
			double c2 = c - yDelta;
			
			// find perpendicular line equations at start and end
			double m_ = -1/m;   //m cannot be zero :(
			double c3 = yStart - m_ * xStart; //c = y - mx
			double c4 = yEnd - m_ * xEnd; //c = y - mx
			double[][] lineEqs = new double[][]{{m,c1}, {m_,c4},{m_,c3}, {m,c2}};
			//bottom left of box
			double x = Math.min(xStart,xEnd) - xDelta;
			double y = Math.min(yStart,yEnd) - yDelta;
			double h = yDelta + Math.max(yStart,yEnd) - y;
			double w = xDelta + Math.max(xStart,xEnd) - x;
			
			inputBy4LineBounds(x,y,h,w, lineEqs);
		}
	}
	
	//need to input NNSS,  +ve grad, draw rect =end,left,start,right
	//-ve grad drawrect = end,right,start,left .. I.e highest,highest,lowest,lowest
	public void inputBy4LineBounds(double boxXCoord, double boxYCoord, double boxHeight, double boxWidth, double[][] lineEqs ){
		boolean[] north = new boolean[]{true,true,false,false};
		if(lineEqs.length > 4) throw new IllegalArgumentException("upto four line equations!");
		for(int i = (int) Math.ceil(boxYCoord); i < boxYCoord + boxHeight; ++i ){
			for(int j = (int) Math.ceil(boxXCoord); j < boxXCoord + boxWidth; ++j ){
				boolean yesAddPlease = true;
				for(int k = 0; k < lineEqs.length; ++k){
					//check line equ
					if(north[k]){
						if(i > lineEqs[k][0] * j + lineEqs[k][1]) yesAddPlease = false;
					}else{
						if(i < lineEqs[k][0] * j + lineEqs[k][1]) yesAddPlease = false;
					}
				}
				if(yesAddPlease) addClosedListNode(i, j);
			}
		}
	}
	
	
	// x^2 + y^2 = r^2
	/**
	 * Works on the 'NodeScale'
	 * @param xNodeCoord
	 * @param yNodeCoord
	 * @param r
	 */
	private void inputCirclePos(double xNodeCoord, double yNodeCoord, double r){
		int highest = (int)Math.floor( yNodeCoord + r);
		int lowest = (int)Math.ceil( yNodeCoord - r);
		
		for(int i = highest; i >= lowest; --i){
			double y = yNodeCoord - i;
			double change = Math.sqrt(r * r - y * y);
			
			int xmin = (int) Math.ceil(xNodeCoord - change);
			int xmax = (int) Math.floor(xNodeCoord + change);
			for(int j = xmin; j <= xmax; ++j){
				addClosedListNode(i,j);
			}
		}
	}
	
	/*
	 * inputed into the ith row and jth column
	 */
	private void addClosedListNode(int i, int j){ //debugging, tmp disable bordercheck
		if (isInsideBorder(i, j)){
			AStarNode toAdd = new AStarNode(i, j);
			closedList.add(toAdd);
			grid[i][j] = toAdd;
		}
	}
	
	public static double[] rotateVector(double[] vector, double centerX, double centerY, double degrees){
		if (vector.length != 2) throw new IllegalArgumentException();
		
		System.out.println("degrees:" + degrees);
		
		double radians = degrees * Math.PI/180;
		System.out.println("radians:" + radians);
		//line center to vector // cv
		double[] toRotate =  new double[]{centerX - vector[0], centerY - vector[1]};
		
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		
		double[] toAdd = new double[]{cos * toRotate[0] - sin * toRotate[1] ,
				sin * toRotate[0] + cos * toRotate[1]};
		double[] result = new double[]{centerX + toAdd[0], centerY + toAdd[1]};
		
		System.out.println("x " + result[0] + "   y " + result[1]);
		
		
		return result;
	}
	
}
