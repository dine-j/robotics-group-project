package tests.aStarTest;

import main.term2Challenges.Grid;

public class GridTest {
	public static void main(String[] args) {
		System.out.println("started");
		Grid model = new Grid();
		
		double[] vector = new double[] {1 ,0};
		
		Grid.rotateVector(vector, 0, 0.5, 90);
		
		//System.out.println(model);
	}
}
