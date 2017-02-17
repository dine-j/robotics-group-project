package main.term2Challenges;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

/**
 * To be controlled in a separate thread.
 */
public class Head implements Runnable {

    private final int HEAD_MS_DELAY = 60;
    private final int HEAD_ROTATE_ANGLE = 35;
    private EV3UltrasonicSensor usSensor;
    private EV3MediumRegulatedMotor motor;


    //initialise head
    public Head(EV3UltrasonicSensor ultrasonicSensor, EV3MediumRegulatedMotor visionMotor) {
        usSensor = ultrasonicSensor;
        motor = visionMotor;
    }

    //code to run in separate thread
    //rotates and takes samples while no obstacles detected
    public void run() {
        //initial sample
        float[] ultrasonicSample = new float[1];
        usSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);

        int counter = 0;

        while (ultrasonicSample[0] > 0.20) {
            counter++;
            if(counter == 0) motor.rotateTo(HEAD_ROTATE_ANGLE, true);
            else if(counter == HEAD_MS_DELAY) motor.rotateTo(0, true);
            else if(counter == 2*HEAD_MS_DELAY) motor.rotateTo(-HEAD_ROTATE_ANGLE, true);
            else if (counter > 3*HEAD_MS_DELAY) counter = -1;

            usSensor.getDistanceMode().fetchSample(ultrasonicSample, 0);
            Delay.msDelay(5);
        }
    }

}
