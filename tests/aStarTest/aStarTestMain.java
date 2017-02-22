package tests.aStarTest;

import main.term2Challenges.Grid;

public class aStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        //model.findGoalNodeFromRoot(40, 30);
        
        GridFrame grid = new GridFrame(model.getSize());
//        grid.placeRobot(40, 30);
//        grid.placeGoal(5, 20);

        
        model.addWallToClosedList(20, 20, 20, 20, /*in cm*/ 10);
        grid.readGrid(model);
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
