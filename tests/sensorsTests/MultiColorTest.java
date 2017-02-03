package tests.sensorsTests;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class MultiColorTest {
	
	// TODO: make sure it can read  RED/GREEN colours for task 3....
	
	// Note: The dark blue tape used for Bayesian localisation, is read as 'BLACK' not 'BLUE'
	
	public static void main(String[] args){
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4); //Colour is on port 4

		SensorMode colorMode = colorSensor.getColorIDMode();
		// The sensor can identify 8 unique colours (NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN)
		float[] sample = new float[colorMode.sampleSize()];
		
		final String[] COLOR_ID = 
				{"NONE", "BLACK", "BLUE", "GREEN", "YELLOW", "RED", "WHITE", "BROWN"};
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		
		g.drawString("STARTING", 50, 0, GraphicsLCD.HCENTER);
		Delay.msDelay(1000);
		g.clear();
		
		for (int i = 0 ; i < 500 ; i++){
			colorMode.fetchSample(sample, 0);
			g.drawString((COLOR_ID[ (int)sample[0]]), 50, 0, GraphicsLCD.HCENTER);
			Delay.msDelay(200);
			g.clear();
		}
	}
	
	
}
