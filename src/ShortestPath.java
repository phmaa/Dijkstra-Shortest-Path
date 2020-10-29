import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ShortestPath {
       
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
    
    public static class Node {
	int id; // identify each node by its id
	double distance; // store the min distance
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
	    
	    // A distance array of heapNodes initialized to infinity
	    Node[] heapNodes = new Node[nodes];
	    for (int i=0; i<nodes; i++) {
		heapNodes[i] = new Node();
		heapNodes[i].id = i;
		heapNodes[i].distance = INFINITY;		
	    }
	    
	    // initialize the source node's distance to 0
	    heapNodes[source].distance = 0.;
	    
	    // Build a min heap priority queue
	    MinHeapPQ mpq = new MinHeapPQ(nodes);
	    for (int i=0; i<nodes; i++) {
		//mpq.insert(heapNodes[i]);
		mpq.buildMinHeap(heapNodes[i]);
	    }
	    
	    while (mpq.heapSize > 0) {
		Node extractedNode = mpq.extractMin(); // extract min
		
		// extract the node
		int nodeId = extractedNode.id;
		visited[nodeId] = true;
		
		LinkedList<Edge> list = adjacencyList[nodeId];
		for (int i = 0; i<list.size(); i++) {
		    Edge edge = list.get(i);
		    int to = edge.to;
		    if (visited[to] == false) {
			double newKey = heapNodes[nodeId].distance + edge.weight;
			double currentKey = heapNodes[to].distance;
			if (currentKey>newKey) {
			    decreaseKey(mpq, to, newKey);
			    heapNodes[to].distance = newKey;
			}
			
		    }
		}
	    }
	    
	    printDijkstra(heapNodes, source);

	}
	
	public void decreaseKey(MinHeapPQ minPQ, int index, double newKey) {
	    // index of the node whose distance needs decrease
	    int vertex = minPQ.minHeap[index];
	    Node node = minPQ.node[index];
	    node.distance = newKey;
	    minPQ.heapify(vertex);
	}
	/*
	 * Print the shortest s-t path from s to t
	 */
	public void printDijkstra(Node[] heapNodes, int source) {
	    for (int i = 0; i<nodes; i++) {
		System.out.println("Source: "+ source + " to vertex "+ i +
			" distance: " + heapNodes[i].distance);
	    }
	}

	// Output the edge list
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
    
    public static class MinHeapPQ{
	private int maxSize;
	private int heapSize;	
	Node[] node;
	int[] minHeap; // indexed priority queue
	
	public MinHeapPQ(int maxSize) {
	    this.maxSize = maxSize;
	    this.heapSize = 0;    // ***should be the size of the nodes
	    node = new Node[maxSize + 1];
	    node[0] = new Node();
	    node[0].distance = Integer.MIN_VALUE;
	    node[0].id = -1;
	    minHeap = new int[maxSize];   
	    
	}
	
	/*
	 * Swap two nodes of min heap
	 */
	public void swapNode(int n1, int n2) {
	    Node temp = node[n1];
	    node[n1] = node[n2];
	    node[n2] = temp;
	}
	
	public int heapSize() {
	    return heapSize;
	}
	
	
	public void insert(Node n) {
	    heapSize++;		// increase heap size by one
	    int index = heapSize;
	    node[index] = n;
	    minHeap[n.id] = index;
	    heapify(index);
	}
	
	public void buildMinHeap(Node n) {
	    heapSize++;		// increase heap size by one
	    int index = heapSize;
	    node[index] = n;
	    minHeap[n.id] = index;
	    minHeapify(index);
	    	    
	}
	
	// Get the left child of a node
	private int getLeftChild(int idx) {
	    return 2*idx;
	}
	
	// Get the right child of a node
	private int getRightChild(int idx) {
	    return 2*idx + 1;
	}
	
	// Get the parent of a node
	private int getParent(int idx) {
	    return idx/2;
	}
	
	public void minHeapify(int idx) {
	    int leftChildIdx = getLeftChild(idx);
	    int rightChildIdx = getRightChild(idx);
	    int smallest = idx;
	    
	    // find the smallest 
	    if (leftChildIdx < heapSize() && node[smallest].distance > node[leftChildIdx].distance) {
		smallest = leftChildIdx;
	    }
	    if (rightChildIdx < heapSize() && node[smallest].distance > node[rightChildIdx].distance) {
		smallest = rightChildIdx;
	    }
		//System.out.println("Smallest: " + node[smallest].id);
	    // smallest is not the node
	    if (smallest != idx) {
		Node smallestNode = node[smallest];
		Node temp = node[idx];
		
		// swap
		minHeap[smallestNode.id] = idx;

		minHeap[temp.id] = smallest;
		swapNode(idx, smallest);
		minHeapify(smallest);
	    }

	}
	
	
	public void heapify(int idx) {
	    int parentIdx = idx/2;
	    int currentIdx = idx;
	    while (currentIdx > 0 && node[parentIdx].distance > node[currentIdx].distance) {
		Node parentNode = node[parentIdx];
		Node currentNode = node[currentIdx];
		
		minHeap[parentNode.id] = currentIdx;
		minHeap[currentNode.id] = parentIdx;

		swapNode(parentIdx, currentIdx);
		currentIdx = parentIdx;
		parentIdx = parentIdx/2;
		System.out.println("Parent node: " + minHeap[parentNode.id]
			+ ", Current node: "+ minHeap[currentNode.id]);
	    }
	    System.out.println("Parent node " + node[parentIdx].id + "'s distance:" + node[parentIdx].distance +
		    ", Current node " + node[currentIdx].id +"'s distance: " + node[currentIdx].distance);

	}
	
	public Node extractMin() {
	    Node min = node[1]; // store the minimum value
	    Node lastNode = node[heapSize];   //to make root equal to the last element
	    minHeap[lastNode.id] = 1; // move min to the last position
	    node[1] = lastNode; // move the last node to top
	    node[heapSize] = null;
	    minHeapify(1);
	    heapSize--;
	    return min;
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
