package main.term2Challenges;

public enum RobotMovement { 
	LEFT45(500),
	LEFT90(1000),
	RIGHT45(500),
	RIGHT90(1000), 
	FORWARD(2000),
	FORWARD_ON_DIAGONAL(2828); // 2000 * sqrt(2)
	
	int timeCost; //in milliseconds
	RobotMovement(int timeCost){
		this.timeCost = timeCost;
	}
	
	
	//some constants
	final static double SQRT2 = Math.sqrt(2);
	
	// some others to use later (as directions...)
	final static int N = 0;
	final static int NE = 1;
	final static int E = 2;
	final static int SE = 3;
	final static int S = 4;
	final static int SW = 5;
	final static int W = 6;
	final static int NW = 7;
	
}