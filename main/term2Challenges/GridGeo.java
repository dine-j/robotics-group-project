package main.term2Challenges;

/**
 * Some Geometry methods and constants for the following: 
 * 1) Grid class having correct size and location of objects
 * 2) Helping the robot move on and off the 'Grid network' 
 * 3) Helping the robot find it's coordinates on the Bayesian strip
 */
public class GridGeo {
	
    public static final int NODES_PER_EDGE = 62; // 62 means 2cm between each node
//	public static final int NODES_PER_EDGE = 15; //smaller grid for debugging
    public static final double COURSE_WIDTH = 122; // in cm
    public static final double CORNER_DIAG_WIDTH = 21; // in cm
    public static final double BAYESIAN_ZEROTH_DIAG = CORNER_DIAG_WIDTH + 2; // in cm
    public static final double BAYESIAN_DETECTOR_TO_ROBOT_CENTER_DIST = 4.5; // in cm
    
    public static final double NODE_SIZE = (double) COURSE_WIDTH / (double) (NODES_PER_EDGE - 1);	
    public static final double BAYESIAN_GAP_DIST = 2; //in cm
    public static final double CYLINDER_RADIUS = 2.5 + 2; // in cm
	public static final double TUNNEL_WALL_RADIUS = 0.9; // in cm
	public static final double[] TUNNEL_FRONT_LEFT = new double[]{-11,-14}; //new double[]{-11,-10};
	public static final double[] TUNNEL_FRONT_RIGHT = new double[]{11,-14};//new double[]{11,-10};
	public static final double[] TUNNEL_BACK_LEFT = new double[]{-11,9};
	public static final double[] TUNNEL_BACK_RIGHT = new double[]{11,9};
	
	/*
	 *Can add as many cylinder positions as want to test out 
	 */
	public static final double[] RAND_CYCL_31cm_Center = new double[]{40, 82};
	public static final double[] RAND_CYCL_14cm_Center = new double[]{50, 72};
	public static final double[] GREEN_CYCL_14cm_Center = new double[]{72, 50};
	public static final double[] RED_CYCL_31cm_Center = new double[]{82, 40};
	public static final double[] TUNNEL_BeginMarch_Center = new double[]{82.5, 110};
	
	public static final double[] CHALLENGE2_BACK_TO_START = new double[]{30, 16};
	
	/**
	 * @return The result of adding offsetVector[] to {x, y}
	 */
	public static double[] correctOffset(double x, double y, double[] offsetVector){
		return new double[]{x + offsetVector[0], y + offsetVector[1]};
	}
	
	/**
	 * @return The closest node in 'NodeScale' coordinates
	 */
    public static int[] closestNodeInNodeCoords(double x, double y) {
        double tmpx = x / NODE_SIZE;
        double tmpy = y / NODE_SIZE;
        tmpx = Math.round(tmpx);
        tmpy = Math.round(tmpy);
        //debugging println statement
        //System.out.println("closestNodeResults: x = " + tmpx + " , y = " + tmpy);
        return new int[]{(int) tmpx, (int) tmpy};
    }
    
    /**
	 * @return The closest node in 'cm' coordinates
	 */
    public static double[] closestNode(double x, double y) {
        int[] tmp = closestNodeInNodeCoords(x, y);
        return new double[]{ ((double)tmp[0]) / NODE_SIZE , ((double)tmp[1]) / NODE_SIZE};
    }
    
    /**
   	 * @return The nth Bayesian-cell in 'cm' coordinates
   	 */
    public static double[] BayesianCoordinate(int n) {
    	double distanceOnDiagonal = BAYESIAN_ZEROTH_DIAG + BAYESIAN_GAP_DIST * n;
    	double xOrY = distanceOnDiagonal / RobotMovement.SQRT2;
    	return new double[]{ xOrY, xOrY};
    }
    /**
     * @return The next node on leading diagonal relative to currPos[] in 'cm' coordinates
     */
    public static double[] nextNodeOnLeadingDiagonal( double[] currPos){
    	double xOrY = (Math.ceil(currPos[0]/ NODE_SIZE)) * NODE_SIZE;
    	return new double[]{ xOrY, xOrY};
    }
    
    /**
     * @param colourReaderCenter The center of the colourReader in 'cm' coordinates
     * @return The center of the robot in 'cm' coordinates, if center is SW behind colour sensor
     */
    public static double[] actualRobotCenterSW(double[] colourReaderCenter){
    	double x = colourReaderCenter[0];
    	double y = colourReaderCenter[1];
    	x -= BAYESIAN_DETECTOR_TO_ROBOT_CENTER_DIST / RobotMovement.SQRT2;
    	y -= BAYESIAN_DETECTOR_TO_ROBOT_CENTER_DIST / RobotMovement.SQRT2;
    	return new double[]{x,y};
    }
	
}
