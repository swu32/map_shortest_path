
/*Project 4
05/04/2015
Shuchen Wu(No Partner)
lab # 29873*/
public class myLinkedList{
	public myLinkedList(){};//a useless constructor

	//referrence myNOde and set it to null 
	//at construction time
	//in this case, MyNode is declared as a type
	Edge head = new Edge();
	Edge tail = head;

    public void insert(Edge e){
    	if(!lookup(e)) {
    		Edge temp = e;
    		tail.next = temp;
    		tail = temp;
    	}
    }
    
    public void delete(Edge x){
    	Edge temp = head;
    	while(temp.next != null)
    	{
    		if(temp.next.equals(x))
    		{
    			if(temp.next.next == null) {
    				temp.next = null;
    				tail = temp;
    			}
    			else {
    				temp.next = temp.next.next;
    			}
    			break;
    		}
    		temp = temp.next;
    	}
    }
    
    public boolean isEmpty(){
    	if(head.next == null)
    	{
    		return true;
    	}
    	return false;
    }
    
    public boolean lookup(Edge e)//O(n) n is the length of the linkedlist 
    {
		Edge temp = head;
		while(temp.next != null)
    	{
    		if(temp.next.equals(e))
    		{
    			return true;
    		}
    		temp = temp.next;
    	}
		return false;
	}
    

	public void printList() {
		Edge temp = head;
		while (temp.next != null)
		{
			temp = temp.next;
			System.out.printf(temp.RoadID + " + from " + temp.f.ID + " + to " + temp.t.ID +"  .....  ");
		}
		System.out.println();
	}
	public int countlist() {
		int count = 0;
		Edge temp = head;
		while (temp.next != null)
		{
			temp = temp.next;
			count++;
		}
		return count;
	}
}
