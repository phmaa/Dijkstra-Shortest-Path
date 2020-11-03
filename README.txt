CSCI 6410 Fall 2020 Assignment 5
Implementation of Dijkstra's Algorithm for finding Shortest Paths

Author: Hwey-Nan Maa
Email: maah20@students.ecu.edu

Date: November 3, 2020

Files included with this projct:
    GraphRev.java	README.txt


-----------
Description
-----------
This program will implement Dijkstra's Algorithm for finding Single Source Shortest Paths in order of
O((V+E)lgV) time. It uses the Adjacency List Representation to store and process a directed weighted graph.


-----------
Compilation
-----------
Compile: javac DijkstraShortestPath.java
Run: java DijkstraShortestPath


----------------------------
Adjacency List Representation
----------------------------
The program reads the input from stdin in the following format:

n
e
a1 b1 w1
a2 b2 w2
.
.
.
ae be we

The number n is the number of vertices in the directed graph, the number e represents the total number of edges
in the graph. (ae be we) represents each edge from the vertex ae to the vertex and has weight we. The weight
is of type double in order to store non-integer weight. The source vertex is the vertex with number 1.



-----------------------------------
Priority Queue Data Structure
-----------------------------------
The MinHeap class represents the structure of the priority queue implemented using the binary heap data structure.
It contains three main functions, ExtractMin() that identifies and deletes and element with minimum key value
from a heap, heapifyDown(int index) to restore the heap order with n-1 elements after the root node is deleted, 
and heapifyUp(int index) that checks whether the heap order is violated after a new node is inserted into the 
last position, if so, fixes it. 
     


-----------------------------------
Dijkstra's Algorithm
-----------------------------------
The main function, dijkstra(int source) takes the source node as the parameter and finds the shortest path between
the source node and every other node in the graph if a path exists. It uses an array visited[] to determine whether
the shortest distance d(u) from source to u has been determined or not. For each node, it maintains the best distance
seen so far and repeatedly choose unexplored node v which minimizes the best distance seen so far. If a shorter 
distance is found, the decreaseKey function is called to update the best distance seen so far.   
 


---------------
Time Complexity
---------------
Graph construction takes O(V+E) time, where V is the total number of vertices and E total edges.
To extract each vertex from the min-heap take O(log V) time, so the total running time for V vertices is O(Vlog V).
Similarly, for all the other heap operations, heapifyUp, heapifyDown, and insert, it takes at most O(Vlog V) time.
To decrease the distance, the function is called at most E time, once for each edge, for a total of (ElogV) time.
Therefore, the overall complexity is O((V+E)log V) time.


-------------
Output Format
-------------
For each sink node, the program will output 1) the vertex number for the node, 2) the shortest distance
to the sink from the source, and 3) a sequence of vertices in the path starting from the source to the sink:

Vertex 1
Shortest Distance is 0.0
Shortest Path is 1  

Vertex 2
Shortest Distance is 4.0
Shortest Path is 1 2  

Vertex 3
Shortest Distance is 12.0
Shortest Path is 1 2 3  

Vertex 4
Shortest Distance is 19.0
Shortest Path is 1 2 3 4  

Vertex 5
Shortest Distance is 21.0
Shortest Path is 1 8 7 6 5  

Vertex 6
Shortest Distance is 11.0
Shortest Path is 1 8 7 6  

Vertex 7
Shortest Distance is 9.0
Shortest Path is 1 8 7  

Vertex 8
Shortest Distance is 8.0
Shortest Path is 1 8  

Vertex 9
Shortest Distance is 14.0
Shortest Path is 1 2 3 9  

with the given input as follows:
9
14
1 2 4
1 8 8
2 3 8
2 9 11
3 4 7
3 6 4
3 9 2
4 5 9
4 6 14
8 9 7
8 7 1
7 9 6
7 6 2
6 5 10































