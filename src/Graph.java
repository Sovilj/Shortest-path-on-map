package main;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Graph {

	private Node start;		// Start field on map
	private Node end;		// End field on map
	
	private static final int DEF_COST  = 10;		// Default value between two fields
	
	private int matrix_size;		// Size of the map
	
	private HashMap<Node,List<Node>> nodes =  new HashMap<Node,List<Node>>(); // Hashmap of Every node and his neighbours
	private HashMap<String,Node> position = new HashMap<String,Node>();			// Map of position on map and Node class
	private LinkedList<Connected> connected = new LinkedList<Connected>();	// List of pairs of connected nodes
	
	public void create_graph(String inputFile) {
		// Method that reads input file and prepares data to be processed

		try {
			if (!inputFile.substring(inputFile.length()-3).equals("xml")) {
				System.out.println("Error - wrong input file type");
				return;
			}
			File file = new File(inputFile);			// Opening file and getting document ready
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);

			NodeList nList = document.getElementsByTagName("cell");
			
			org.w3c.dom.Node lastN = nList.item(nList.getLength()-1);
			Element lastE = (Element) lastN;
			String lastS = lastE.getAttribute("row");
			matrix_size = Integer.parseInt(lastS);

			for (int temp = 0; temp < nList.getLength(); temp++) {		// Every "cell" field is translated to Node help class

				org.w3c.dom.Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;

				String row = eElement.getAttribute("row");
				String col = eElement.getAttribute("col");
				
				Node n = new Node(Integer.parseInt(row),Character.getNumericValue(col.charAt(0))-9);
				addNode(n);
			}
			nList = document.getElementsByTagName("start-point");		// Getting starting field
			org.w3c.dom.Node sNode = nList.item(0);
			Element e = (Element) sNode;
			String row = e.getAttribute("row");
			String col = e.getAttribute("col");
			start = new Node(Integer.parseInt(row),Character.getNumericValue(col.charAt(0))-9);
			addNode(start);
			
			nList = document.getElementsByTagName("end-point");			// Getting end field
			org.w3c.dom.Node eNode = nList.item(0);
			e = (Element) eNode;
			row = e.getAttribute("row");
			col = e.getAttribute("col");
			end = new Node(Integer.parseInt(row),Character.getNumericValue(col.charAt(0))-9);
			addNode(end);
			
			connectAll();		// When all fields are read from a file they are connected

		} catch (Exception e) {
			System.out.println("Error - readFile");
			e.printStackTrace();
		}
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}
	
	public void addNode(Node node) {
		if (!nodes.containsKey(node)) {
			nodes.put(node,new LinkedList<Node>());			// Adding node to a hashmap with empty neighbour list
			position.put((node.getX()+""+node.getY()),node);	// Adding node to a hashmap with its position on map
		}
	}
		
	public int getMatrix_size() {
		return matrix_size;
	}

	public void connect (Node node1, Node node2, int cost) {
		// Connecting two nodes means adding each other to each neighbour list 
		
		addNode(node1);
		addNode(node2);
		
		List<Node> node1_list = nodes.get(node1);
		if (!node1_list.contains(node2))
			node1_list.add(node2);
		
		List<Node> node2_list = nodes.get(node2);
		if (!node2_list.contains(node1))
			node2_list.add(node1);
		
		
		int index = connected.indexOf(new Connected(node1,node2));
		if (index == -1)
			connected.add(new Connected(node1,node2,cost));
		else
			connected.get(index).setValue(cost);
	}
	
	public int getCost(Node node1, Node node2) {
		return connected.get(connected.indexOf(new Connected(node1,node2))).getValue();
	}
	
	public List<Node> getNeighbors(Node node){
		return nodes.get(node);
	}
	 
	public void setCost(Node node1,Node node2, int cost) {
		connect(node1,node2,cost);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public void connectAll() {
		
		// Each field on a map is connected to its neighbours if they exists
		
		Node n1,n2;
		for(int i = 1; i <= getMatrix_size() ; i++) {
			for(int j = 1; j <= getMatrix_size() ; j++) {
				if(position.containsKey(i+""+j)) {
					n1 = position.get(i+""+j);
					
					if (position.containsKey(i+""+(j+1))) {
						n2 = position.get(i+""+(j+1));
						connect(n1,n2,DEF_COST);
						
					}
					if (position.containsKey(i+""+(j-1))) {
						n2 = position.get(i+""+(j-1));
						connect(n1,n2,DEF_COST);
						
					}
					if (position.containsKey((i+1)+""+j)) {
						n2 = position.get((i+1)+""+j);
						connect(n1,n2,DEF_COST);
						
					}
					if (position.containsKey((i-1)+""+j)) {
						n2 = position.get((i-1)+""+j);
						connect(n1,n2,DEF_COST);
						
					}
				}
				
			}
		}

	}
	
	
	
	
	
	
	
	
	
	
	
}
