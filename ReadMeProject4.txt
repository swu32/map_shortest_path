
05/04/2015
Shuchen Wu(No Partner)



Files included:

Graphd.java
test.java
myLinkedList.java
Node.java
MyNode.java
Edge.java



test files:
nys.txt
ur.txt
monroe.txt


The program is going to read in file with all the points and lines, and plot the map if specified by user. 


Run instruction:

As indicated from the project manual, you can input one of the following as you please.

java test map.txt
java test map.txt -show
java test map.txt -directions i1 i2
java test map.txt -meridianmap
java test map.txt -show -directions i1 i2
java test map.txt -show -meridianmap

Compile instructions:

javac test.java map.txt
javac test.java map.txt -show
javac test.java map.txt -directions i1 i2 
javac test.java map.txt -meridianmap
javac test.java map.txt -show -directions i1 i2
javac test.java map.txt -show -meridianmap

I will explain this project in sections:
The primary figure of this project include the following:

1) Read in and plot a map file
2) Find the shortest path between 2 user defined intersections
3) Find the MWST using prim’s algorithm. 




The basic unit to contain the vertices is 
the Arraylist ID, which stores String all the IDs of the vertices(intersections)
the ArrayList coordinate, which stores 2 dimension doubles for the longitude and altitude of the vertices. 

The basic unit for connection is ArrayList EdgeColl
EdgeColl stores a collection of edge objects, which contains 

Object MyNode from: 
Object MyNode to: t.weight stores the distance between 2 egdes
note that this implementation seems to be one directional


*****************1) Read in and plot a map file****************************
As the program read in file of interceptions and roads, it calls readinput()
which scan through all the intersections, creat a new Node object for each intersection, and then put the name of the node as string and the new node object into a hashMap called nodeMap. 


When the program detects the road specification, it would scan through the node name and get the Node from nodeMap with String hashkey from the intersection’s name. x1y1, and x2y2 would store the coordinate of both intersections for drawing, and EdgeColl would store the pair of Edge Collection. Weight calculated from using the distance method, which uses haversine’s formula to calculate the distance between 2 points with longitude and latitude, assuming earth’s radius is around 3 thousand miles, thus it stores the distance in miles. 


Reading data process takes O(20n), which is around O(n).



Plotting Map:O(4log(n) + 13n)

once the user input show, the progam would call calculate maxim, which scanns thou longitude and latitude array list, find the minimum and maximum and store them as global variables for the program. This process takes about O(4log(n)), because it performs the sorting for 4 times, other process are constants, so this method takes about O(4log(n) + n + 8) which is O about O (4log(n))


paintComponent is called at the beginning of the program, but it does not execute until enough information is provided. It runs a loop through x1y1 and x2y2, obtaining longitude and latitude data, then uses scale map method to produce relative coordinates. Scale map takes around O(8) run time, looping takes O(13n)(O(N)) plus drawing. 


*****************2) Dijkstra****************************
The program  would build the adjacent list implementation of all the Edges from the EdgeColl. 

the main would call BuildAdjList, which creates an array of linked list with length of number of ertices O(1), initialize them O(n), and then as it iterate through the EdgeColl array list, it inserts Edges into the linked list structure of adjunct list. 

Insert method would dublicate for each non repeated Edges because of the indirect graph structure from the map. An insert method involves looking up on the list to prevent duplication, which is O(n) depend on how many elements has already been on the list of Edges, then insert Edge into both the vertex of “from” and vertex of “to”O(2), inserting one element takes about O(2k), whth k being the number elements already on the list, which is very small on average except for some major connection intersections, so I would approximate it with constant time. The iteration process would give it O(2kn) time for insert. 

So buildadjlist would take O(2kn +n) time

dijkstra:
initialize edgem distance, and known array.O(3)
initialize them O(3n)
priority queue keeps track of the nodes with the distance as it creates a new node class when a distance is claimed to be acceptable.For the runtime analysis, it is hard to tell from the code itself. But I perceive it as if the algorithm starts at the start vertex, and grows a tree that eventually spans every vertice from x. vertices are added to the tree in the order of distances. 


 With the help of the priority queue, it runs in a O(Elog(V)) time. With E being the total number of Edges, and V being the number of vertices. log(V) comes from the remove function from the priority queue. 

 Which grows slower as data increases, as compared to O(EV) without the priority queue, because otherwise dijkstra needs to sort the distance every time when it needs the distance to compare with something. This has significantly changes the running time. 


showDijkstra takes O(2n) time because of the 2 iteration it need to do. 




**********************3) Find the MWST using prim’s algorithm*************
Prim’s algorthm is modified from the dijkstra’s algorthm. It runs O(ElogV) time with the help of priority queue. Again this can be understood as growing a tree from the starting node. This is very similar to Dijkstra’s algorithm. 



From ur.txt it is not hard to observe that instead of numbers to represent vertices, we have different names for different intersections. 
However, since our previous code of adjunct list only supports numbered vertex, which is also more convinent, it is better to convert different name of the vertices into different numbers, thus the first thing to do in read input is to identify intersections, and then convert those names of intersections into vertex numbers, and store the x, and y coordinate of that intersection.





**********************SideNote:**********************

Originally, the most time consuming part of this program is to have the program read in the data. It takes second for the UR map, but several minutes for the monroe map and around 10 minutes for the new york state map. That was the initial challenge for this map. 


However, after consulting Ta, I realized that the most time consuming part of the program to draw the map is to look up data from a large data set of the array list without having any clue. It takes roughly O(n^2) to do the lookup Given that roads have the string IDs of an intersection, I was think of a data structure that has O(1) random lookups so that I can reduce reading operation down to O(r), which is a hash map, and hash map has improved the run time significiantly. 





	By asking Ryan, I was informed that it is perfectly legit to use both Kruskal or Prim’s algorithm. An initial choice was made to do Prim’s algorithm, however, it was not until close to the end of all those effort did I discover Prim’s algorithm is imperfect for this type of problem. 

	Because prim’s algorithm is based on the assumption that every node can be connected to any other node on the graph. For the algorithm the program chooses a user defined startindex, and then find the Minimal weight spanning tree from that index. It works fine for the UR.txt.

However, as this map gets bigger, this assumption is no longer valid. A scan through the array gives out the result that some intersections in new york state is not able to connect to EVERY OTHER intersection for the map. 

	This circumstance is not unique for single nodes, but for many. if you uncomment the while loop in the main calling primMWST recursively, it scans through output of Edge array from primMWST, once it discovered a “null” that is not the startIndex, then it would increment the startIndex one more. Until there comes a startIndex that can span through the whole map vertex.
	
	This method theoritically works. However, I comment this because once this part is appended into the program, it increases the runtime to O(n^2), which makes it extremely slow on a big data structure. 

	At this point I think it is too late to rewrite something else. I am sorry this program is not perfect as I want it to be. I have to study for other finals, this is the best I can do so far. I have improved the run time significantly through using priority queue for Dijkstra and hash map for the plotting. Therefore all showing graph takes less than 10 second, and Dijkstra takes about less than a minute for the program to finish. 


	The map with the grey background and colors is atheactically pleasing to the eye. Give extra credit as you please.



**********************SAMPLE OUTPUT*************


The input  map file is ur.txt
There are 124 intersections 
There are 181 roads  on this Map
startIndex is i2 toIndexis i3
In order to get from i2 to i3 Please 
 take Road r2 to get from  i2 to i1,  take Road r3 to get from  i1 to i3, 
total milage for this path is 0.02229247032175213 miles

…
Sample picture is included in the output file. 

 

	