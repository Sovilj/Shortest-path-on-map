package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Search {

	protected List<Path> paths = new LinkedList<Path>();	// List of all paths that need to be examined
	private Path current;		// Current path that is being processed
	
	private int shortest = -1;		// Value of the shortest path
	protected List<Path> short_paths = new LinkedList<Path>();	// All the shortest pats that have the same value(distance)

	private Graph graph;    // Input map

	public static class Compare implements Comparator<Path> {		// To sort the list of all possible paths we need Comparator
		@Override
		public int compare(Path p1, Path p2) {
			return Integer.compare(p1.getValue(), p2.getValue());
		}
	}

	public Search(Graph graph) {
		if (graph.getStart() != null) {
			this.paths.add(new Path(graph.getStart()));
			this.graph = graph;
		}
	}

	public void search() {
		// Search started
		while (hasNext()) {  // While there are paths in paths list expand those paths and calculate their value
			current = next();
			if (shortest != -1 && current.getValue() > shortest) {
				break;
			}
			if (isEnd()) {
				shortest = current.getValue();
				short_paths.add(current);
				continue;
			}
			List<Path> newPaths = expandCurrent();
			removeLoops(newPaths);
			addPaths(newPaths);
		}
		// End
	}

	protected void addPaths(List<Path> newPaths) {
		
		// Adding new paths to existing and calculating their value

		paths.addAll(newPaths);
		for (int i = 0; i < paths.size(); i++) {
			Path next = paths.get(i);

			int value = 0;
			for (int t = 0; t < next.size() - 1; t++) {
				value += graph.getCost(next.get(t), next.get(t + 1));
			}
			int lastX =  next.getLast().getX();
			int lastY =  next.getLast().getY();
			
			double heuristic = (Math.sqrt(Math.pow((lastX - graph.getEnd().getX()), 2)
					 + Math.pow((lastY - graph.getEnd().getY()), 2))); ;
			
			next.setValue(value + (int)(heuristic*10));
		}
		Collections.sort(paths, new Compare());			// Sorting all paths by value

	}

	private Path next() {
		return paths.remove(0);
	}

	private void removeLoops(List<Path> expanded) {
		// Removing loops from paths so that there is no turning back on the previous fields
		
		for (int i = expanded.size() - 1; i >= 0; i--) {
			Path newPath = expanded.get(i);
			for (int j = 0; j < newPath.size() - 1; j++)
				if (newPath.get(j).equals(newPath.getLast())) {
					expanded.remove(i);
					break;
				}
		}
	}

	private List<Path> expandCurrent() {
		
		//  Adding all neighbours of the last Node to the end of path
		
		List<Node> nodes = new LinkedList<Node>();
		nodes.addAll(graph.getNeighbors(current.getLast()));

		List<Path> newPaths = new LinkedList<Path>();
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) == null)
				throw new NullPointerException("Error - no neighbours on node");
			Path newPath = current.clone();
			newPath.addNode(nodes.get(i));
			newPaths.add(newPath);
		}

		return newPaths;
	}

	private boolean hasNext() {
		return !paths.isEmpty();
	}

	private boolean isEnd() {
		return graph.getEnd().getX() == current.getLast().getX() &&		// Checking if last Node on current path is last field
				graph.getEnd().getY() == current.getLast().getY() ;
	}

	public Path getCurrent() {
		return current;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public void write(String outputFile, long searchTime, long readTime) {

		try (PrintWriter writer = new PrintWriter(new File(outputFile))) {

			// Writing to file using print writer; 

			writer.append("{\n");
			writer.append('"'+"read_time_in_ms"+'"'+": "+ readTime + ",\n");
			writer.append('"'+"search_time_in_ms"+'"'+": "+ searchTime + ",\n");
			writer.append('"'+"total_time_in_ms"+'"'+": "+ (readTime+searchTime) + ",\n");
			writer.append('"'+"paths"+'"'+": [\n");
			writer.append("{\n");
			
			for (int k = 0; k < short_paths.size() ; k++) {		// Printing all short paths
				
				writer.append('"'+"points"+'"'+": [\n");
				
				for (int i = 0; i < short_paths.get(k).size(); i++) {
					writer.append(short_paths.get(k).get(i).toString());
					if (i != short_paths.get(k).size()-1) {
						writer.append(",\n");
					}
				}

				writer.append("\n\t]\n");
				writer.append("}");	
			}
			writer.append("\n]\n}\n");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) {
		
		if (args.length == 2) {
			long startRead = System.nanoTime();		// Measuring time 
			Graph g = new Graph();				
			g.create_graph(args[0]);				// Reading file and generating map
			
			long startSearch = System.nanoTime();
			Search s = new Search(g);
			s.search();								// Searching for solution
			
			long endTime   = System.nanoTime();
			long searchTime = TimeUnit.MILLISECONDS.convert(endTime - startSearch, TimeUnit.NANOSECONDS) ;
			long readTime = TimeUnit.MILLISECONDS.convert(endTime - startRead, TimeUnit.NANOSECONDS) ;
			
			s.write(args[1],searchTime,readTime);	// Writing in file
			
			System.out.println("Done!");
			System.out.println("Outputfile src: "+args[1]);
			
		}else {
			System.out.println("No input args");
		}
		
		
	}

}
