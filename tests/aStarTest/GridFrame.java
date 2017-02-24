package tests.aStarTest;

import main.term2Challenges.*;

import javax.swing.*;
import java.awt.*;

public class GridFrame extends JFrame {

    private GridNode[][] nodes;

    public GridFrame(int size) {
        super("Grid");
        setSize(600, 600);
        setLayout(new GridLayout(size, size));
        nodes = new GridNode[size][size];
        for(int i = 0; i < size; ++i) {
            for(int j = 0; j < size; ++j) {
                nodes[i][j] = new GridNode();
                add(nodes[i][j]);
            }
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void placeRobot(int x, int y) {
        if(x >= nodes.length || y >= nodes.length)
            throw new IndexOutOfBoundsException("Max = " + (nodes.length - 1));
        nodes[nodes.length - 1 - x][y].setState(Color.blue);
    }
    public void placeEmpty(int x, int y) {
        if(x >= nodes.length || y >= nodes.length)
            throw new IndexOutOfBoundsException("Max = " + (nodes.length - 1));
        nodes[nodes.length - 1 - x][y].setState(Color.gray);
    }
    

    public void placeObstacle(int startX, int endX, int startY, int endY) {
        if(endX >= nodes.length || endY >= nodes.length)
            throw new IndexOutOfBoundsException("Max = " + (nodes.length - 1));
        for(int i = startX; i <= endX; ++i) {
            for(int j = startY; j <= endY; ++j) {
                nodes[i][j].setState(Color.black);
            }
        }
    }

    public void placeGoal(int x, int y) {
        if(x >= nodes.length || y >= nodes.length)
            throw new IndexOutOfBoundsException("Max = " + (nodes.length - 1));
        nodes[nodes.length - 1 - x][y].setState(Color.red);
    }
    
    public void readGrid(Grid grid){
    	AStarNode[][] matrix = grid.getGrid();
    	for(int i = 0; i < nodes.length; ++i){
    		for( int j = 0; j < nodes.length; ++ j) {
    			if(matrix[i][j] == null){
    				//pass
    			
    			}else if (!matrix[i][j].isOpen()){
    				nodes[nodes.length - 1 - i][j].setState(Color.black);// display (0,0 at bottom left)
    			}else if (matrix[i][j].isRoot()){  
    				placeRobot(nodes.length - i,j);  // display (0,0 at bottom left)
    			}
    		}
    	}
    }

	public void placeGoal(int[] pos) {
		placeGoal(pos[0], pos[1]);
		
	}
}
