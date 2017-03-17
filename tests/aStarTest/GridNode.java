package tests.aStarTest;

import javax.swing.*;

import main.term2Challenges.GridGeo;
import main.term2Challenges.RobotMovement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * (Interactive blank-button) , with settable colour, and string info.
 */
public class GridNode extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Color state;
    private String coordString = "no info";

    public GridNode() {
        super();
        //setEnabled(false);
        addActionListener(this);
    }

    public void setState(Color state) {
        this.state = state;
        setBackground(this.state);
        setOpaque(true);
    }
    
    public void setCoordString(String s) {
        coordString = s;
    }
    public void setCoordString(int i, int j) {
    	double x = j * GridGeo.NODE_SIZE;
    	double y = GridGeo.COURSE_WIDTH - i * GridGeo.NODE_SIZE;
    	coordString = String.format(
    			"x: %.2fcm, y: %.2fcm node:%d , %d", x , y,
    			j , GridGeo.NODES_PER_EDGE-1 - i);
    	
    	
    	// trying to add distance from leading diagonal line to make debugging quicker
//    	coordString += "\nDiag info(21cm from diag axis):";
//    	double d1 = (x + y)/RobotMovement.SQRT2;
//    	double d2 = (y - 0.5*x) * RobotMovement.SQRT2;
//    	coordString += String.format("%.2f diagDiff: %.2f",
//    			d1,d2);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(coordString);
	}
}
