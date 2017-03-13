package tests.aStarTest;

import java.util.LinkedList;

import main.term2Challenges.AStarNode;
import main.term2Challenges.AlternativeGrid;

public class OldaStarTestMain {
    public static void main(String[] args) throws InterruptedException {
        AlternativeGrid model = new AlternativeGrid(62);
        int size = model.getSize();
        OldGridFrame grid = new OldGridFrame(model.getSize());
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (!model.isInsideBorder(i, j)) {
                    grid.placeEmpty(i, j);
                }
            }
        }

        //calculate tunnel position
        double[] goal = model.inputTunnelPosition(90, 90, 90);

        AStarNode goalNode = model.aStarSearch(20, 20, (int) goal[1], (int) goal[0]);
        LinkedList<AStarNode> list = model.getListPathFromGoalNode(goalNode);
        //List<RobotMovement> actionList = model.calculatePath(list);

        grid.readGrid(model);
        grid.setVisible(true);

        Thread.sleep(500);
        for (AStarNode element : list) {
            grid.placeRobot(element.getX(), element.getY());
            Thread.sleep(200);
        }

//        for(int i = 0; i < 10; ++i) {
//            grid.placeRobot(40 - i, 30);
//            Thread.sleep(500);
//        }
    }
}
