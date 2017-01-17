package utils;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class HelloWorld {
	public static void main(String[] args){
        Delay.msDelay(100);
        LCD.drawString("Hello World", 0,0);
        Delay.msDelay(500);
	}
}
