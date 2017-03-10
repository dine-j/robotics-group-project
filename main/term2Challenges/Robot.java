package main.term2Challenges;

import java.util.List;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.*;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/*
 * Port A: MotorL (works best this configuration - week 4)
 * Port D: MotorR
 * 
 * Port B: HeadRotatingMotor
 * 
 * Port 1: Gyro
 * Port 2: Ultrasound
 * Port 3: Touch
 * Port 4: Color
 */

public class Robot {

    private EV3LargeRegulatedMotor motorL, motorR;
    private EV3MediumRegulatedMotor visionMotor;

    private EV3ColorSensor colorSensor;
    private EV3UltrasonicSensor ultrasonicSensor;
    private EV3GyroSensor gyroSensor;
    private EV3TouchSensor touchSensor;

    private static final double DISTANCE_PER_REVOLUTION = 17.27; // cm per 360° rotation

    public Robot(EV3LargeRegulatedMotor motorL, EV3LargeRegulatedMotor motorR, EV3MediumRegulatedMotor visionMotor, EV3ColorSensor colorSensor, EV3UltrasonicSensor ultrasonicSensor, EV3GyroSensor gyroSensor, EV3TouchSensor touchSensor) {
        this.motorL = motorL;
        this.motorR = motorR;

        this.visionMotor = visionMotor;
        this.colorSensor = colorSensor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.gyroSensor = gyroSensor;
        this.touchSensor = touchSensor;

        this.motorL.setSpeed(120);
        this.motorR.setSpeed(120);

        this.ultrasonicSensor.getDistanceMode();
        this.visionMotor.rotateTo(0);
    }

    /**
     * Localize position with Bayesian Filter
     * @return A distance of how far along the 'Strip the robot is'
     */
    public int localize() {
        LocalizationStrip localizationStrip = new LocalizationStrip();

        double sensorProbability = 0.95;

        SensorMode colorMode = colorSensor.getRGBMode();
        float[] sample = new float[colorMode.sampleSize()];

        for (int i = 0; i < 10; ++i) {
            colorMode.fetchSample(sample, 0);
            boolean isBlue = false;

            if (sample[2] < 0.1) // if robot senses blue
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

            if (sample[0] == 2) // if robot senses blue
                isBlue = true;

            moveDistance(2);
            localizationStrip.updateProbs(true, isBlue, sensorProbability, 1);
        }
        return localizationStrip.getLocation();
    }

    public void followInstructions(List<RobotMovement> instructions, double straightDistance, double diagonalDistance) {
        for (int i = 0; i < instructions.size(); ++i) {
            switch (instructions.get(i)) {
                case LEFT45:
                    rotate(45);
                    break;
                case LEFT90:
                    rotate(90);
                    break;
                case LEFT135:
                    rotate(135);
                    break;
                case RIGHT45:
                    rotate(-45);
                    break;
                case RIGHT90:
                    rotate(-90);
                    break;
                case RIGHT135:
                    rotate(-135);
                    break;
                case RIGHT180:
                    rotate(-180);
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
     * Move forward until reaches the end of the tunnel
     */
    public void enterTunnel() {
        SampleProvider sampleProvider = touchSensor.getTouchMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        motorL.setSpeed(120);
        motorR.setSpeed(120);

        while(sample[0] == 0) { // button is not pressed
            motorL.forward();
            motorR.forward();
            sampleProvider.fetchSample(sample, 0);
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

    public void exitTunnel() {
        moveDistance(-15);
    }

    /**
     * Rotate robot by given angle, which can be positive (anticlockwise) or negative (clockwise)
     * @param rotationValue Angle of rotation
     */
    private void rotate(int rotationValue) {
        gyroSensor.reset();
        SampleProvider sampleProvider = gyroSensor.getAngleMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);

        float kp = 0.7f;
        float ki = 0f;
        float kd = 0f;
        int tp = 10;
        float integral = 0f;
        float derivative = 0f;


        if (rotationValue > 0) {
            while (sample[0] < rotationValue) {
                float angle = sample[0];
                float error = angle - rotationValue;

                float turn = kp * error + ki * integral + kd * derivative;
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

                float turn = kp * error + ki * integral + kd * derivative;
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
     * Move the robot forward or backward given a certain distance
     * @param distance Distance for movement in cm, can be positive or negative
     */
    private void moveDistance(double distance) {
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
        return colorValue < Color.GREEN; // TODO: to be modified once we get the true colors
    }
}
