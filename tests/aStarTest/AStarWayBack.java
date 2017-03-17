package tests.aStarTest;

import java.util.LinkedList;
import java.util.List;
import main.term2Challenges.Node;
import main.term2Challenges.RobotMovement;
import main.term2Challenges.Grid;
import main.term2Challenges.GridGeo;

/**
 * Creates grid gui, to see how A* search will perform   on the way back
 */
public class AStarWayBack {
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
        
//        boolean isGreen = true;
        boolean isGreen = false;

        model.initClosedList2(isGreen);
        long startTime = System.currentTimeMillis();
        
        Node goalNode = model.aStarSearch(new double[]{110,62} ,GridGeo.CHALLENGE2_BACK_TO_START );
        System.out.println(System.currentTimeMillis() - startTime);
        
        LinkedList<Node> list = model.findForwardPath(goalNode);
//        List<Node> list = model.findBackwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
        
        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(400);
        for (Node element : list) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(60);
        }

    }
}
