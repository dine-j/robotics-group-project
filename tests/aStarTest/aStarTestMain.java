package tests.aStarTest;

import main.term2Challenges.Grid;

public class aStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        GridFrame grid = new GridFrame(model.getSize());
        grid.placeRobot(40, 30);
        grid.placeGoal(5, 20);
        grid.setVisible(true);

//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
