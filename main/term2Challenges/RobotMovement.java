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
}
