package tests.aStarTest;

import java.util.List;
import main.term2Challenges.Node;
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

        // some extra tests.
//        model.inputCylinderPosition(61, 61);
//        AStarNode goalNode = model.aStarSearch(GridGeo.BayesianCoordinate(20));
        double[] goalCoords = model.initClosedList1();
        long startTime = System.currentTimeMillis();
        Node goalNode = model.aStarSearch(GridGeo.BayesianCoordinate(20-3) ,goalCoords );
        System.out.println(System.currentTimeMillis() - startTime);
        List<Node> list = model.findForwardPath(goalNode);
//        List<Node> list = model.findBackwardPath(goalNode);
        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(400);
        for (Node element : list) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(60);
        }

    }
}
