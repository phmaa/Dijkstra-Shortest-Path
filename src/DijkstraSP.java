/**
 * Find the shortest path for 0-indexed graph
 */

import java.util.LinkedList;
import java.util.Scanner;

public class DijkstraSP {
    public static class Edge{
	int source;
	int destination;
	double weight;
	
	public Edge(int source, int destination, double weight) {
	    this.source = source;
	    this.destination = destination;
	    this.weight = weight;
	}
    }
    
    public static class Node{
	int id;
	double distance;
    }
    
    public static class Graph{
	LinkedList<Edge>[] adjacencyList;
	int vertices;
	
	public Graph(int vertices) {
	    this.vertices = vertices;
	    adjacencyList = new LinkedList[vertices];
	    
	    for (int i=0; i<vertices; i++) {
		adjacencyList[i] = new LinkedList<>();
	    }
	}
	
	public void addEdge(int source, int destination, double weight) {
	    Edge edge = new Edge(source, destination, weight);
	    adjacencyList[source].addFirst(edge);    
	}
	
	// Output the edge list
	public void printGraph() {
	    for (int i =0; i<vertices; i++) {
		LinkedList<Edge> list = adjacencyList[i];
		for (int j = 0; j < list.size(); j++) {
		    System.out.println("Vertex " + i + " is connected to "+ 
		list.get(j).destination + " with weight " + list.get(j).weight);
		}
	    }
	}
	
	public void dijkstra(int source) {
	    int INFINITY = Integer.MAX_VALUE;
	    boolean[] visited = new boolean[vertices];
	    //List<Integer> path = new ArrayList<>(); 

	    
	    int[] parents = new int[vertices]; // ___________________1
	    parents[source] = -1;
	    // initialize the distance of each node to the max value
	    Node[] nodes = new Node[vertices];
	    for (int i=0; i<vertices; i++) {
		nodes[i] = new Node();
		nodes[i].id = i;
		nodes[i].distance = INFINITY;
	    }
	    
	    nodes[source].distance = 0; // set source node distance to self to 0
	    //path.add(source); // _______________4
	    
	    // insert nodes to min heap priority queue
	    MinHeap minHeap = new MinHeap(vertices);
	    for (int i=0; i<vertices; i++) {
		minHeap.insert(nodes[i]);
	    }
	    
	    // find the best d(v) for each edge leaving u
	    while (!minHeap.isEmpty()) {
		Node vertex = minHeap.ExtractMin();
		int vertexId = vertex.id;
		visited[vertexId] = true;
		
		
		LinkedList<Edge> edgeList = adjacencyList[vertexId];
		for (int i=0; i < edgeList.size();i++) {
		    Edge edge = edgeList.get(i);
		    int destination = edge.destination; // only check the weight of each outgoing edge
		    
		    // check the distance to the node that has not been visited
		    if (visited[destination] == false) {
			double newKey = nodes[vertexId].distance + edge.weight;
			double currentKey = nodes[destination].distance;
			if (newKey < currentKey) {
			    parents[destination] = edge.source; // ____________________2
			    decreaseKey(minHeap, newKey, destination);  // ______________
			    nodes[destination].distance = newKey;  // __________________
			    //path.add(destination);  // _______________3

			}

		    }
		}
	    }
	    
	    printDijkstra(nodes, source, parents);
	}
	
	public void decreaseKey(MinHeap minHeap, double newKey, int vertex) {
	    // get the index of the node whose key needs decrease
	    int index = minHeap.mhPQ[vertex];
	 
	    Node node = minHeap.heapNodes[index];
	    node.distance = newKey;
	    minHeap.heapifyUp(index);  // restore the heap order
	}
	
	public void printDijkstra(Node[] nodes, int source, int[] parents) {
	    for (int i = 0; i<vertices; i++) {
		System.out.println("\nVertex: "+ i +
			"\nShortest distance: " + nodes[i].distance);
		System.out.print("Shorest Path is ");
		printPath(i, parents);
		System.out.println(" ");
	    }
	}
	
	private static void printPath(int currentIndex, int[] parents) {
	    if (currentIndex == -1) {return;}
	    printPath(parents[currentIndex], parents);
	    System.out.print(currentIndex + " ");
	}
	
    }
    
    public static class MinHeap{
	int maxSize;
	int size;
	int[] mhPQ;  
	Node[] heapNodes;
	
	public MinHeap(int maxSize) {
	    this.maxSize = maxSize;
	    size = 0;
	    mhPQ = new int[maxSize]; // priority queue 
	    heapNodes = new Node[maxSize + 1]; // start the array from index 1
	    heapNodes[0] = new Node();
	    heapNodes[0].id = -1;
	    heapNodes[0].distance = Integer.MIN_VALUE;	    
	}
	
	// helper functions that find return parent & child positions and return their indices
	int getLeftChild(int parentIndex) {return parentIndex*2 ;}
	int getRightChild(int parentIndex) {return parentIndex*2 + 1 ;}
	int getParent(int childIndex) { return (childIndex)/2 ; }
	
	boolean hasLeftChild(int parentIndex) { return getLeftChild(parentIndex) < size;}
	boolean hasRightChild(int parentIndex) { return getRightChild(parentIndex) < size;}
	boolean hasParent(int childIndex) { return getParent(childIndex) > 0;}
	
	int leftChildIndex(int parentIndex) {return mhPQ[getLeftChild(parentIndex)] ; }
	int rightChildIndex(int parentIndex) { return mhPQ[getRightChild(parentIndex)] ; }
	int parentIndex(int childIndex) { return mhPQ[getParent(childIndex)] ; }
	
	// return the current size of min heap
	public boolean isEmpty() {	    	
		return size == 0;
	    }
	
	// swap the positions of two nodes
	public void swapNode(int a, int b) {
	    Node temp = new Node();
	    temp = heapNodes[a];
	    heapNodes[a] = heapNodes[b];
	    heapNodes[b] = temp;
	}
	
	public void insert(Node n) {
	    size++;
	    int currentIndex = size;
	    heapNodes[currentIndex]= n;
	    mhPQ[n.id] = currentIndex;
	    heapifyUp(currentIndex);
	}
	
	public void heapifyUp(int currentIndex) {
	    while (hasParent(currentIndex) && 
		    heapNodes[getParent(currentIndex)].distance > heapNodes[currentIndex].distance) {
		Node currentNode = heapNodes[currentIndex];
		Node parentNode = heapNodes[getParent(currentIndex)];
		
		mhPQ[currentNode.id] = getParent(currentIndex);
		mhPQ[parentNode.id] = currentIndex;
		swapNode(getParent(currentIndex), currentIndex);
		currentIndex = getParent(currentIndex);
	    }
	}
	
	/**
	 * 
	 * @return root node
	 */
	public Node ExtractMin(){
	    Node min = heapNodes[1];
	    Node lastNode = heapNodes[size]; // fetch the last node
	    mhPQ[lastNode.id]=1; // move the last node to top
	    heapNodes[1] = lastNode;
	    heapNodes[size] = null;
	    heapifyDown(1);
	    size--;
	    
	    return min;
	}
	
	public void heapifyDown(int index) {
	    int smallest = index; // get the index of the root node
	    while (hasLeftChild(smallest) && heapNodes[smallest].distance > heapNodes[getLeftChild(smallest)].distance) {
		smallest = getLeftChild(smallest);
		if (hasRightChild(smallest) && 
			heapNodes[getRightChild(smallest)].distance < heapNodes[getLeftChild(smallest)].distance) {
		    smallest = getRightChild(smallest);
		}
		
		if (smallest != index) {
		    Node smallestNode = heapNodes[smallest];
		    Node currentNode = heapNodes[index];
		    
		    mhPQ[smallestNode.id] = index;
		    mhPQ[currentNode.id] = smallest;
		    swapNode(index, smallest);
		    index = smallest;
		}
		
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
