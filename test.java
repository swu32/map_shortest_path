/*Project 4
05/04/2015
Shuchen Wu(No Partner)
lab # 29873*/


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

class test extends JPanel {
	static ArrayList<Edge> pathDij;
	static Edge[] pathMWST;
	static double[] coDi = new double[4];
	static int numPixel = 500;
	public static final double R = 3963.2;//radius in miles

	static HashMap<String, MyNode> nodeMap = new HashMap<String, MyNode>();
	static boolean showO;
	static boolean direcO;
	static boolean meriO;
	static String show = "-show";
	static String meridianmap = "-meridianmap";
	static String directions = "-directions";




	// stores min and max of the longitude and latitude
	static int width;
	static int height;
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	static ArrayList<double[]> coordinate = new ArrayList<double[]>();
	static ArrayList<double[]> x1y1 = new ArrayList<double[]>();
	static ArrayList<double[]> x2y2 = new ArrayList<double[]>();

	static ArrayList<Edge> EdgeColl = new ArrayList<Edge>();
	static int vtxcounter = 0;
	static int rdCounter = 0;
	static String mapName;

	public static void calculateMaxMin() {

		double[] x1 = new double[x1y1.size()];
		double[] y1 = new double[x1y1.size()];
		double[] x2 = new double[x2y2.size()];
		double[] y2 = new double[x2y2.size()];

		for (int i = 0; i < x1y1.size(); i++) {

			x1[i] = x1y1.get(i)[0];
			y1[i] = x1y1.get(i)[1];
			x2[i] = x2y2.get(i)[0];
			y2[i] = x2y2.get(i)[1];



		}
		Arrays.sort(x1);
		Arrays.sort(x2);
		Arrays.sort(y1);
		Arrays.sort(y2);
		coDi[0] = Math.min(x1[0], x2[1]);
		coDi[1] = Math.max(x1[x1.length - 1], x2[x2.length - 1]);
		coDi[2] = Math.min(y1[0], y2[1]);
		coDi[3] = Math.max(y1[y1.length - 1], y2[y2.length - 1]);
	}

	public void getmin(double[] sortedArray) {
		System.out.println(sortedArray[0] + "   "
				+ sortedArray[sortedArray.length]);
	}

	// return scaled version of the map

	public static double[] scaleMap(double[] x1y1x2y2) {
		double ratioX = height / (coDi[1] - coDi[0]);
		double ratioY = width / (coDi[3] - coDi[2]);// max range of
		double cX = coDi[0];
		double cY = coDi[2];

		double[] scal = new double[4];

		scal[0] = height - (x1y1x2y2[0] - cX) * ratioX;
		scal[1] = (x1y1x2y2[1] - cY) * ratioY;
		scal[2] = height - (x1y1x2y2[2] - cX) * ratioX;
		scal[3] = (x1y1x2y2[3] - cY) * ratioY;
		return scal;

	}

	public static void main(String[] args) throws IOException {

		showO = false;
		direcO = false;
		meriO = false;
		
		
		try {
			int startIndex = 1;// default starting vertx for prim's algorithm
			@SuppressWarnings("unused")
			int toIndex = 0;

			// input the name of the graph txt file
			Graphd newgraph = new Graphd(false);
			mapName = args[0].toString();

			readinput(args[0]);//O(20n)
			newgraph.BuildAdjList(EdgeColl, vtxcounter);//So buildadjlist would take O(2kn +n) time

			newgraph.inputVtx(vtxcounter, nodeMap);//O(1)

			// newgraph.show(newgraph)
			// print out the list
			System.out.println("The input  map file is " + args[0]);
			System.out.println("There are " + vtxcounter + " intersections ");
			System.out.println("There are " + rdCounter + " roads  on this Map");
			try {
				if (args[1].equals(directions)) {
					direcO = true;//O(1)
					if (nodeMap.containsKey(args[2]) && nodeMap.containsKey(args[3])) {//O(1)
						startIndex = nodeMap.get(args[2]).vtx;//O(1)
						toIndex = nodeMap.get(args[3]).vtx;//O(1)

						pathDij = (ArrayList<Edge>) newgraph.dijkstra(
								args[2], args[3], newgraph);
						
						showDijkstra(pathDij, args[2], args[3]);
					} else {
						System.out
								.println("Error, Road Specified is not found, please recheck input method! ");
					}

				} else if (args[2].equals(directions)) {
					direcO = true;
					if (nodeMap.containsKey(args[3]) && nodeMap.containsKey(args[4])) {

						pathDij = (ArrayList<Edge>) newgraph.dijkstra(
								args[3], args[4], newgraph);
						showDijkstra(pathDij, args[3], args[4]);
					} else {
						System.out
								.println("Error, Road Specified is not found, please recheck input method! ");
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			for (int i = 0; i < args.length; i++) {

				if (args[i].equals(meridianmap)) {
					meriO = true;
					pathMWST = newgraph.PrimMWST(startIndex);
									
					System.out.println("The road travelled from the meridianmap are : ");
					//int rd = 0;
					for (int i1 = 0; i1 < pathMWST.length; i1++) {
						if(pathMWST[i1] == null){
							//System.out.printf("null"+ " , ");
						}else{
							//rd++;
							System.out.println(pathMWST[i1].RoadID);}
					}
					//System.out.println(rd);
					//if path contains null that is not start index,
					//redo the pathMWST
					
					boolean Legit = checkspan(pathMWST, startIndex);
					if(Legit==false){
						System.out.println("this is not a full tree, try to change the starting node to include isolated areas");}		
					/*
					while(Legit == false){
						System.out.println(":(");//you can tell the run time with the increment of sad face
						startIndex = startIndex+1;
						Edge[] newpathMWST = newgraph.PrimMWST(startIndex);
						Legit = checkspan(newpathMWST, startIndex);
						if(Legit==true){
							break;}					
					}*/
				}
			}
			
			if (args[1].equals(show)) {
				showO = true;

				/********* Draw Image ************/
				calculateMaxMin();//O(4log(n) + n + 8) 

				test panel = new test();
				JFrame application = new JFrame();
				application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				application.add(panel); // add the panel to the frame

				application.setSize(numPixel, numPixel); // set the size of the
															// frame
				application.setVisible(true); // make the frame visible

			}



		} catch (FileNotFoundException e) {
			System.out.println("cannot find file specified");
			e.printStackTrace();
		}
	}

	public static boolean checkspan(Edge[] pathMWST, int startIndex){
		boolean Legity = true;
		for(int i2 = 0; i2< pathMWST.length; i2++){
			if ((pathMWST[i2] ==null )){
				if((i2!=startIndex)){
					Legity = false;
					break;
				}
			}
		}
		return Legity;
	}
	public static void showDijkstra(ArrayList<Edge> pathDij, String start,
			String toIndex) {
		System.out.println("startIndex is " + start + " toIndexis "
				+ toIndex);
		System.out.println("In order to get from " + nodeMap.get(start).ID
				+ " to " + nodeMap.get(toIndex).ID + " Please ");

		for (int i = pathDij.size() - 1; i >= 0; i--) {
			if(pathDij.get(i) == null){
				System.out.println("Error, cannnot get from " + start +" to " +toIndex  );
			}
			System.out.println(" take Road " + pathDij.get(i).RoadID
					+ " to get from  " + pathDij.get(i).f.ID + " to "
					+ pathDij.get(i).t.ID + ", ");
		}

		System.out.println();

		double totalDistance = 0;
		for (int i = 0; i < pathDij.size(); i++) {
			totalDistance = totalDistance + pathDij.get(i).t.weight;
		}
		System.out.println("total milage for this path is " + totalDistance
				+ " miles");

	}
	//readinput takes around O(20n)
	public static void readinput(String name) throws IOException{
		String fileName = name.toString();//O(1)
		FileReader inputFile = new FileReader(fileName);//O(1)
		@SuppressWarnings("resource")
		BufferedReader newFile = new BufferedReader(inputFile);//O(1)

		String line = newFile.readLine();//O(1)

		while (line != null) {
			StringTokenizer newline = new StringTokenizer(line);//O(1)

			String next = newline.nextToken();//O(1)

			//O(vtxcounter*7)
			if (next.equals("i")) {//(O(1))
				String location = newline.nextToken();//O(1)
				//ID.add(location);//O(1)

				double[] longlati = new double[2];//O(1)
				double x = Double.parseDouble(newline.nextToken());//O(1)
				double y = Double.parseDouble(newline.nextToken());//O(1)

				longlati[0] = x;//O(1)
				longlati[1] = y;//O(1)
				MyNode newNode = new MyNode();

				newNode.x = x;//O(1)
				newNode.y = y;//O(1)
				newNode.vtx = vtxcounter;//O(1)
				newNode.ID = location;//O(1)
				
				nodeMap.put(location, newNode);//O(1)

				coordinate.add(longlati);//O(1)				
				//coordinate stores longitude and latitude of points

				vtxcounter++;
			}

			else if (next.equals("r")) {

				String roadID = newline.nextToken();//O(1)
				String Itx1 = newline.nextToken();//O(1)
				String Itx2 = newline.nextToken();//O(1)
				//road and intersections
				double [] X1Y1 = {nodeMap.get(Itx1).x,nodeMap.get(Itx1).y};//O(1)
				double [] X2Y2 = {nodeMap.get(Itx2).x,nodeMap.get(Itx2).y};//O(1)

				x1y1.add(X1Y1);//O(1)
				x2y2.add(X2Y2);//O(1)
				//x1y1, x2y2 stores information based on the order of roads
				
				
				
				
				// distance between point one and the other
				double distance = distance(X1Y1, X2Y2);//O(1)


				Edge AnewEdge = new Edge(nodeMap.get(Itx1), nodeMap.get(Itx2), distance, roadID);
				//System.out.println(AnewEdge.RoadID +AnewEdge.f.ID+ AnewEdge.t.ID );
				EdgeColl.add(AnewEdge);
				//System.out.println(AnewEdge.RoadID + ", " + AnewEdge.f.weight
					//	+ " " + AnewEdge.t.weight);
				
				
				rdCounter++;//O(1)

			}

			line = newFile.readLine();
		}
		
	}
	
	// return the distance between 2 points
	public static double distance(double[] x1y1, double[] x2y2) {
		double dist = haversine(x1y1[0],x1y1[1],x2y2[0],x2y2[1]);
		
		return dist;
	}
	
	
	public static  double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
//O(13n)
	public void paintComponent(Graphics g) {
		if(showO == true){
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);

		g.setColor(Color.BLUE);

		width = getWidth();
		height = getHeight();
		// g.drawLine( 0, 0, width, height );
		// g.draw((Shape) new Point2D.Double(scal[1],scal[0]));

		for (int i = 0; i < x1y1.size(); i++) {

			double[] x1y1x2y2 = new double[4];

			x1y1x2y2[0] = x1y1.get(i)[0];
			x1y1x2y2[1] = x1y1.get(i)[1];
			x1y1x2y2[2] = x2y2.get(i)[0];
			x1y1x2y2[3] = x2y2.get(i)[1];

			double[] scal = scaleMap(x1y1x2y2);//(O(8))

			g.drawLine((int) scal[1], (int) scal[0], (int) scal[3],
					(int) scal[2]);
		}
		
		}

		if (meriO == true) {//O(5n)
		//	System.out.println("meridian map is working");

			g.setColor(Color.CYAN);
			for (int i = 0; i < pathMWST.length; i++) {

				double[] x1y1x2y2 = new double[4];
				if (pathMWST[i] == null) {
				} else {

					x1y1x2y2[0] = pathMWST[i].f.x;
					x1y1x2y2[1] = pathMWST[i].f.y;
					x1y1x2y2[2] = pathMWST[i].t.x;
					x1y1x2y2[3] = pathMWST[i].t.y;
					
					double[] scal = scaleMap(x1y1x2y2);
					g.drawLine((int) scal[1], (int) scal[0], (int) scal[3],
							(int) scal[2]);
				}
			}
		}

		if (direcO == true) {
			g.setColor(Color.RED);
			for (int i = 0; i < pathDij.size(); i++) {

				double[] x1y1x2y2 = new double[4];
				if (pathDij.get(i) == null) {
				} else {

					x1y1x2y2[0] = pathDij.get(i).f.x;
					x1y1x2y2[1] = pathDij.get(i).f.y;
					x1y1x2y2[2] = pathDij.get(i).t.x;
					x1y1x2y2[3] = pathDij.get(i).t.y;

					double[] scal = scaleMap(x1y1x2y2);

					g.drawLine((int) scal[1], (int) scal[0], (int) scal[3],
							(int) scal[2]);
				}
			}
		}

		// g.drawLine( 0, height, width, 0 );
	}
	


	public static void MWST(ArrayList<Edge> EdgeColl) {

		ArrayList<Edge> MWST = new ArrayList<Edge>();

		// find the index of the minimum spanning tree
		double[] weights = new double[EdgeColl.size()];// stores the values of
														// weights
		double[] sorted = new double[EdgeColl.size()];// stored sorted weights

		for (int i = 0; i < EdgeColl.size(); i++) {
			weights[i] = EdgeColl.get(i).t.weight;
			sorted[i] = EdgeColl.get(i).t.weight;
		}

		Arrays.sort(sorted);

		for (int i = 0; i < sorted.length; i++) {
			int index = Arrays.binarySearch(weights, sorted[i]);

			if (!EdgeColl.get(index).f.isknown) {// add it to the node
				// collection
				EdgeColl.get(index).f.isknown = true;
				MWST.add(EdgeColl.get(index));
			}
			if (!EdgeColl.get(index).t.isknown) {
				EdgeColl.get(index).t.isknown = true;
				if (!MWST.contains(EdgeColl.get(index))) {
					MWST.add(EdgeColl.get(index));
				}
			}
		}
	}
}
