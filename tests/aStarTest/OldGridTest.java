package tests.aStarTest;

import main.term2Challenges.OldGrid;

/**
 * Used to check if all the Grid map-placing methods functioned correctly
 * 
 *
 */
public class OldGridTest {
    public static void main(String[] args) throws InterruptedException {
        OldGrid model = new OldGrid();
        //model.findGoalNodeFromRoot(40, 30);
        
        int size = model.getSize();
        OldGridFrame grid = new OldGridFrame(model.getSize());
        
        for(int i = 0; i < size; ++i){
        	 for(int j = 0; j < size; ++j){
        		 if (!model.isInsideBorder(i, j)){
        			 grid.placeEmpty(i, j);
        		 }
        	 }
        }
        
        double[] tmp = model.inputTunnelPosition(82.5, 110, 90);
        model.inputCylinderPosition(40, 122-40);
        model.inputWallPosition(20, 0, 122, 100, 1);   // 'invisible' wall to reduce search-space
        model.inputCorners();
        grid.readGrid(model);
        //grid.placeGoal(model.findClosestNode(tmp[1], tmp[0]));
        grid.placeGoal(55,34); //what above commented-out (private)method calculates
        grid.placeRobot(20,20);
        
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
