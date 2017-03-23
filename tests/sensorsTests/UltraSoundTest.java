package tests.sensorsTests;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Tests Ultrasonic sensor readings(unnecessary - tool on brick)
 */
public class UltraSoundTest {

    public static void main(String[] args) {
        EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
        
        
        ultraSonicSensor.enable();
        
        SampleProvider sampleProvider = ultraSonicSensor.getDistanceMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0);
        
        for(int i = 0; i < 100; i++) {
            sampleProvider.fetchSample(sample, 0); 

            GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
            g.drawString("" + sample[0], 0, 0, GraphicsLCD.VCENTER);
            Delay.msDelay(1000);
            g.clear();
        }
        
        
        ultraSonicSensor.disable();
        ultraSonicSensor.close();
    }

}
