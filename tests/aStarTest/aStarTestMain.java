package tests.aStarTest;

import java.util.List;
import main.term2Challenges.AStarNode;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;

/**
 * Creates grid gui, to see how A* search will perform
 */
public class aStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        Grid model = new Grid();
        int size = model.getSize();
        GridFrame grid = new GridFrame(model.getSize());
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (!model.isInsideBorder(i, j)) {
                    grid.placeEmpty(i, j);
                }
            }
        }
        
        model.inputWallPosition(4, 88, 32, 88, 3);
        model.inputWallPosition(40, 52, 40, 72, 3);


        //AStarNode goalNode = model.aStarSearch(GridGeo.BayesianCoordinate(6));
        //AStarNode goalNode = model.aStarSearch(74,22);
        AStarNode goalNode = model.aStarSearch(60,12);
        
        List<AStarNode> list = model.findForwardPath(goalNode);
        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(400);
        for (AStarNode element : list) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(60);
        }

    }
}
