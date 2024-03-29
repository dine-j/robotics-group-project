package tests.pathTest;

import main.term2Challenges.RobotMovement;

import java.util.LinkedList;
import java.util.List;

/**
 * A test class to test 
 *
 */
public class MockedPathGenerator {
	
	
	/**
	 * 
	 * @return returns a straightline path, moving 60cm
	 */
	public static List<RobotMovement> testPath1(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		for(int i = 0; i < 30; ++i){
			listEg.add(RobotMovement.FORWARD);
		}
		return listEg;
		
	}
	
	
	/**
	 * 
	 * @return returns a straightline path, moving 56.5cm
	 */
	public static List<RobotMovement> testPath2(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		for(int i = 0; i < 20; ++i){
			listEg.add(RobotMovement.FORWARD_ON_DIAGONAL);
		}
		return listEg;
		
	}
	
	/**
	 * 
	 * @return Square path to infront of goal  (within 3mm)
	 */
	public static List<RobotMovement> testPath3(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		listEg.add(RobotMovement.LEFT45);
		for(int i = 0; i < 33; ++i){  //22 was wrong, 31 was wrong
			listEg.add(RobotMovement.FORWARD);
		}
		listEg.add(RobotMovement.RIGHT90);
		for(int i = 0; i < 10; ++i){  //13 was wrong
			listEg.add(RobotMovement.FORWARD);
		}
		return listEg;
		
	}
	
	/**
	 * 
	 * @return Square path to infront of goal  (within 3mm)
	 */
	public static List<RobotMovement> testPath4(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		listEg.add(RobotMovement.LEFT45);
		for(int i = 0; i < 34; ++i){  //22 was wrong, 31 was wrong, 33 was wrong
			listEg.add(RobotMovement.FORWARD);
		}
		listEg.add(RobotMovement.RIGHT90);
		for(int i = 0; i < 20; ++i){  
			listEg.add(RobotMovement.FORWARD);
		}
		return listEg;
		
	}
	
	
	/**
	 * 
	 * @return returns a straightline path, moving 4 meters
	 */
	public static List<RobotMovement> testPath5(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		for(int i = 0; i < 200; ++i){
			listEg.add(RobotMovement.FORWARD);
		}
		return listEg;
		
	}
	
	/**
	 * 
	 * @return returns a straightline path, moving 1.414 meters
	 */
	public static List<RobotMovement> testPath6(){
		
		
		List<RobotMovement> listEg = new LinkedList<RobotMovement>();
		
		for(int i = 0; i < 50; ++i){
			listEg.add(RobotMovement.FORWARD_ON_DIAGONAL);
		}
		return listEg;
		
	}
}
