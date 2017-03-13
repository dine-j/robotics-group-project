package main.term2Challenges;

import java.util.Comparator;

/**
 * Represents A* nodes, sorted by f(n) values or a comparator
 *
 */
public class Node implements Comparable<Node>{
	private static enum NodeState{OPEN, CLOSED, GOAL};
	private int x;
	private int y;
	private Node parent;

	private double hn;
	private double gn; // may be recomputed
	private double fn;
	
	private NodeState state;  //node may either be open or closed
	
	public Node(int x, int y, double hn, double gn, Node parent, boolean open){
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
	public Node (int x, int y){
		this.x = x;
		this.y = y;
		this.hn = 0;
		this.gn = 0;
		this.fn = 0;
		this.parent = null;
		this.state = NodeState.CLOSED;
	}
	
	/**
	 * Can create a valueless goal node
	 * @param x
	 * @param y
	 */
	public Node (int x, int y, boolean isGoal){
		this(x,y);
		if(isGoal) state = NodeState.GOAL;
	}
	
	public void setClosed(){
		this.state = NodeState.CLOSED;
	}
	
	public boolean isOpen(){
		return state == NodeState.OPEN || state == NodeState.GOAL;
	}
	
	public boolean isGoal(){
		return state == NodeState.GOAL;
	}
	
	public void setGn(double gn){
		this.gn = gn;
		fn = gn + hn; // recompute fn;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public boolean isRoot(){
		return parent == null; 
	}
	
	/*
	 * Removed Equals method 12/3/17 as it introduced nasty bug, in different datastructures
	 */
	public int compareTo(Node other){
		return Double.compare(this.fn,other.fn);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Node getParent() {
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
	
	@Override
	public String toString(){
		return "["+x+","+y+"] "+(isOpen() ? "OPEN":"CLOSED")+ " h,g,f = " + String.format("%.1f,%.1f,%.1f", hn,gn,fn);
	}
	
	public static class PositionComparator implements Comparator<Node>{
		@Override
		public int compare(Node o1, Node o2) {
			return o1.x * 500 + o1.y - o2.x * 500 - o2.y; //works for grid size of up to 500
		}
	}
}
