package tests.aStarTest;

import main.term2Challenges.Grid;

public class GridTest {
	public static void main(String[] args) {
		System.out.println("started");
		Grid model = new Grid();
		model.addWallToClosedList(20, 20, 25, 20, /*in cm*/ 10);
		System.out.println(model);
	}
}
