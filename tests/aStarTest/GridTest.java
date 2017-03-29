package tests.aStarTest;

import main.term2Challenges.Grid;

/**
 * Used to check if all the Grid map-placing methods functioned correctly
 */
public class GridTest {
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
        
//        double[] tmp = model.inputTunnelPosition(82.5, 110, 90);
//        model.inputCylinderPosition(GridGeo.RAND_CYCL_31cm_Center);
//        model.inputCorners();
//        
//        //further testing
//        model.inputCylinderPosition(GridGeo.RED_CYCL_31cm_Center);
//        model.inputCylinderPosition(GridGeo.GREEN_CYCL_14cm_Center);
//        
//        //test invisible walls on way back (challenge 2)
//        model.inputWallPosition(0, 120, 60, 60, 4);
//        model.inputWallPosition(0,0 , 60, 60, 4);
        model.initialiseClosedList2(false, true);
        
        grid.readGrid(model);
        //grid.placeGoal(GridGeo.closestNodeInNodeCoords(tmp[1], tmp[0]));
//        grid.placeRobot(20,20);
        
        grid.setVisible(true);
      
    }
}
