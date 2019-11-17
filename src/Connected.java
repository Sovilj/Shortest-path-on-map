package main;

public class Connected {

	// Two connected fields are represented with Connected class
	
	private Node n1;
	private Node n2;
	private int value;

	public Connected(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
	}

	public Connected(Node n1, Node n2, int value) {
		this.n1 = n1;
		this.n2 = n2;
		this.value = value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Connected)) {
			return false;
		}
		Connected c = (Connected) o;
		return (n1.equals(c.n1) && n2.equals(c.n2)) || (n1.equals(c.n2) && n2.equals(c.n1));
	}

	public String toString() {
		return n1 + " " + n2;
	}
}
