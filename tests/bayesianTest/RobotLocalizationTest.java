package tests.bayesianTest;

import main.term2Challenges.LocalizationStrip;
import static org.junit.Assert.*;

public class RobotLocalizationTest {

    public static RobotMockUp robot;
    public static LocalizationStrip localizationStrip;

    public static void main(String[] args) {
        localizationStrip = new LocalizationStrip();

        try {
            test1();
        } catch (AssertionError error) {
            localizationStrip.printBayesianResults();
        }
    }

    public static void test1() {
        robot = new RobotMockUp(14, 0.95);
        localizationStrip.reinitalizeProbabilities();

        for(int i = 0; i < 9; ++i) {
            localizationStrip.updateProbs(true, robot.getColorIsBlue());
            robot.moveForward();
        }

        assertEquals(robot.getCurrentLocation(), localizationStrip.getLocation());
    }
}
