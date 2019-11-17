package main;

public class Node {
	
	// Help class used to describe Node in a tree
	// Each Node represents a field on a map
	
	private int x;		// Coordinates of a field; Example { (1,B) => (1,2)}
	private int y;

	public Node(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "\t\t{\n\t\t"+'"'+"row"+'"'+ ":"+x+", \n\t\t"+'"'+"col"+'"'+":"+(char)(y+64)+"\n\t\t}";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
