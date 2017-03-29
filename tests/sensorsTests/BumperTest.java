package tests.sensorsTests;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import main.term2Challenges.Robot;

public class BumperTest {
    public static void main(String[] args) {
        Robot r = new Robot();

        Button.waitForAnyPress();

        r.tryToEnterTunnel();

        // Going inside the tunnel
        r.moveToWall();

        // Sensing color
        boolean isGreen = r.getNextObstacle();

        if (isGreen)
            Sound.beep();
        else
            Sound.twoBeeps();

        // Moving back
        r.exitTunnel();
    }
}
