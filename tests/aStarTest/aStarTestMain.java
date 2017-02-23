package tests.aStarTest;

import main.term2Challenges.Grid;

public class aStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        //model.findGoalNodeFromRoot(40, 30);
        
        int size = model.getSize();
        GridFrame grid = new GridFrame(model.getSize());
//        grid.placeRobot(40, 30);
//        grid.placeGoal(5, 20);

        
        //model.addWallToClosedList(20, 20, 60, 20, /*in cm*/ 10);
        //int[] goalPos = model.inputTunnelPosition(90, 90, 2);
//        grid.placeGoal(goalPos[0], goalPos[1]);
        
        
        for(int i = 0; i < size; ++i){
        	 for(int j = 0; j < size; ++j){
        		 if (!model.isInsideBorder(i, j)){
        			 grid.placeGoal(i, j);
        		 }
        	 }
        }
        
        model.addAngledRectangleToClosedList( 20, 20, 40, 25, 10);
        
        
        grid.readGrid(model);
        
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
