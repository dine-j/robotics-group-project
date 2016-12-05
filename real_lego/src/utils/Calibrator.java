package utils;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

/**
 * Tests the (red mode) color sensor
 * Prints 0.0 - 1.0 for 10 seconds to screen
 *
 */
public class Calibrator {
	public static void main(String[] args) {
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		SensorMode colorMode = colorSensor.getRedMode();
		float[] sample = new float[colorMode.sampleSize()];

		for(int i = 0; i < 10; i++) {
			colorMode.fetchSample(sample, 0);
			GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
			g.drawString("" + sample[0], 0, 0, GraphicsLCD.VCENTER);
			Delay.msDelay(1000);
			g.clear();
		}
	}
}
