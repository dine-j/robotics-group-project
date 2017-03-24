package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

/**
 * Localize Robot, follow path to infront of goal, follow path to wall at start location
 * Motors: left= A right = D
 * Sensors: Color= 4,lowerTouch=3, upperTouch=2,  Gyro = 1
 */
public class Challenge1 {

    public static void main(String[] args) {

        Robot r = new Robot();

        Button.waitForAnyPress();

        // Measure drift 
        if(r.isSensorDrifting())
            return;
        
        // Localize with Bayesian 'strip'
        int n = r.localize();  
        System.out.println(n);
        //int n = 20; // stub - the last white square within two lines

        // Get onto the 'Grid network'
        double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
        r.moveDistance(distToMoveOnDiagonal); 
        
        // Goal using A * (doesn't have to go inside)
        Grid model = new Grid();
        double[] goalCoords = model.initClosedList1();
        Node goalNode = model.aStarSearch(firstNodePosition, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
        double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        
        Delay.msDelay(3000); // found goal (hopefully)
        //Sound.beep();
        
        // Going back to starting point
        // System.out.println("Start to go back");
        list = model.findBackwardPath(goalNode);
        actionList = RobotMovement.parsePathToMovements(list);

        // Add extra movement to face wall
        int wallDirection = RobotMovement.SW;
        actionList.add(RobotMovement.dirChange(wallDirection));

        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        r.moveToWall();
        //Sound.beep();
    }
}
