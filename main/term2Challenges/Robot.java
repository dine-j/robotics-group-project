package main.term2Challenges;

import java.util.List;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.*;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/*
 * Port A: MotorL (works best this configuration - week 4)
 * Port D: MotorR
 * 
 * Port B: HeadRotatingMotor
 * 
 * Port 1: Gyro
 * Port 2: Upper Touch - wall of tunnel
 * Port 3: Bottom Touch - back of tunnel bumper
 * Port 4: Color
 */
public class Robot {

    private EV3LargeRegulatedMotor motorL, motorR;

    private EV3ColorSensor colorSensor;
    private EV3TouchSensor upperTouchSensor;
    private EV3GyroSensor gyroSensor;
    private EV3TouchSensor bottomTouchSensor;

    private static final double DISTANCE_PER_REVOLUTION = 17.27; // cm per 360° rotation

    public Robot() {  	
    	this.motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        this.motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        this.gyroSensor = new EV3GyroSensor(SensorPort.S1);
        this.upperTouchSensor = new EV3TouchSensor(SensorPort.S2);
        this.bottomTouchSensor = new EV3TouchSensor(SensorPort.S3);
        this.colorSensor = new EV3ColorSensor(SensorPort.S4);
         
        this.motorL.setSpeed(120);
        this.motorR.setSpeed(120);

        RobotMovement.direction = RobotMovement.NE;
    }

    /**
     * Test if the sensor is drifting and beep twice if this is the case
     * @return  True if the sensor is drifting
     */
    public boolean isSensorDrifting() {
        gyroSensor.reset();
        SampleProvider sampleProvider = gyroSensor.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        float start = sample[0];
        for(int i = 0; i < 100; ++i) {
            sampleProvider.fetchSample(sample, 0);
            Delay.msDelay(2);
        }
        if(Math.abs(start - sample[0]) > 2) {
            Sound.twoBeeps();
            return true;
        }
        return false;
    }

    /**
     * Localize position with Bayesian Filter
     * @return A distance of how far along the 'Strip the robot is'
     */
    public int localize() {
        LocalizationStrip localizationStrip = new LocalizationStrip();

        double sensorProbability = 0.95;
        double threshold = 0.1;

        SensorMode colorMode = colorSensor.getRGBMode();
        float[] sample = new float[colorMode.sampleSize()];

        while(localizationStrip.getHighestProbability() < 0.8) {
            colorMode.fetchSample(sample, 0);
            boolean isBlue = false;

            if (sample[2] < threshold) // if robot senses blue
                isBlue = true;

            // Debugging
//            if(isBlue)
//                System.out.println("B");
//            else
//                System.out.println("W");

            Delay.msDelay(2000);
            moveDistance(2);
            localizationStrip.updateProbs(true, isBlue, sensorProbability, 1);
        }

        if (localizationStrip.getHighestProbability() < 0.5) { // if by any chance the probabilities are too low, take one more step to correct it
            colorMode.fetchSample(sample, 0);
            boolean isBlue = false;

            if (sample[2] < threshold) // if robot senses blue
                isBlue = true;

            moveDistance(2);
            localizationStrip.updateProbs(true, isBlue, sensorProbability, 1);
        }
        return localizationStrip.getLocation();
    }

    /**
     * Follow series of instructions
     * @param instructions List of RobotMovements
     * @param straightDistance Width of grid node in cm
     * @param diagonalDistance Diagonal of grid node in cm
     */
    public void followInstructions(List<RobotMovement> instructions, double straightDistance, double diagonalDistance) {
        for (int i = 0; i < instructions.size(); ++i) {
            switch (instructions.get(i)) {
                case LEFT45:
                    rotateSlowly(45);
                    break;
                case LEFT90:
                    rotateSlowly(90);
                    break;
                case LEFT135:
                    rotateSlowly(135);
                    break;
                case RIGHT45:
                    rotateSlowly(-45);
                    break;
                case RIGHT90:
                    rotateSlowly(-90);
                    break;
                case RIGHT135:
                    rotateSlowly(-135);
                    break;
                case RIGHT180:
                    rotateSlowly(-180);
                    break;
                case FORWARD:
                    int forwardCount = 1;
                    for (int j = i + 1; j < instructions.size(); ++j) {
                        if (instructions.get(j) == RobotMovement.FORWARD) {
                            ++forwardCount;
                            ++i;
                        } else {
                            break;
                        }
                    }
                    moveDistance(straightDistance * forwardCount);
                    break;
                case FORWARD_ON_DIAGONAL:
                    int diagonalCount = 1;
                    for (int j = i + 1; j < instructions.size(); ++j) {
                        if (instructions.get(j) == RobotMovement.FORWARD_ON_DIAGONAL) {
                            ++diagonalCount;
                            ++i;
                        } else {
                            break;
                        }
                    }
                    moveDistance(diagonalDistance * diagonalCount);
                    break;
            }
        }
    }

    /**
     * Try to enter tunnel and if not able call adjustPosition()
     */
    public void tryToEnterTunnel() {
        Sound.beepSequenceUp();
        long startingTime = System.currentTimeMillis();
        int duration = 3000;

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(!isPressed(upperTouchSensor) && System.currentTimeMillis() - startingTime <= duration) {
            motorL.forward();
            motorR.forward();
            if(isPressed(upperTouchSensor)) {
                Sound.beep();
                adjustPosition();
            }
        }

        Sound.beepSequence();
    }

    /**
     * Move forward until reaches the end of the tunnel
     */
    public void moveToWall() {
        SampleProvider sampleProvider = bottomTouchSensor.getTouchMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(sample[0] == 0) { // button is not pressed
            motorL.forward();
            motorR.forward();
            sampleProvider.fetchSample(sample, 0);
        }

        // Debugging
//        System.out.println("Button pressed");

        motorL.stop();
        motorR.stop();
    }

    /**
     * Sense color and return true if the color is green, false otherwise
     */
    public boolean getNextObstacle() {
        SensorMode colorMode = colorSensor.getRGBMode();
        float[] sample = new float[colorMode.sampleSize()];

        colorMode.fetchSample(sample, 0);

        // Debugging
//        System.out.println(sample[0]);

        return isGreen(sample[0]);
    }

    /**
     * Exit tunnel by moving back 21.5cm
     */
    public void exitTunnel() {
        moveDistance(-21.5);
    }

    /**
     * Turn the robot right by 90° and update its direction on the grid
     */
    public void turnToGoAway() {
        rotate(-90);
        RobotMovement.direction = RobotMovement.S;
    }

    /**
     * Adjust robot position to be able to enter tunnel
     */
    public void adjustPosition() {
        moveDistance(-14);
        rotate(-27);
        moveDistance(8.2);
        rotate(27);

        long startingTime = System.currentTimeMillis();
        int duration = 3000;
        boolean rightHandSide = false;

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(!isPressed(bottomTouchSensor) && System.currentTimeMillis() - startingTime <= duration) {
            motorL.forward();
            motorR.forward();
            if(isPressed(bottomTouchSensor)) {
                rightHandSide = true;
                Sound.beep();
            }
        }

        motorL.setSpeed(0);
        motorR.setSpeed(0);

        if(rightHandSide) {
            moveDistance(-11);
            rotate(45);
            moveDistance(13);
            rotate(-45);
        }
    }

    /**
     * Rotate robot by given angle, which can be positive (anticlockwise) or negative (clockwise)
     * Use PID with high correction for 90% of the turn and a slow correction for the remaining 10%
     * @param rotationValue Angle of rotation
     */
    private void rotate(int rotationValue) {
        gyroSensor.reset();
        SampleProvider sampleProvider = gyroSensor.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 2f;
        int tp = 30;
        int threshold = 10;

        if (rotationValue > 0) {
            while (sample[0] < rotationValue) {
                float angle = sample[0];
                float error = angle - rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorR.forward();
                motorL.backward();

                sampleProvider.fetchSample(sample, 0);

                if(rotationValue - angle <= threshold) {
                    kp = 0.7f;
                    tp = 10;
                }
            }
        } else {
            while (Math.abs(sample[0]) < -rotationValue) {
                float angle = Math.abs(sample[0]);
                float error = angle + rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorL.forward();
                motorR.backward();

                sampleProvider.fetchSample(sample, 0);

                if (-rotationValue - angle <= threshold) {
                    kp = 0.7f;
                    tp = 10;
                }
            }
        }

        // Debugging
//        System.out.println(sample[0]);

        motorL.stop();
        motorR.stop();
    }

    /**
     * Rotate robot by given angle, which can be positive (anticlockwise) or negative (clockwise)
     * Use PID with high correction for 90% of the turn and a slow correction for the remaining 10%
     * @param rotationValue Angle of rotation
     */
    private void rotateSlowly(int rotationValue) {
        gyroSensor.reset();
        SampleProvider sampleProvider = gyroSensor.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 0.7f;
        int tp = 10;

        if (rotationValue > 0) {
            while (sample[0] < rotationValue) {
                float angle = sample[0];
                float error = angle - rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorR.forward();
                motorL.backward();

                sampleProvider.fetchSample(sample, 0);
            }
        } else {
            while (Math.abs(sample[0]) < -rotationValue) {
                float angle = Math.abs(sample[0]);
                float error = angle + rotationValue;

                float turn = kp * error;
                float power = tp - turn;

                motorL.setSpeed(power);
                motorR.setSpeed(power);

                motorL.forward();
                motorR.backward();

                sampleProvider.fetchSample(sample, 0);
            }
        }

        // Debugging
//        System.out.println(sample[0]);

        motorL.stop();
        motorR.stop();
    }

    /**
     * Move the robot forward or backward given a certain distance
     * @param distance Distance for movement in cm, can be positive or negative
     */
    public void moveDistance(double distance) {
        motorL.setSpeed(120);
        motorR.setSpeed(120);
        double angle = distance * 360 / DISTANCE_PER_REVOLUTION;
        motorL.rotate((int) angle, true);
        motorR.rotate((int) angle);
    }

    /**
     * Check if a given value corresponds to green
     * @param colorValue    Value given by sensor
     * @return  True if the color is green, false otherwise
     */
    private boolean isGreen(float colorValue) {
        return colorValue < 0.1;
    }

    /**
     * Check if touch sensor is pressed
     * @param touchSensor
     * @return True if touch sensor is pressed
     */
    private boolean isPressed(EV3TouchSensor touchSensor) {
        SampleProvider sp = touchSensor.getTouchMode();
        float[] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        return sample[0] > 0;
    }
}
