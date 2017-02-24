package tests.aStarTest;

import main.term2Challenges.AStarNode;
import main.term2Challenges.Grid;

public class aStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        int size = model.getSize();
        GridFrame grid = new GridFrame(model.getSize());
        for(int i = 0; i < size; ++i){
        	 for(int j = 0; j < size; ++j){
        		 if (!model.isInsideBorder(i, j)){
        			 grid.placeEmpty(i, j);
        		 }
        	 }
        }
        
        //grid.placeRobot(20,20);
        //model.inputWallPosition(20, 0, 122, 100, 1);   // 'invisible' wall to reduce search-space
        
     // 1. Add closed list stuff
//        model.inputCylinderPosition(40, 122-40); // we don't know yet
//        model.inputCorners();
//        double[] goalIdeal = model.inputTunnelPosition(90, 90, 90);
//        int[] goalTmp = model.findClosestNode(goalIdeal[1], goalIdeal[0]); // please check..
        
        // 2. Add initial pos to open list
        //AStarNode init = new AStarNode(xStart, yStart, manhattanHeuristic(xStart, yStart, goal), 0, null, true);
        //openList.add(init);
        //grid[xStart][yStart] = init; //add to grid
        //..........
        //grid.placeGoal(goalTmp[0], goalTmp[1]); 
        
        model.findGoalNodeFromRoot(20,20);
        
        grid.readGrid(model);
        grid.setVisible(true);
      
//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
