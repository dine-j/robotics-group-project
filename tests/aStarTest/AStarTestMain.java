package tests.aStarTest;

import java.util.LinkedList;

import main.term2Challenges.Node;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;

/**
 * Creates grid gui, to see how A* search will perform
 */
public class AStarTestMain {
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

        double[] goalCoords = model.initialiseClosedList1(true);
        long startTime = System.currentTimeMillis();
        
        int n = 20;
        double[] bayesCoordPos = GridGeo.BayesianCoordinate(n);
        double[] startPosition = GridGeo.actualRobotCenterSW(bayesCoordPos);
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        Node goalNode = model.aStarSearch(firstNodePosition ,goalCoords );
        System.out.println(System.currentTimeMillis() - startTime + " ms to find path");

        // Forward
        LinkedList<Node> list = model.findFowardPath(goalNode);


        // Backward
//        LinkedList<Node> list = model.findBackwardPath(goalNode);



        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(400);
        for (Node element : list) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(60);
        }
    }
}
