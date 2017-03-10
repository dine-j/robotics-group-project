package main.term2Challenges;

/**
 * Lists individual movements for robot to perform
 * Also lists and possible directions robot is facing.
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
}
