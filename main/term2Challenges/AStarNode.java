package main.term2Challenges;

public class AStarNode {
	private int x;
	private int y;
	private AStarNode parent;

	private Double hn;
	private Double gn; // may be recomputed
	private Double fn;
	
	
	public AStarNode(int x, int y, double hn, double gn, AStarNode parent){
		this.x = x;
		this.y = y;
		this.hn = hn;
		this.gn = gn;
		this.fn = gn + hn;
		this.parent = parent;
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
				AStarNode tmp = (AStarNode) other;
				return x == tmp.x && y == tmp.y;
			}
			
		} catch( Exception e){
			e.printStackTrace();
		}
		
		return false;
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
