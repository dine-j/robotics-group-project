package main.term2Challenges;

import java.util.*;

/**
 * Represents the internal map of the course with path planning methods
 */
public class Grid {

    // course is 122 cm x 122cm
    final private int COURSE_WIDTH = 122;  // in cm
    // robot dimensions are 15cm x 20cm,  with wheel-center point (7.5cm,12cm)
    final private int ROBOT_RADIUS = 8; // in cm
    final private int ROBOT_WIDTH = 10;   // Robot is 30x20cm
    final private int ROBOT_LENGTH = 15;
    //final private int ROBOT_RADIUS = (int) Math.sqrt(ROBOT_WIDTH * ROBOT_WIDTH + ROBOT_LENGTH * ROBOT_LENGTH) + 1;

    // Array of 'actions'
    final int[][] ACTION = new int[][]{{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

    final private double NODE_GAP_DIST; //in cm
    final private int NODES_PER_EDGE; // in # of nodes
    final private int BORDER_NODE_WIDTH; // in # of nodes


    /*
     * Nodes are stored as follows in the 'map' , where b's represent the bayesian strip.
     *  -------------------
     * |                   |
     * |                   |
     * |      b            |
     * |   b               |
     * |b                  |
     * 	^------------------
     *  |
     *  Matrix coordinate (0,0)
     */
    private AStarNode[][] grid;

    private TreeSet<AStarNode> closedList;
    private PriorityQueue<AStarNode> openList;


    public Grid() {
        this(62); //calculated to be 2cm between each node
    }

    public Grid(int numberOfNodesPerEdge) {

        closedList = new TreeSet<AStarNode>(new AStarNode.positionComparator());
        openList = new PriorityQueue<AStarNode>();

        NODES_PER_EDGE = numberOfNodesPerEdge;
        NODE_GAP_DIST = (double) COURSE_WIDTH / (double) (NODES_PER_EDGE - 1);
        BORDER_NODE_WIDTH = (int) ((ROBOT_RADIUS) / NODE_GAP_DIST);

        // pointers stored in grid for easy x,y access
        grid = new AStarNode[numberOfNodesPerEdge][numberOfNodesPerEdge];
    }

    public int getSize() {
        return NODES_PER_EDGE;
    }

    public AStarNode[][] getGrid() {
        return grid;
    }

    // method used to approximate goalNodePosition
    private int[] findClosestNode(double x, double y) {
        double tmpx = x / NODE_GAP_DIST;
        double tmpy = y / NODE_GAP_DIST;
        tmpx = Math.round(tmpx);
        tmpy = Math.round(tmpy);
        //debugging println statement
        //System.out.println("closestNodeResults: x = " + tmpx + " , y = " + tmpy);
        return new int[]{(int) tmpx, (int) tmpy};
    }

    /**
     * @param n The index of the Bayesian strip the robot is sensing
     * @return A distance in cm to travel to next node
     */
    public double distanceToNextNodeOnStrip(int n){
    	//21 center till corner-wall, 2cm to center of zeroth cell, -4cm to robot-center
    	double distanceOnDiagonal = 21 + 2 - 4 + n;
    	double xCoord = distanceOnDiagonal / RobotMovement.SQRT2;
    	double[] toReach = nextNodeCoordsOnStrip(n);
    	return (toReach[0] - xCoord) * RobotMovement.SQRT2;
    }

    /**
     * @param n The index of the Bayesian strip the robot is sensing
     * @return Coordinates in cm of next node
     */
    public double[] nextNodeCoordsOnStrip(int n){
    	//21 center till corner-wall, 2cm to center of zeroth cell, -4cm to robot-center
    	double distanceOnDiagonal = 21 + 2 - 4 + n;
    	double x, y;
    	x = y = (distanceOnDiagonal / RobotMovement.SQRT2) / NODE_GAP_DIST;
    	x = y = Math.ceil(x) * NODE_GAP_DIST;
    	return new double[]{x, y};
    }

    // TODO: test the A* search

    /**
     * Does A* search after initialising closed list
     *
     * @param xStart
     * @param yStart
     * @return Either goalNode, with parent chain to root,  or null in result of failure
     */
    public AStarNode findGoalNodeFromRoot(int xStart, int yStart, int xGoal, int yGoal) {
        AStarNode result = null;

        // 1. Add closed list stuff
        //obstacle position
        inputCylinderPosition(40, 122 - 40); // we don't know yet
        inputCorners();



        //middle point of the tunnel
        //double[] goalIdeal = inputTunnelPosition(90, 90, 90);

        /*
         * TODO:  why flipping works?
         * //TODO:check coordinates of goal node
         */
        //int[] goalTmp = new int[]{(int) goalIdeal[1], (int) goalIdeal[0]};

        // Maybe good idea:
        inputWallPosition(20, 0, 122, 100, 1);   // 'invisible' wall to reduce search-space

        // 1b. Add goal node
        int goalCoord[] = findClosestNode(xGoal, yGoal);
        AStarNode goal = new AStarNode(goalCoord[0], goalCoord[1], true); //create goal node
        grid[goalCoord[0]][goalCoord[1]] = goal; //add to grid

        // 2. Add initial pos to open list
        int initCoord[] = findClosestNode(xStart, yStart);
        AStarNode init = new AStarNode(initCoord[0], initCoord[1], manhattanHeuristic(xStart, yStart, goal), 0, null);
        openList.add(init);
        grid[initCoord[0]][initCoord[1]] = init; //add to grid

        while (!openList.isEmpty()) {
            AStarNode toExpand = openList.poll(); //find node with minimum value
            for (int i = 0; i < ACTION.length; ++i) {
                final int x = toExpand.getX() + ACTION[i][0];
                final int y = toExpand.getY() + ACTION[i][1];
                double actionCost = (i % 2 == 0) ? 1.0 : RobotMovement.SQRT2;

                //exit out method if found goal
                if (x == goal.getX() && y == goal.getY()) {
                    goal.setParent(toExpand);
                    return goal;
                }
                // filter out so inside border and open/empty in grid
                else if (isInsideBorder(x, y) && (grid[x][y] == null || grid[x][y].isOpen())) {
                    // check first if node is already in openList.
                    if (grid[x][y] != null && grid[x][y].isOpen()) {
                        double newGn = toExpand.getGn() + actionCost;

                        if (grid[x][y].getGn() > newGn) {
                            AStarNode rmNode = grid[x][y];
                            openList.remove(rmNode); //remove temporarily so doesn't mess up tree-set structure
                            rmNode.setGn(newGn);
                            rmNode.setParent(toExpand);  //set the new parent with better costed path
                            openList.add(rmNode);
                        }
                    } else if (grid[x][y] == null) {
                        //add to openlist
                        double hn = manhattanHeuristic(x, y, goal);
                        AStarNode toAdd = new AStarNode(x, y, hn, 0, toExpand);
                        openList.add(toAdd);
                        grid[x][y] = toAdd; //add to grid
                    }
                    // otherwise if closed do nothing
                }
            }// end of for loop
            AStarNode rmNode = grid[toExpand.getX()][toExpand.getY()]; //have finished expanding
            openList.remove(rmNode);
            closedList.add(rmNode);
            rmNode.setClosed(); // add to closed list
        }

        return result; //only gets here if result is null
    }

    //gets path by backtracking through parents
    public LinkedList<AStarNode> getListPathFromGoalNode(AStarNode goal) {
        LinkedList<AStarNode> list = new LinkedList<AStarNode>();
        list.add(goal);
        AStarNode current = goal;
        while (!current.isRoot()) {
            current = current.getParent();
            list.addFirst(current);
        }
        return list;
    }

    /**
     * After doing A* search, parses the path to a list of actions to follow
     *
     * @param
     * @param
     * @return A list of Actions for robot to follow
     */
    public List<RobotMovement> calculatePath(LinkedList<AStarNode> path) {
        int direction = RobotMovement.NW;
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
                list.add(changeDirection(direction, newDirection));
            }
            direction = newDirection;
            if(direction == RobotMovement.NE || direction == RobotMovement.NW)
                list.add(RobotMovement.FORWARD_ON_DIAGONAL);
            else
                list.add(RobotMovement.FORWARD);
            x = nextNode.getX();
            y = nextNode.getY();
        }
        return list;
    }

    private RobotMovement changeDirection(int direction, int newDirection) {
        switch (direction - newDirection) {
            case -2:
                return RobotMovement.LEFT90;
            case -1:
                return RobotMovement.LEFT45;
            case 1:
                return RobotMovement.RIGHT45;
            case 2:
                return RobotMovement.RIGHT90;
        }
        return null;
    }

    private int getDirectionToGoal(int xChange, int yChange) {
//        System.out.println("Change in x: " + xChange + ", change in y: " + yChange);
        if(xChange == 0) { // Next square if at left or right
            if(yChange > 0) // Next square at right
                return RobotMovement.E;
            else
                return RobotMovement.W;
        }
        if(yChange == 0) { // Up or down
            if(xChange > 0) // Up
                return RobotMovement.N;
        }
        if(xChange == 1 && yChange == 1)
            return RobotMovement.NW;
        if(xChange == 1 && yChange == -1)
            return RobotMovement.NE;
        return -1;
    }

    private int manhattanHeuristic(int x, int y, AStarNode goalNode) {
        return Math.abs(x - goalNode.getX()) + Math.abs(y - goalNode.getY());
    }


    public boolean isInsideBorder(int x, int y) {
        int tmp1 = NODES_PER_EDGE - BORDER_NODE_WIDTH;
        if (x > BORDER_NODE_WIDTH && y > BORDER_NODE_WIDTH && x < tmp1 && y < tmp1) {
            return true;
        }
        return false;
    }

    public double getNodeSize() {
        return NODE_GAP_DIST;
    }

/////////////////////////////////////////////////////////////////////////////
// below are the object placing methods, for map/grid
/////////////////////////////////////////////////////////////////////////////

    /**
     * Updates the map so that it won't navigate though a CYLINDER OBJECT
     * CYCLINDER is hardcoded to be 5.5cm diameter
     *
     * @param x cm from LEFT wall
     * @param y cm from BOTTOM wall
     */
    public void inputCylinderPosition(double x, double y) {
        final double cr = 2.25; // the cylinderRadius
        inputCirclePos(x / NODE_GAP_DIST, y / NODE_GAP_DIST, (cr + ROBOT_RADIUS) / NODE_GAP_DIST);
    }

    /**
     * Adds corners into the closed list
     */
    public void inputCorners() {
        double dist = 29.3 / NODE_GAP_DIST; // in nodes
        double r = ROBOT_RADIUS / NODE_GAP_DIST;
        double w = COURSE_WIDTH / NODE_GAP_DIST;
        inputSlantRectangle(dist, 0, 0, dist, r);
        inputSlantRectangle(w - dist, w, w, w - dist, r);
    }

    /**
     * Updates the map so that it won't navigate though a TUNNEL OBJECT
     * TUNNEL is hardcoded to be 24cm width by 20cm depth   2cm walls
     *
     * @param x       x coordinate of centre of tunnel in cm
     * @param y       x coordinate of centre of tunnel in cm
     * @param degrees degrees clockwise from south
     * @return A two element array, containing x & y, of ideal goal position
     */
    public double[] inputTunnelPosition(double x, double y, double degrees) {
        double[] result = new double[2];
        //add left wall (centre calculated to be (24 + 2)/2 = 13 away from middle line of wall
        double tmpx, tmpy;
        double tmpxend, tmpyend;

        double radians = degrees * (Math.PI / 180.0); // convert to radians to work with math functions.
        tmpx = x - 13;
        tmpy = y - 10;
        double[] frontLeft = rotateVector(new double[]{tmpx, tmpy}, x, y, radians);
        tmpxend = tmpx;
        tmpyend = y + 11; // extra cm to go to middle line of back wall
        tmpx = tmpxend;
        tmpy = tmpyend;
        double[] backLeft = rotateVector(new double[]{tmpx, tmpy}, x, y, radians);
        tmpxend = x + 13;
        tmpyend = y + 11;
        tmpx = tmpxend;
        tmpy = tmpyend;
        double[] backRight = rotateVector(new double[]{tmpx, tmpy}, x, y, radians);
        tmpxend = x + 13;
        tmpyend = y - 10;
        double[] frontRight = rotateVector(new double[]{tmpxend, tmpyend}, x, y, radians);

        //add the walls
        inputWallPosition(frontLeft[0], frontLeft[1], backLeft[0], backLeft[1], ROBOT_RADIUS);
        inputWallPosition(backLeft[0], backLeft[1], backRight[0], backRight[1], ROBOT_RADIUS);
        inputWallPosition(backRight[0], backRight[1], frontRight[0], frontRight[1], ROBOT_RADIUS);

        // compute a sensible ideal goal to plan to..
        final int DIST_FROM_ENTRANCE_OPENING = 5;
        final int DEPTH_TO_CENTER = 10;

        result = rotateVector(new double[]{x, y - DEPTH_TO_CENTER - DIST_FROM_ENTRANCE_OPENING}, x, y, radians);

        return result;

    }

    /**
     * Updates the map so that it won't navigate though a WALL OBJECT
     *
     * @param xStart in cm
     * @param yStart in cm
     * @param xEnd   in cm
     * @param yEnd   in cm
     * @param radius
     */
    public void inputWallPosition(double xStart, double yStart, double xEnd, double yEnd, double radius) {
        // work on the 'nodes' scale
        double scale = NODE_GAP_DIST;
        xStart = xStart / scale;
        yStart = yStart / scale;
        xEnd = xEnd / scale;
        yEnd = yEnd / scale;
        radius = radius / scale;
        inputSlantRectangle(xStart, yStart, xEnd, yEnd, radius);
        inputCirclePos(xStart, yStart, radius);
        inputCirclePos(xEnd, yEnd, radius);
    }

    /**
     * Works on 'NodeScale'
     *
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     * @param radius
     */
    private void inputSlantRectangle(double xStart, double yStart, double xEnd, double yEnd, double radius) {
        if (xStart == xEnd || yStart == yEnd) {
            if (xStart == xEnd && yStart == yEnd) {
            } //don't do anything
            else if (yStart == yEnd) {
                int top = (int) Math.floor(yStart + radius);
                int bottom = (int) Math.ceil(yStart - radius);
                for (int i = top; i >= bottom; --i) {
                    int begin = (int) Math.ceil(Math.min(xEnd, xStart));
                    double end = Math.max(xEnd, xStart);
                    for (int j = begin; j < end; ++j) {
                        addClosedListNode(i, j);
                    }
                } //end of for loop
            } else {
                int top = (int) Math.floor(Math.max(yStart, yEnd));
                int bottom = (int) Math.ceil(Math.min(yStart, yEnd));
                for (int i = top; i >= bottom; --i) {
                    int begin = (int) Math.ceil(xStart - radius);
                    double end = xStart + radius;
                    for (int j = begin; j < end; ++j) {
                        addClosedListNode(i, j);
                    }
                } //end of for loop
            }// end of grid-aligned condition
        } else {
            LinkedList<double[]> pnts = new LinkedList<double[]>();
            double centerX = (xStart + xEnd) / 2;
            double centerY = (yStart + yEnd) / 2;
            double len = Math.sqrt((xEnd - xStart) * (xEnd - xStart) + (yEnd - yStart) * (yEnd - yStart));
            double m = (yEnd - yStart) / (xEnd - xStart);
            double angle = Math.atan(m); //rotate anti-clockwise
            pnts.add(new double[]{centerX - len / 2, centerY + radius});
            pnts.add(new double[]{centerX - len / 2, centerY - radius});
            pnts.add(new double[]{centerX + len / 2, centerY + radius});
            pnts.add(new double[]{centerX + len / 2, centerY - radius});
            for (double[] element : pnts) {
                double[] rotated = rotateVector(element, centerX, centerY, angle);
                element[0] = rotated[0];
                element[1] = rotated[1];
            }

            Collections.sort(pnts, new Comparator<double[]>()  // sort by y values
            {
                @Override
                public int compare(double[] o1, double[] o2) {
                    return new Double(o1[1]).compareTo(new Double(o2[1]));
                }
            });

            double[] lowestPnt = pnts.removeFirst();
            double[] highestPnt = pnts.removeLast();
            double[] otherPnt1 = pnts.pop();
            double[] otherPnt2 = pnts.pop();

            double[][] lineEqs = new double[][]{findLineEq(highestPnt, otherPnt1), findLineEq(highestPnt, otherPnt2),
                    findLineEq(lowestPnt, otherPnt1), findLineEq(lowestPnt, otherPnt2)
            };

            double y = lowestPnt[1];
            double h = highestPnt[1] - y;
            double x = Math.min(otherPnt1[0], otherPnt2[0]);
            double w = Math.max(otherPnt1[0], otherPnt2[0]) - x;

            inputBy4LineBounds(x, y, h, w, lineEqs);
        }
    }

    //need to input NNSS,  +ve grad, draw rect =end,left,start,right
    //-ve grad drawrect = end,right,start,left .. I.e highest,highest,lowest,lowest
    private void inputBy4LineBounds(double boxXCoord, double boxYCoord, double boxHeight, double boxWidth, double[][] lineEqs) {
        boolean[] north = new boolean[]{true, true, false, false};
        if (lineEqs.length > 4) throw new IllegalArgumentException("upto four line equations!");
        for (int i = (int) Math.ceil(boxYCoord); i < boxYCoord + boxHeight; ++i) {
            for (int j = (int) Math.ceil(boxXCoord); j < boxXCoord + boxWidth; ++j) {
                boolean yesAddPlease = true;
                for (int k = 0; k < lineEqs.length; ++k) {
                    //check line equ
                    if (north[k]) {
                        if (i > lineEqs[k][0] * j + lineEqs[k][1]) yesAddPlease = false;
                    } else {
                        if (i < lineEqs[k][0] * j + lineEqs[k][1]) yesAddPlease = false;
                    }
                }
                if (yesAddPlease) addClosedListNode(i, j);
            }
        }
    }


    // x^2 + y^2 = r^2

    /**
     * Works on the 'NodeScale'
     *
     * @param xNodeCoord
     * @param yNodeCoord
     * @param r
     */
    private void inputCirclePos(double xNodeCoord, double yNodeCoord, double r) {
        int highest = (int) Math.floor(yNodeCoord + r);
        int lowest = (int) Math.ceil(yNodeCoord - r);

        for (int i = highest; i >= lowest; --i) {
            double y = yNodeCoord - i;
            double change = Math.sqrt(r * r - y * y);

            int xmin = (int) Math.ceil(xNodeCoord - change);
            int xmax = (int) Math.floor(xNodeCoord + change);
            for (int j = xmin; j <= xmax; ++j) {
                addClosedListNode(i, j);
            }
        }
    }

    /*
     * inputed into the ith row and jth column
     */
    private void addClosedListNode(int i, int j) { //debugging, tmp disable bordercheck
        if (isInsideBorder(i, j)) {
            AStarNode toAdd = new AStarNode(i, j);
            closedList.add(toAdd);
            grid[i][j] = toAdd;
        }
    }

    public static double[] rotateVector(double[] vector, double centerX, double centerY, double radians) {
        if (vector.length != 2) throw new IllegalArgumentException();
        //line center to vector // cv
        double[] toRotate = new double[]{centerX - vector[0], centerY - vector[1]};
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double[] toAdd = new double[]{cos * toRotate[0] - sin * toRotate[1],
                sin * toRotate[0] + cos * toRotate[1]};
        double[] result = new double[]{centerX + toAdd[0], centerY + toAdd[1]};

        for (int i = 0; i < 2; ++i) {
            result[i] = 1e-12 * Math.rint(1e12 * result[i]);  // discards small multiplication errors
        }
//		System.out.println("x " + result[0] + "   y " + result[1]);
        return result;
    }


    public static double[] findLineEq(double x1, double y1, double x2, double y2) {
        double m = (y1 - y2) / (x1 - x2);
        double c = y1 - m * x1;
        return new double[]{m, c};
    }

    public static double[] findLineEq(double[] a, double[] b) {
        double m = (a[1] - b[1]) / (a[0] - b[0]);
        double c = a[1] - m * a[0];
        return new double[]{m, c};
    }
}
