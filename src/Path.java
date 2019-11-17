package main;

import java.util.LinkedList;
import java.util.List;

public class Path implements Cloneable {

	private List<Node> path = new LinkedList<Node>();  // List of nodes on a path
	private int value;		// Value of a path in total (cost of distance traveled + heuristics)

	public Path(Node node) {
		path.add(node);
	}

	public void addNode(Node node) {
		path.add(node);
	}

	public int size() {
		return path.size();
	}

	public Node get(int index) {
		return path.get(index);
	}

	public Node getLast() {
		return path.get(path.size() - 1);
	}
	
	public int getValue() {
		return value;
	}

	public List<Node> getPath() {
		return path;
	}

	public void setPath(List<Node> path) {
		this.path = path;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Path clone() {		// For easier expanding of a path we need to clone it
		try {
			Path newPath = (Path) super.clone();
			newPath.path = new LinkedList<Node>(path);
			return newPath;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
