package tests.bayesianTest;

import main.term2Challenges.LocalizationStrip;
import org.junit.Test;

import static org.junit.Assert.*;

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
    @Test
    public static void test1() {
        robot = new RobotMockUp(14, 0.9);
        localizationStrip.reinitializeProbabilities();

        for(int i = 0; i < 18; ++i) {
            localizationStrip.updateProbs(true, robot.getColorIsBlue(), 0.9, 1);
            robot.moveForward();
        }

        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }

    /*
     * Test for only moving backward
     */
    @Test
    public static void test2() {
        robot = new RobotMockUp(25, 0.9);
        localizationStrip.reinitializeProbabilities();

        for(int i = 0; i < 9; ++i) {
            localizationStrip.updateProbs(false, robot.getColorIsBlue(), 0.9, 1);
            robot.moveBackward();
        }

        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }
}
