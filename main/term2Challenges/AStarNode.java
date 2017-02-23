package main.term2Challenges;

import java.util.Comparator;

/**
 * Represents A* nodes, sorted by f(n) values or a comparator
 *
 */
public class AStarNode implements Comparable<AStarNode>{
	private static enum NodeState{OPEN, CLOSED, GOAL, ROOT};
	private int x;
	private int y;
	private AStarNode parent;

	private Double hn;
	private Double gn; // may be recomputed
	private Double fn;
	
	private NodeState state;  //node may either be open or closed
	
	
	public AStarNode(int x, int y, double hn, double gn, AStarNode parent, boolean open){
		this.x = x;
		this.y = y;
		this.hn = hn;
		this.gn = gn;
		this.fn = gn + hn;
		this.parent = parent;
		this.state = NodeState.OPEN;
	}
	
	/**
	 * Creates a valueless, node mainly for closed list
	 * @param x
	 * @param y
	 */
	public AStarNode (int x, int y){
		this.x = x;
		this.y = y;
		this.hn = null;
		this.gn = null;
		this.fn = null;
		this.parent = null;
		this.state = NodeState.CLOSED;
	}
	
	/**
	 * Can create a valueless goal node
	 * @param x
	 * @param y
	 */
	public AStarNode (int x, int y, boolean isGoal){
		this(x,y);
		if(isGoal) state = NodeState.GOAL;
	}
	
	public void setClosed(){
		this.state = NodeState.CLOSED;
	}
	
	public boolean isOpen(){
		return state == NodeState.OPEN || state == NodeState.GOAL;
	}
	
	public void setGn(double gn){
		this.gn = gn;
		fn = gn + hn; // recompute fn;
	}
	
	public void setParent(AStarNode parent) {
		this.parent = parent;
	}
	
	public boolean isRoot(){
		return parent == null;
	}
	
	public boolean hasValues(){
		return hn == null || gn == null || fn == null;
	}

	@Override
	public boolean equals(Object other){
		try{
			if (other instanceof AStarNode){
				return this.compareTo((AStarNode) other) == 0;
			}
		} catch( Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public int compareTo(AStarNode other){
		try{
			int result = 0;
			if ((this.fn - other.fn) > 0 )  result = 1;
			else if ((this.fn - other.fn) > 0 ) result = -1;
			return result;
			
		} catch( Exception e){
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public AStarNode getParent() {
		return parent;
	}

	public double getHn() {
		return hn;
	}

	public double getGn() {
		return gn;
	}

	public double getFn() {
		return fn;
	}
	
	public static class positionComparator implements Comparator<AStarNode>{
		@Override
		public int compare(AStarNode o1, AStarNode o2) {
			return o1.x * 500 + o1.y - o2.x * 500 - o2.y; //works for grid size of up to 500
		}
	}
}
