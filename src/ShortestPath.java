import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ShortestPath {
       
    public static class Node {
	int id; // identify each node by its id
	double distance; // store the min distance

	//public Node(int id, double distance) {
	   // this.id = id;
	   // this.distance = distance;
	//}

    }
    
    /*
     * 
     */
    public static class Edge {
	int from;
	int to;
	double weight;
	
	public Edge(int from, int to, double weight) {
	    this.from = from;
	    this.to = to;
	    this.weight = weight;
	}	
    }
    
    /*
     * Create a digraph with weighted edges
     */
    public static class Graph{
	int nodes;
	LinkedList<Edge>[] adjacencyList;
	
	@SuppressWarnings("unchecked")
	public Graph(int nodes) {
	    this.nodes = nodes;
	    adjacencyList = new LinkedList[nodes];
	    //initialize adjacency lists for all the nodes
	    for (int i = 0; i < nodes; i++) {
		adjacencyList[i] = new LinkedList<>();
	    }
	}
	
	public void addEdge(int from, int to, double weight) {
	    Edge edge = new Edge(from, to, weight);
	    adjacencyList[from].addFirst(edge);
	}
	
	public void dijkstra(int source) {
	    int INFINITY = Integer.MAX_VALUE;
	    // track which nodes have already been visited
	    boolean[] visited = new boolean[nodes];
	    
	    // create heap nodes for all the vertices
	    Node[] heapNodes = new Node[nodes];
	    for (int i=0; i<nodes; i++) {
		heapNodes[i] = new Node();
		heapNodes[i].id = i;
		heapNodes[i].distance = INFINITY;
		
	    }
	    
	    // initialize the source node's distance to 0
	    heapNodes[source].distance = 0;
	    
	    // insert the nodes to the MinHeap
	    MinHeap minHeap = new MinHeap(nodes);
	    for (int i=0; i<nodes; i++) {
		minHeap.insert(heapNodes[i]);
	    }
	    
	    //printDijkstra(heapNodes, source);

	}
	    public void printDijkstra(Node[] heapNodes, int source) {
		for (int i = 0; i<nodes; i++) {
		    System.out.println("Source: "+ source + " to vertex "+ i +
			    " distance: " + heapNodes[i].distance);
		}
	    }

	
	public void printGraph() {
	    for (int i =0; i<nodes; i++) {
		LinkedList<Edge> list = adjacencyList[i];
		for (int j = 0; j < list.size(); j++) {
		    System.out.println("Vertex " + i + " is connected to "+ 
		list.get(j).to + " with weight " + list.get(j).weight);
		}
	    }
	}
    }
    
    public static class MinHeap{
	int size;
	int currentSize;
	Node[] node;
	int[] indexes; // decrease pi value
	
	public MinHeap(int size) {
	    this.size = size;
	    currentSize = 0;
	    node = new Node[size + 1];
	    node[0] = new Node();
	    node[0].distance = Integer.MIN_VALUE;
	    node[0].id = -1;
	    indexes = new int[size];   
	    
	}
	
	/*
	 * Swap two nodes of min heap
	 */
	public void swapNode(int n1, int n2) {
	    Node temp = node[n1];
	    node[n1] = node[n2];
	    node[n2] = temp;
	}
	
	public void insert(Node n) {
	    currentSize++;
	    int index = currentSize;
	    node[index] = n;
	    indexes[n.id] = index;
	    heapify(index);
	}
	
	public void heapify(int idx) {
	    int parentIdx = idx/2;
	    int currentIdx = idx;
	    while (currentIdx > 0 && node[parentIdx].distance > node[currentIdx].distance) {
		Node parentNode = node[parentIdx];
		Node currentNode = node[currentIdx];
		
		indexes[parentNode.id] = currentIdx;
		indexes[currentNode.id] = parentIdx;

		swapNode(parentIdx, currentIdx);
		currentIdx = parentIdx;
		parentIdx = parentIdx/2;
	    }
	}
	

    }
    
    
    
    public static void main(String args[]) {
	// Read user input, sc1 reads int, sc2 reads string
	Scanner sc1 = new Scanner(System.in);
	Scanner sc2 = new Scanner(System.in);
	String[] s;
	System.out.println("Enter the number of nodes:");
	int nodes = sc1.nextInt();
	System.out.println("Enter the number of edges:");
	int numEdges = sc1.nextInt();
	Graph graph = new Graph(nodes);
	System.out.println("Enter the edge from the vertex a to the vertex b and the weight separated space: ");
	for (int i = 0; i < numEdges; i++) {
		s = sc2.nextLine().split(" ");
		int from = Integer.parseInt(s[0]);
		int to = Integer.parseInt(s[1]);
		double w = Double.parseDouble(s[2]);
		graph.addEdge(from, to, w);		// populate edges for the original graph
	}

	sc1.close();
	sc2.close();
	int source = 0;
	graph.dijkstra(source);
	//graph.printGraph();
    }
    
}
