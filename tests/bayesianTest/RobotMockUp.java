package tests.bayesianTest;

import java.util.Random;

public class RobotMockUp {

    private int currentLocation;
    private double sensorProba;
    private final boolean[] stripIsBlue;

    /*
     * @param sensorProba   Probability that the sensor is right
     */
    public RobotMockUp(int currentLocation, double sensorProba) {
        this.currentLocation = currentLocation;
        this.sensorProba = sensorProba;
        stripIsBlue = new boolean[] { true, false, true, false, false, true, true, false, true, true,
                false, false, true, true, true, false, true, true, true, false, false,
                true, true, true, false, false, true, true, true, false, false, false, false,
                true, true, true, true, false, false, false};
    }

    public void moveForward() {
        ++currentLocation;
    }

    public void moveBackward() {
        --currentLocation;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    /*
     * If the sensor is right, then it returns the correct color
     */
    public boolean getColorIsBlue() {
        if(Math.random() < sensorProba)
            return stripIsBlue[currentLocation];
        return !stripIsBlue[currentLocation];
    }
}
