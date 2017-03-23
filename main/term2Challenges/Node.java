package main.term2Challenges;

/**
 * Represents A* nodes, sorted by f(n) values or a comparator
 *
 */
public class Node implements Comparable<Node>{
    private boolean open;
    private int x;
    private int y;
    private Node parent;

    private double hn;
    private double gn; 
    private double fn; // may be recomputed when gn changes
    
    
    public Node(int x, int y, double hn, double gn, Node parent, boolean open){
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
    public Node (int x, int y){
        this.x = x;
        this.y = y;
        this.hn = 0;
        this.gn = 0;
        this.fn = 0;
        this.parent = null;
        this.open = false;
    }
    
    public void setClosed(){
        this.open = false;
    }
    
    public boolean isOpen(){
        return open;
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
    
}
