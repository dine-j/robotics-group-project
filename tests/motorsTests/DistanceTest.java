package tests.motorsTests;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

/**
 * Early term2 work of testin moving robot
 */
public class DistanceTest {
    public static void main(String[] args) {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);

        float[] touchSample = new float[1];
        while(true){
            SensorMode sm = touchSensor.getTouchMode();
            sm.fetchSample(touchSample, 0);
            if (touchSample[0] == 1.0) break;
        }

        final int DEGREES = 2100;
        final int MORE = 1000;
        
        motorL.rotate(DEGREES,true);
        motorR.rotate(DEGREES);
        
        Delay.msDelay(20000);
        
        motorL.rotate(DEGREES,true);
        motorR.rotate(DEGREES);
        Delay.msDelay(20000);
        
        motorL.rotate(DEGREES,true);
        motorR.rotate(DEGREES);
        
    }

}


