package main.term2Challenges;

/**
 * Some Geometry methods and constants for Grid class
 */
public class GridGeo {
	
    public static final int NODES_PER_EDGE = 62; // 62 means 2cm between each node
    public static final double COURSE_WIDTH = 122; // in cm
    public static final double CORNER_DIAG_WIDTH = 21; // in cm
    public static final double BAYESIAN_ZEROTH_DIAG = CORNER_DIAG_WIDTH + 2; // in cm
    public static final double NODE_GAP_DIST = (double) COURSE_WIDTH / (double) (NODES_PER_EDGE - 1);	
    public static final double BAYESIAN_GAP_DIST = 2; //in cm
    public static final double CYLINDER_RADIUS = 2.5; // in cm
	public static final double TUNNEL_WALL_RADIUS = 0.9; // in cm
	public static final double[] TUNNEL_FRONT_LEFT = new double[]{-11,-10};
	public static final double[] TUNNEL_FRONT_RIGHT = new double[]{11,-10};
	public static final double[] TUNNEL_BACK_LEFT = new double[]{-11,9};
	public static final double[] TUNNEL_BACK_RIGHT = new double[]{11,9};
	
	public static final double[] RAND_CYCL_31cm_Center = new double[]{40, 82};
	public static final double[] GREEN_CYCL_14cm_Center = new double[]{72, 50};
	public static final double[] RED_CYCL_31cm_Center = new double[]{82, 40};
	
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
        double tmpx = x / NODE_GAP_DIST;
        double tmpy = y / NODE_GAP_DIST;
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
        return new double[]{ ((double)tmp[0]) / NODE_GAP_DIST , ((double)tmp[1]) / NODE_GAP_DIST};
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
    	double xOrY = (Math.ceil(currPos[0]/ NODE_GAP_DIST)) * NODE_GAP_DIST;
    	return new double[]{ xOrY, xOrY};
    }
	
}
