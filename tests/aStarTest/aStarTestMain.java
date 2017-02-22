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

        
        //model.addWallToClosedList(20, 20, 25, 20, /*in cm*/ 10);
        double[] goalPos = model.inputTunnelPosition(50, 90, 0);
        int goalx = (int) (goalPos[0] / 2.0);
        int goaly = (int) (goalPos[1] / 2.0);
        
        
        
        for(int i = 0; i < size; ++i){
        	 for(int j = 0; j < size; ++j){
        		 if (!model.isInsideBorder(i, j)){
        			 grid.placeGoal(i, j);
        		 }
        	 }
        }
        
        grid.placeGoal(goalx, goaly);
        grid.readGrid(model);
        
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
