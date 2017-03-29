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
    private EV3TouchSensor leftTouchSensor;
    private EV3GyroSensor gyroSensor;
    private EV3TouchSensor rightTouchSensor;

    private static final double DISTANCE_PER_REVOLUTION = 17.27; // cm per 360° rotation

    public Robot() {  	
    	this.motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        this.motorR = new EV3LargeRegulatedMotor(MotorPort.D);

        this.gyroSensor = new EV3GyroSensor(SensorPort.S1);
        this.leftTouchSensor = new EV3TouchSensor(SensorPort.S2);
        this.rightTouchSensor = new EV3TouchSensor(SensorPort.S3);
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

        while(localizationStrip.getHighestProbability() < 0.85) {
            colorMode.fetchSample(sample, 0);
            boolean isBlue = false;

            if (sample[2] < threshold) // if robot senses blue
                isBlue = true;

            Delay.msDelay(500);
            moveDistance(1.9, 120);
            localizationStrip.updateProbabilities(true, isBlue, sensorProbability, 1);
        }

        System.out.println(localizationStrip.getHighestProbability());
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
        gyroSensor.reset();
        SampleProvider sampleProvider = gyroSensor.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];

        Sound.beepSequenceUp();
        long startingTime = System.currentTimeMillis();
        int duration = 3000;

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(!isPressed(leftTouchSensor) && !isPressed(rightTouchSensor) && System.currentTimeMillis() - startingTime <= duration) {
            motorL.forward();
            motorR.forward();
            if(isPressed(leftTouchSensor) || isPressed(rightTouchSensor)) {
                Sound.beep();
                if(isPressed(leftTouchSensor)) {
                    sampleProvider.fetchSample(sample, 0);
                    adjustPosition(false, (int) sample[0]);
                }
                if(isPressed(rightTouchSensor)) {
                    sampleProvider.fetchSample(sample, 0);
                    adjustPosition(true, (int) sample[0]);
                }
            }
        }

        Sound.beepSequence();
    }

    /**
     * Move forward until reaches the end of the tunnel
     */
    public void moveToWall() {
        SampleProvider leftSampleProvider = leftTouchSensor.getTouchMode();
        float[] leftSample = new float[leftSampleProvider.sampleSize()];
        leftSampleProvider.fetchSample(leftSample, 0);

        SampleProvider rightSampleProvider = rightTouchSensor.getTouchMode();
        float[] rightSample = new float[rightSampleProvider.sampleSize()];
        rightSampleProvider.fetchSample(rightSample, 0);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(!isPressed(leftTouchSensor) && !isPressed(rightTouchSensor)) { // button is not pressed
            motorL.forward();
            motorR.forward();
            leftSampleProvider.fetchSample(leftSample, 0);
            rightSampleProvider.fetchSample(rightSample, 0);
        }

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

        return isGreen(sample[0]);
    }

    /**
     * Exit tunnel by moving back 21.5cm
     */
    public void exitTunnel() {
        moveDistance(-21.5);
    }

    /**
     * 
     * @param rightHandSide
     * @param angle
     */
    public void adjustPosition(boolean rightHandSide, int angle) {
        if(rightHandSide) {
            moveDistance(-2, 120);
            rotateSlowly(-angle);
            moveDistance(-14);
            rotate(27);
            moveDistance(8.2);
            rotate(-27);
        } else {
            moveDistance(-2, 120);
            rotateSlowly(-angle);
            moveDistance(-14);
            rotate(-27);
            moveDistance(8.2);
            rotate(27);
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

        motorL.stop();
        motorR.stop();
    }

    /**
     * Move the robot forward or backward given a certain distance in cm
     * @param distance Distance for movement in cm, can be positive or negative
     */
    public void moveDistance(double distance) {
        motorL.setSpeed(200);
        motorR.setSpeed(200);
        double angle = distance * 360 / DISTANCE_PER_REVOLUTION;
        motorL.rotate((int) angle, true);
        motorR.rotate((int) angle);
    }

    /**
     * Move the robot forward or backward given a certain distance in cm
     * @param distance Distance for movement in cm, can be positive or negative
     * @param speed Speed given to the motors
     */
    public void moveDistance(double distance, int speed) {
        motorL.setSpeed(speed);
        motorR.setSpeed(speed);
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
     * @param touchSensor EV3 Touch Sensor
     * @return True if touch sensor is pressed
     */
    private boolean isPressed(EV3TouchSensor touchSensor) {
        SampleProvider sp = touchSensor.getTouchMode();
        float[] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        return sample[0] > 0;
    }
}
