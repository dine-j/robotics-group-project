package main.term2Challenges;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Challenge2 {
	
    public static void main(String[] args) {
        EV3LargeRegulatedMotor motorL = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorR = new EV3LargeRegulatedMotor(MotorPort.D);
        EV3MediumRegulatedMotor visionMotor = new EV3MediumRegulatedMotor(MotorPort.B);

        EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);

        Robot r = new Robot(motorL, motorR, visionMotor, colorSensor, ultrasonicSensor, gyroSensor, touchSensor);

//        Position greenObstacle = new Position(0, 0);
//        Position redObstacle = new Position(0, 0);
        //Node coloredObstacle;

        Button.waitForAnyPress();

        // Localize with Bayesian 'strip'
        // int n = r.localize();
//		System.out.println(n);

        // Make a sound
        if(r.isSensorDrifting())
            return;

        // Get onto the 'Grid network'
		int n = 20; // stub - the last white square within two lines
		double[] startPosition = GridGeo.actualRobotCenterSW(GridGeo.BayesianCoordinate(n));
        double[] firstNodePosition = GridGeo.nextNodeOnLeadingDiagonal(startPosition);
        double distToMoveOnDiagonal = (firstNodePosition[0] - startPosition[0]) * RobotMovement.SQRT2;
		r.moveDistance(distToMoveOnDiagonal); 
		
		// Goal using A * (doesn't have to go inside)
		double[] goalCoords = planToGoal(firstNodePosition,r);
        // Going inside the tunnel
		r.rotate(-90);
        r.enterTunnel();
        
        // Sensing color
        boolean green = r.getNextObstacle();
        // Moving back
        r.exitTunnel();
        r.turnToGoAway();

        // Going to assigned obstacle then back starting point


        planBackToStart(goalCoords, r, green);
//        if(green)
//            coloredObstacle = new Node(greenObstacle.x, greenObstacle.y);
//        else
//            coloredObstacle = new Node(redObstacle.x, redObstacle.y);



    }
    
	private static double[] planToGoal(double[] start, Robot r){
        Grid model = new Grid();
		double[] goalCoords = model.initClosedList1();
		Node goalNode = model.aStarSearch(start, goalCoords);
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList = RobotMovement.parsePathToMovements(list);
		double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
        return goalCoords;
	}
	
	private static void planBackToStart(double[] start, Robot r, boolean isGreen){
		
		Grid model = new Grid();
		model.initClosedList2(isGreen);
		//Node goalNode = model.aStarSearch(start, GridGeo.CHALLENGE2_BACK_TO_START);
		
		//TODO: unhardcode this
		Node goalNode = model.aStarSearch(new double[]{110,62} ,GridGeo.CHALLENGE2_BACK_TO_START );
        LinkedList<Node> list = model.findForwardPath(goalNode);
        List<RobotMovement> actionList =RobotMovement.parsePathToMovements(list);
		double nodeDiagonal = RobotMovement.SQRT2 * model.getNodeSize();
        r.followInstructions(actionList, model.getNodeSize(), nodeDiagonal);
	}
}
