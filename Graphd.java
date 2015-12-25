/*Project 4
05/04/2015
Shuchen Wu(No Partner)
lab # 29873*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Graphd {
	private static int  vertexcounter, EdgeCount; // number of vertices and edges
	boolean directed; // false for undirected graphs
	private static myLinkedList adj[];
	int INFINITY = 100000;
	static HashMap<String, MyNode> thisMap;

	ArrayList<Edge> EdgeColl ;
	int rdCounter;


	public Graphd( boolean isDirected) {
		directed = isDirected;

	}
	public void inputVtx(int vtx, HashMap<String, MyNode> nodemap){
		thisMap = nodemap;
		vertexcounter  = vtx;
	}

	
	public boolean isDirected() {return directed;}
	// count the number of edges
	public int Edges() {return EdgeCount;}

	public void insert(Edge e) {//insert edges
		//contains information on IDs of nodes,
		//and other stuff
		//arrange linked list of arrays by Arraylist
		// indirected graph
		
		Edge edger = new Edge(e.t,e.f,e.t.weight,e.RoadID);

			if (adj[e.f.vtx].lookup(e) &&adj[e.t.vtx].lookup(edger)) {
			} else {
				//System.out.println("weight"+e.t.weight);
				adj[e.f.vtx].insert(e);
			//	System.out.println(e.f.vtx);
				adj[edger.f.vtx].insert(edger);
				//System.out.println(edger.f.vtx);

				//weight is always stored in "to"
				EdgeCount = EdgeCount + 2;
			}		
	}

	
	public void  BuildAdjList( ArrayList<Edge> EdgeColl,int vtxcounter) {

		
		adj = new myLinkedList[vtxcounter];

		for(int i = 0; i < adj.length; i++){
			 adj[i] = new myLinkedList();///each vertex has a linked list connection
		}

		for(int i  = 0; i < EdgeColl.size(); i++){
			insert(EdgeColl.get(i));	
		}
		
	//	System.out.println("vtxcounter"		+vtxcounter);
		// System.out.println(rdCounter);
	}


	
	public ArrayList<Edge> getEdgeColl(){
		return EdgeColl;
	}

	public void delete(Edge e) {
		if (directed) {
			adj[e.f.vtx].delete(e);

			EdgeCount--;
		} else {
			adj[e.f.vtx].delete(e);
			adj[e.t.vtx].delete(e);
			EdgeCount--;
		}
	}



	public void show(Graphd G) {
		for(int i = 0; i < adj.length; i++){
			adj[i].printList();
		}
	}
	

	
	/*
 @param int start, int number of vertices, Graohd G
 @output sorted path
 * */
	
	public ArrayList<Edge> dijkstra(String start, String to, Graphd G) {
		Edge [] FinalEdges = new Edge[vertexcounter];//Final Edges to store
		ArrayList<Edge> Finalpath;
		
		double[] dist = new double[vertexcounter];
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>(10, new Node());
		//keep track of every vertex
		boolean[] known = new boolean[vertexcounter];
		
		//keep track of the path
		//initialize them
		for (int i = 0; i < vertexcounter; i++) {
			dist[i] = INFINITY;
			known[i] = false;
		}
		int s = thisMap.get(start).vtx;
		
		dist[s] = 0;
		priorityQueue.add(new Node(s, 0));

		// System.out.println(condition(known));
		while(priorityQueue.peek()!=null)//O(1)
		{


			//double[] sortedDist = dist.clone();
			//Arrays.sort(sortedDist);
			
			//int v = MinUnDisVtx(sortedDist,dist, known);
			int v = priorityQueue.remove().node;//O(logV)
		   //	System.out.println("v is " + v);
			if (v == -1) {//there are n vertices, so this is going through n times
				break;//O(1)
			} 
			
			else {//scan throught the linked list
				known[v] = true;//O(1)
				
				Edge temp = adj[v].head;//O(1)
				while(temp.next!=null){
					//if the vertex v connects is not known
					int w = temp.next.t.vtx;//O(1)
					if(!known[w]){

						double cvw = temp.next.t.weight;//O(1)
						if(dist[v]+cvw < dist[w]){
							dist[w] = dist[v] + cvw;//O(1)

							FinalEdges[w] =temp.next;//O(1)
							priorityQueue.add(new Node(w, dist[w]));//O(1)
						}
					}
					temp = temp.next;
				}		
				
			}
		}

		Finalpath = readpath(start, to, FinalEdges);//O(1)
		
		return Finalpath;
	}
	/*
	 find the smallest unknown distance index*/
	public int MinUnDisVtx(double [] temp, double[] distance, boolean[] known) {
		// return -1 if there is no unknown distance index
		//System.out.println(Arrays.toString(distance));

		//System.out.println(Arrays.toString(temp));
		int minDistIdx = -1;


		int k = 0;
		// temp[0] is the smallest
		while (k < distance.length) {
			for (int i = 0; i < distance.length; i++) {
				if (distance[i] == (temp[k]) && known[i] == false) {
						minDistIdx = i;
				}
			}
			if (minDistIdx == -1) {
				k++;
			} else {
				break;
			}
		}
		return minDistIdx;
	}
	
	
	
	public ArrayList<Edge> readpath(String s, String t , Edge [] FinalEdges){
		ArrayList<Edge> FinalPath= new ArrayList<Edge>();
		int num = thisMap.get(t).vtx;

		while(FinalEdges[num]!=null){
			Edge temp = FinalEdges[num];
			FinalPath.add(temp);
			 num = temp.f.vtx;
		}

		
		return FinalPath;

	}


	public int getrd(){
		return rdCounter;
	}
	
	
	
	/*
 @param int start, int number of vertices, Graohd G
 @output sorted path
 * */
	
	public Edge[] PrimMWST(int s) {
		Edge [] MWSTEdges = new Edge[vertexcounter];
		//s and t are indexes in 2 nodes from one edge
		
		//keep track of the distance from one node to the next
		double[] dist = new double[vertexcounter];
		
		//keep track of every vertex
		boolean[] known = new boolean[vertexcounter];
		
		PriorityQueue<Node> Q = new PriorityQueue<Node>(10, new Node());

		
		for (int i = 0; i < vertexcounter; i++) {
			dist[i] = INFINITY;
			known[i] = false;
			//final path to take account of the edges where 
			//node travels from and to
		//	path[i] = 0;// the nunexistant 0 node
		}
		
		dist[s] = 0;
		Q.add(new Node(s, 0));

		// System.out.println(dist[0]);

		// System.out.println(condition(known));
		while (Q.peek()!=null)// while there is an unknown distance vertex
		{

			int v = Q.remove().node;

			if (v == -1) {
				break;
			} 
			
			else {
				known[v] = true;//scan throught the linked list
				
				Edge temp = adj[v].head;
				while(temp.next!=null){
					int w = temp.next.t.vtx;
					if(!known[w]){

						double cvw = temp.next.t.weight;
							dist[w] = Math.min(dist[w], cvw);
							MWSTEdges[w] =temp.next;
							Q.add(new Node(w, dist[w]));

							//System.out.println(path[w]);
					}
					temp = temp.next;
				}
			}
		}
		return MWSTEdges;
	}
	

	public boolean condition(boolean[] known) {
		for (int i = 0; i < known.length; i++) {
			if (known[i] == false) {
				return true;
			}
		}

		return false;}
}


