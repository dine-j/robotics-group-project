package main.term2Challenges;

/**
 * Represents A* nodes, sorted by f(n) values.
 *
 */
public class AStarNode implements Comparable{
	private int x;
	private int y;
	private AStarNode parent;

	private Double hn;
	private Double gn; // may be recomputed
	private Double fn;
	
	private boolean open;  //node may either be open or closed
	
	
	public AStarNode(int x, int y, double hn, double gn, AStarNode parent, boolean open){
		this.x = x;
		this.y = y;
		this.hn = hn;
		this.gn = gn;
		this.fn = gn + hn;
		this.parent = parent;
		this.open = open;
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
		open = false;
	}
	
	public void setClosed(){
		open = false;
	}
	
	public boolean isOpen(){
		return open;
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
	
	/*
	 * DONE: x.compareTo(y)  == 0   iff x.equals(y)     is not true yet. Sort this out.
	 */
	
	@Override
	public boolean equals(Object other){
		try{
			if (other instanceof AStarNode){
				AStarNode tmp = (AStarNode) other;
				//return x == tmp.x && y == tmp.y;
				return this.compareTo(tmp) == 0;
			}
			
		} catch( Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int compareTo(Object other){
		try{
			if (other instanceof AStarNode){
				AStarNode tmp = (AStarNode) other;
				int result;
				if ((this.fn - tmp.fn) > 0 )  result = 1;
				else if ((this.fn - tmp.fn) > 0 ) result = -1;
				else result = 0;
				return result;
			}
			
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
	
}
