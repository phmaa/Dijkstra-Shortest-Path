/*
 * Final version   
 * Updated on November 03, 2020 
 */
import java.util.LinkedList;
import java.util.Scanner;

public class DijkstraShortestPath {
	
	/*
	 * Properties of the adjacency list 
	 */
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
	
	/*
	 * Each node will store the vertex id and 
	 * distance from source vertex to the vertex
	 */
	public static class Node{
		int id;
		double distance;
	}

	public static class Graph{
		LinkedList<Edge>[] adjacencyList;
		int vertices;

		public Graph(int vertices) {
			this.vertices = vertices;
			adjacencyList = new LinkedList[vertices + 1];

			for (int i=1; i<=vertices; i++) {
				adjacencyList[i] = new LinkedList<>();
			}
		}

		public void addEdge(int source, int destination, double weight) {
			Edge edge = new Edge(source, destination, weight);
			adjacencyList[source].addFirst(edge);    
		}
		
		/**
		 * Compute the shortest distance from the source 
		 * to every other vertex in the weighted digraph
		 * @param source
		 */
		public void dijkstra(int source) {
			int INFINITY = Integer.MAX_VALUE;
			
			// store vertices for which the best distance from the source has been determined
			boolean[] visited = new boolean[vertices+1];
			visited[0] = true; // skip vertex 0
			
			// track the previous node to reconstruct the path
			int[] parents = new int[vertices+1]; 
			parents[0]= -1;  // set a sentinel value for vertex 0
			parents[source] = -1;   // set a sentinel value for source node
			
			// initialize each node with an id and set the distance to infinity
			Node[] nodes = new Node[vertices+1];
			for (int i=0; i<=vertices; i++) {
				nodes[i] = new Node();
				nodes[i].id = i;    
				nodes[i].distance = INFINITY;
			}
			nodes[0] = null;
			
			// set source node distance to self to 0
			nodes[source].distance = 0; 

			// insert nodes to min-heap
			MinHeap minHeap = new MinHeap(vertices);
			for (int i=1; i<=vertices; i++) {   
				minHeap.insert(nodes[i]);  
			}

			// find the best d(v) for each edge leaving u
			while (!minHeap.isEmpty()) {
				Node vertex = minHeap.ExtractMin();
				int vertexId = vertex.id;
				
				// keep track of the vertices that are still in min-heap
				visited[vertexId] = true;

				LinkedList<Edge> edgeList = adjacencyList[vertexId];
				for (int i=0; i < edgeList.size();i++) {

					Edge edge = edgeList.get(i);

					int destination = edge.destination; // only check the weight of each outgoing edge

					// check the distance to the node that has not been visited
					if ( visited[destination] == false) {
						double newKey = nodes[vertexId].distance + edge.weight;
						double currentKey = nodes[destination].distance;
						if (newKey < currentKey) {
							parents[destination] = vertexId; 
							decreaseKey(minHeap, newKey, destination);  
							nodes[destination].distance = newKey;  

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
			
			// restore the heap order
			minHeap.heapifyUp(index);  
		}

		public void printDijkstra(Node[] nodes, int source, int[] parents) {
			for (int i = 1; i<=vertices; i++) {
				System.out.println("\nVertex "+ i +
						"\nShortest Distance is " + nodes[i].distance);
				System.out.print("Shortest Path is ");
				printPath(i, parents);
				System.out.println(" ");
			}
		}
		
		// helper function to print the path
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
			mhPQ = new int[maxSize+1]; // priority queue 

			heapNodes = new Node[maxSize + 1]; // start the array from index 1
			heapNodes[0] = new Node();
			heapNodes[0].id = -1;
			heapNodes[0].distance = Integer.MIN_VALUE;	    
		}

		// helper functions to check the current size of min-heap
		public boolean isEmpty() {	    	
			return size == 0;
		}

		public int heapSize() {
			return size;
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

		public void heapifyUp(int index) {
			int parent = index/2;
			int currentIndex = index;
			while (currentIndex >0 && 
					heapNodes[parent].distance > heapNodes[currentIndex].distance) {
				Node currentNode = heapNodes[currentIndex];
				Node parentNode = heapNodes[parent];

				mhPQ[currentNode.id] = parent;
				mhPQ[parentNode.id] = currentIndex;
				swapNode(currentIndex, parent);
				currentIndex = parent;
				parent = parent/2;
			}
		}

		/*
		 * Extract the min mode from the heap and 
		 * maintain the heap order
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
			int leftChild = 2*index;
			int rightChild = 2*index + 1;

			if (leftChild < heapSize() && heapNodes[smallest].distance > heapNodes[leftChild].distance) {
				smallest = leftChild;}

			if (rightChild < heapSize() && heapNodes[rightChild].distance < heapNodes[leftChild].distance) {
				smallest = rightChild;
			}

			if (smallest != index) {
				Node smallestNode = heapNodes[smallest];
				Node currentNode = heapNodes[index];

				mhPQ[smallestNode.id] = index;
				mhPQ[currentNode.id] = smallest;
				swapNode(index, smallest);
				heapifyDown(smallest);
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
			
			// populate edges for the original graph
			graph.addEdge(from, to, w);		
		}

		sc1.close();
		sc2.close();
		int source = 1;
		graph.dijkstra(source);

	}

}

