package main.term2Challenges;

import java.util.*;

/**
 * Represents the internal map of the course with path planning methods
 */
public class Grid {

    // robot dimensions are 15cm x 20cm,  with wheel-center point (7.5cm,12cm)
    final private static int ROBOT_RADIUS = 8; // in cm
    
    //number of definitely inaccessible nodes away from course-edges
    final private static int BORDER_NODE_WIDTH = (int) ((ROBOT_RADIUS) / GridGeo.NODE_GAP_DIST); 
    
    
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
        this(GridGeo.NODES_PER_EDGE);
    }

    private Grid(int numberOfNodesPerEdge) {
        closedList = new TreeSet<AStarNode>(new AStarNode.PositionComparator());
        openList = new PriorityQueue<AStarNode>();
        // pointers stored in grid for easy x,y access
        grid = new AStarNode[numberOfNodesPerEdge][numberOfNodesPerEdge]; 
    }

    public int getSize() {
        return grid.length;
    }

    public AStarNode[][] getGrid() {
        return grid;
    }
    
    private static int manhattanHeuristic(int x, int y, AStarNode goalNode) {
        return Math.abs(x - goalNode.getX()) + Math.abs(y - goalNode.getY());
    }

    /**
     * Does A* search after initialising closed list
     * precondition: (xStart, yStart) in cm and should line up with a node.
     */
    public AStarNode aStarSearch(double xStart, double yStart) {
        AStarNode result = null; //default return if no path found

        // 1. Add closed list stuff & get cm coordinates of goalNode
        double[] goalIdeal = initClosedList();

        // 1b. Add goal node
        int goalNodeXY[] = GridGeo.closestNodeInNodeCoords((int) goalIdeal[1], (int) goalIdeal[0]); //TODO: why reversed?
        AStarNode goal = new AStarNode(goalNodeXY[0], goalNodeXY[1], true);
        grid[goalNodeXY[0]][goalNodeXY[1]] = goal; //add to grid

        // 2. Add initial pos to open list
        int[] XY = GridGeo.closestNodeInNodeCoords(xStart, yStart);
        int xNodeStart = XY[0];
        int yNodeStart = XY[1];
        
        AStarNode init = new AStarNode(xNodeStart, yNodeStart, manhattanHeuristic(xNodeStart, yNodeStart, goal), 0, null, true);
        openList.add(init);
        grid[xNodeStart][yNodeStart] = init; //add to grid
        
        // list of possible actions to use in while loop
        int[][] ACTION = new int[][]{{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        while (!openList.isEmpty()) {
            AStarNode toExpand = openList.poll(); //find node with minimum value
            for (int i = 0; i < ACTION.length; ++i) {
                final int x = toExpand.getX() + ACTION[i][0];
                final int y = toExpand.getY() + ACTION[i][1];
                double actionCost = (i % 2 == 0) ? 1.0 : RobotMovement.SQRT2;
                actionCost *= GridGeo.NODE_GAP_DIST;

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
                        AStarNode toAdd = new AStarNode(x, y, hn, 0, toExpand, true);
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
    
    /**
     * Does A* search after initialising closed list
     * precondition: start[] vector in cm and should line up with a node.
     */
    public AStarNode aStarSearch(double[] start) {
    	return aStarSearch(start[0], start[1]);
    }
    
    public LinkedList<AStarNode> findForwardPath(AStarNode goal) {
        LinkedList<AStarNode> list = new LinkedList<AStarNode>();
        list.add(goal);
        AStarNode current = goal;
        while (!current.isRoot()) {
            current = current.getParent();
            list.addFirst(current);
        }
        return list;
    }
    
    public LinkedList<AStarNode> findBackwardPath(AStarNode goal) {
        LinkedList<AStarNode> list = new LinkedList<AStarNode>();
        list.add(goal);
        AStarNode current = goal;
        while (!current.isRoot()) {
            current = current.getParent();
            list.addLast(current);
        }
        return list;
    }

    public boolean isInsideBorder(int x, int y) {
        int tmp1 = GridGeo.NODES_PER_EDGE - BORDER_NODE_WIDTH;
        if (x >= BORDER_NODE_WIDTH && y >= BORDER_NODE_WIDTH && x < tmp1 && y < tmp1) {
            return true;
        }
        return false;
    }

    public double getNodeSize() {
        return GridGeo.NODE_GAP_DIST;
    }

/////////////////////////////////////////////////////////////////////////////
// below are the object placing methods, for map/grid
/////////////////////////////////////////////////////////////////////////////
    
    /**
     * Initialises ClosedNodes and closed list
     * @return An ideal position in 'cm' coordinates of goal node.
     */
    public double[] initClosedList(){
    	inputCylinderPosition(GridGeo.RAND_CYCL_31cm_Center); 
        inputCorners();
        double[] goalIdeal = inputTunnelPosition(GridGeo.TUNNEL_BeginMarch_Center, 90);     
        // Maybe good idea:
        //inputWallPosition(20, 0, 122, 100, 1);   // 'invisible' wall to reduce search-space
        
        return goalIdeal;
    }

    /**
     * Updates the map so that it won't navigate though a CYLINDER OBJECT
     */
    public void inputCylinderPosition(double x, double y) {
        addClosedNodeCircle(x / GridGeo.NODE_GAP_DIST, y / GridGeo.NODE_GAP_DIST,
        		(GridGeo.CYLINDER_RADIUS + ROBOT_RADIUS) / GridGeo.NODE_GAP_DIST);
    }
    /**
     * Updates the map so that it won't navigate though a CYLINDER OBJECT
     */
    public void inputCylinderPosition(double[] vect) {
        inputCylinderPosition(vect[0], vect[1]);
    }
    

    /**
     * Adds corners into the closed list
     */
    public void inputCorners() {
        double dist = 29.3 / GridGeo.NODE_GAP_DIST; // in nodes
        double r = ROBOT_RADIUS / GridGeo.NODE_GAP_DIST;
        double w = GridGeo.COURSE_WIDTH / GridGeo.NODE_GAP_DIST;
        addClosedNodeRectangle(dist, 0, 0, dist, r);
        addClosedNodeRectangle(w - dist, w, w, w - dist, r);
    }

    /**
     * Updates the map so that it won't navigate though a TUNNEL OBJECT
     * TUNNEL is hardcoded to be 24cm width by 20cm depth   2cm walls
     */
    public double[] inputTunnelPosition(double x, double y, double degrees) {
        double radians = degrees * (Math.PI / 180.0);
        
        double[] tmp = GridGeo.correctOffset(x, y, GridGeo.TUNNEL_FRONT_RIGHT);
        double[] fRight = rotateVector(tmp, x, y, radians);
        tmp = GridGeo.correctOffset(x, y, GridGeo.TUNNEL_BACK_RIGHT);
        double[] bRight = rotateVector(tmp, x, y, radians);
        tmp = GridGeo.correctOffset(x, y, GridGeo.TUNNEL_BACK_LEFT);
        double[] bLeft = rotateVector(tmp, x, y, radians);
        tmp = GridGeo.correctOffset(x, y, GridGeo.TUNNEL_FRONT_LEFT);
        double[] fLeft = rotateVector(tmp, x, y, radians);

        double r = GridGeo.TUNNEL_WALL_RADIUS + ROBOT_RADIUS;
        //add the walls
        inputWallPosition(fLeft, bLeft, r);
        inputWallPosition(bLeft, bRight, r);
        inputWallPosition(bRight, fRight, r);

        // compute a sensible ideal goal to plan to & return
        final int extraDist = 5;
        final int depthToCenter = 10;
        return rotateVector(x, y - depthToCenter - extraDist, x, y, radians);
    }

    private double[] inputTunnelPosition(double[] center, double degrees) {
    	if (center.length != 2) throw new IllegalArgumentException();
    	return inputTunnelPosition(center[0], center[1], degrees);
    }
    /**
     * Updates the map so that it won't navigate though a WALL OBJECT
     */
    public void inputWallPosition(double x1, double y1, double x2, double y2, double r) {
        double scale = GridGeo.NODE_GAP_DIST; // switch to work on the 'nodes' scale
        x1 = x1 / scale;
        y1 = y1 / scale;
        x2 = x2 / scale;
        y2 = y2 / scale;
        r = r / scale;
        addClosedNodeRectangle(x1, y1, x2, y2, r);
        addClosedNodeCircle(x1, y1, r);
        addClosedNodeCircle(x2, y2, r);
    }
    
    /**
     * Updates the map so that it won't navigate though a WALL OBJECT
     */
    public void inputWallPosition(double[] pntA, double[] pntB, double r) {
    	inputWallPosition(pntA[0], pntA[1], pntB[0], pntB[1], r);
    }

    //(x1,y1), (x2,y2) are mid points of rectangle ends
    private void addClosedNodeRectangle(double x1, double y1, double x2, double y2, double r) {
        if (x1 == x2 || y1 == y2) { // CODE IF MID-LINE ALIGNED TO X OR Y AXIS
            if (x1 == x2 && y1 == y2) {
             //both points identical so don't do anything
            }else if (y1 == y2) {
                int top = (int) Math.floor(y1 + r);
                int bottom = (int) Math.ceil(y1 - r);
                for (int i = top; i >= bottom; --i) {
                    int begin = (int) Math.ceil(Math.min(x2, x1));
                    double end = Math.max(x2, x1);
                    for (int j = begin; j < end; ++j) {
                        addClosedNode(i, j);
                    }
                } 
            } else { // if x1 == x2
                int top = (int) Math.floor(Math.max(y1, y2));
                int bottom = (int) Math.ceil(Math.min(y1, y2));
                for (int i = top; i >= bottom; --i) {
                    int begin = (int) Math.ceil(x1 - r);
                    double end = x1 + r;
                    for (int j = begin; j < end; ++j) {
                        addClosedNode(i, j);
                    }
                }
            }
        } else { // CODE IF MID-LINE NOT-ALIGNED TO X OR Y AXIS
            LinkedList<double[]> pnts = new LinkedList<double[]>();
            double centerX = (x1 + x2) / 2;
            double centerY = (y1 + y2) / 2;
            double len = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            double angle = Math.atan((y2 - y1) / (x2 - x1));
            pnts.add(new double[]{centerX - len / 2, centerY + r});
            pnts.add(new double[]{centerX - len / 2, centerY - r});
            pnts.add(new double[]{centerX + len / 2, centerY + r});
            pnts.add(new double[]{centerX + len / 2, centerY - r});
            for (double[] element : pnts) {
                double[] rotated = rotateVector(element, centerX, centerY, angle);
                element[0] = rotated[0];
                element[1] = rotated[1];
            }

            Collections.sort(pnts, new Comparator<double[]>()  // sort by y values
            {
                @Override
                public int compare(double[] o1, double[] o2) {
                	return Double.compare(o1[1], o2[1]);
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

            addClosedNodeQuadrangle(x, y, h, w, lineEqs);
        }
    }

    // lineEqs must be inputed as first two being upper-bounding and second two being lower-bounding
    private void addClosedNodeQuadrangle(double x, double y, double h, double w, double[][] lineEqs) {
        boolean[] isUpperBounding = new boolean[]{true, true, false, false};
        if (lineEqs.length > 4) throw new IllegalArgumentException("upto four line equations!");
        for (int i = (int) Math.ceil(y); i < y + h; ++i) {
            for (int j = (int) Math.ceil(x); j < x + w; ++j) {
                boolean insideLines = true;
                for (int k = 0; k < lineEqs.length; ++k) {
                    if (isUpperBounding[k]) {
                        if (i > lineEqs[k][0] * j + lineEqs[k][1]) insideLines = false;
                    } else {
                        if (i < lineEqs[k][0] * j + lineEqs[k][1]) insideLines = false;
                    }
                }
                if (insideLines) addClosedNode(i, j);
            }
        }
    }

    private void addClosedNodeCircle(double x, double y, double r) {
        int highest = (int) Math.floor(y + r);
        int lowest = (int) Math.ceil(y - r);

        for (int i = highest; i >= lowest; --i) {
            double h = y - i;
            double change = Math.sqrt(r * r - h * h);
            int xmin = (int) Math.ceil(x - change);
            int xmax = (int) Math.floor(x + change);
            for (int j = xmin; j <= xmax; ++j) {
                addClosedNode(i, j);
            }
        }
    }

    // adds to ith row and jth column of 'grid' matrix
    private void addClosedNode(int i, int j) {
        if (isInsideBorder(i, j)) {
            AStarNode toAdd = new AStarNode(i, j);
            closedList.add(toAdd);
            grid[i][j] = toAdd;
        }
    }

    //rotates 
    private static double[] rotateVector(double[] vector, double centerX, double centerY, double radians) {
        if (vector.length != 2) throw new IllegalArgumentException();
        double[] toRotate = new double[]{centerX - vector[0], centerY - vector[1]};
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double[] toAdd = new double[]{cos * toRotate[0] - sin * toRotate[1],
                sin * toRotate[0] + cos * toRotate[1]};
        double[] result = new double[]{centerX + toAdd[0], centerY + toAdd[1]};
        
        for (int i = 0; i < 2; ++i) {
            result[i] = 1e-12 * Math.rint(1e12 * result[i]);  // discards small multiplication errors
        }
        return result;
    }
    
    private static double[] rotateVector(double x, double y, double centerX, double centerY, double radians){
    	return rotateVector(new double[]{x,y},centerX,centerY,radians);
    }

    private static double[] findLineEq(double[] pntA, double[] pntB) {
        double m = (pntA[1] - pntB[1]) / (pntA[0] - pntB[0]);
        double c = pntA[1] - m * pntA[0];
        return new double[]{m, c};
    }
}
