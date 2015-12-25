/*Project 4
05/04/2015
Shuchen Wu(No Partner)
lab # 29873*/

public 	class Edge {
		MyNode f = new MyNode();
		MyNode t = new MyNode();
		String RoadID;
		public Edge next;

		//the to node stores weight

		public Edge(MyNode from, MyNode to, double weight, String roadID) {
			this.f = from;
			this.f.isknown = false;
			
			
			this.t = to;
			this.t.isknown = false;
			
			this.f.weight  = weight;
			this.t.weight = weight;
			
			this.RoadID = roadID;
		}
		public Edge(){
			
		}
		
	}

