package real_lego;

import lejos.hardware.Sound;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Lego {
	public static void main(String[] args){
		//Sound.beepSequence();
        Delay.msDelay(100);
        //Button.LEDPattern(1);
        //Delay.msDelay(2000);
        LCD.drawString("hello", 0,0);
        Delay.msDelay(500);
        
	}
}
