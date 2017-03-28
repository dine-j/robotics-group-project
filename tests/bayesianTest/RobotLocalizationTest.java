package tests.bayesianTest;
//import org.junit.Test;

//import static org.junit.Assert.assertEquals;

import main.term2Challenges.LocalizationStrip;

public class RobotLocalizationTest {

    public static RobotMockUp robot;
    public static LocalizationStrip localizationStrip;

    public static void main(String[] args) {
        localizationStrip = new LocalizationStrip();
        try {
            test1();
//            test2();
        } catch (AssertionError error) {
            System.out.println(error);
            localizationStrip.printBayesianResults();
        }
    }

    /*
     * Test for only moving forward
     */
//    @Test
    public static void test1() {
        robot = new RobotMockUp(20, 0.98);
        localizationStrip.reinitializeProbabilities();

        int steps = 0;

        while(localizationStrip.getHighestProbability() < 0.8) {
            localizationStrip.updateProbs(true, robot.getColorIsBlue(), 0.95, 1);
            robot.moveForward();
            ++steps;
        }
        localizationStrip.printBayesianResults();
        System.out.println("Steps: " + steps);
//        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }

    /*
     * Test for only moving backward
     */
//    @Test
    public static void test2() {
        robot = new RobotMockUp(20, 0.95);
        localizationStrip.reinitializeProbabilities();

        int steps = 0;

        for(int i = 0; i < 7; ++i) {
            localizationStrip.updateProbs(true, robot.getColorIsBlue(), 0.95, 1);
            robot.moveForward();
            ++steps;
        }
        localizationStrip.printBayesianResults();
        System.out.println(steps);
//        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }

    public static void test3() {
        robot = new RobotMockUp(10, 0.95);
        localizationStrip.reinitializeProbabilities();

        for(int i = 0; i < 9; ++i) {
            localizationStrip.updateProbs(false, robot.getColorIsBlue(), 0.95, 1);
            robot.moveBackward();
        }

//        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }
}
