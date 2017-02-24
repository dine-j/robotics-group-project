package tests.aStarTest;

import main.term2Challenges.Grid;

/**
 * Used to check if all the Grid map-placing methods functioned correctly
 * 
 *
 */
public class GridTest {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        //model.findGoalNodeFromRoot(40, 30);
        
        int size = model.getSize();
        GridFrame grid = new GridFrame(model.getSize());
        
        for(int i = 0; i < size; ++i){
        	 for(int j = 0; j < size; ++j){
        		 if (!model.isInsideBorder(i, j)){
        			 grid.placeEmpty(i, j);
        		 }
        	 }
        }
        
        double[] tmp = model.inputTunnelPosition(90, 90, 90);
        model.inputCylinderPosition(40, 122-40);
        model.inputWallPosition(20, 0, 122, 100, 1);   // 'invisible' wall to reduce search-space
        model.inputCorners();
        grid.readGrid(model);
        grid.placeGoal(model.findClosestNode(tmp[1], tmp[0]));
        grid.placeRobot(20,20);
        
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
