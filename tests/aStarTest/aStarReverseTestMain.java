package tests.aStarTest;


import main.term2Challenges.AStarNode;
import main.term2Challenges.AlternativeGrid;
import main.term2Challenges.Grid;

import java.util.LinkedList;

public class aStarReverseTestMain {

    public static void main(String[] args) throws InterruptedException {
        AlternativeGrid model = new AlternativeGrid(62);
        int size = model.getSize();
        GridFrame grid = new GridFrame(model.getSize());
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (!model.isInsideBorder(i, j)) {
                    grid.placeEmpty(i, j);
                }
            }
        }

        //calculate tunnel position
        double[] goal = model.inputTunnelPosition(90, 90, 90);

        //find reverse path
        AStarNode startNode = model.aStarSearch((int) goal[1], (int) goal[0], 20, 20);

        //get list with reverse path
        LinkedList<AStarNode> reverseList = model.getListPathFromGoalNode(startNode);

        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(500);
        for (AStarNode element : reverseList) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(200);
        }
    }
}
