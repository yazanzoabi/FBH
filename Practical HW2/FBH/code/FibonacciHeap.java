import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode Min=null; 
	private int size=0;
	private int numOfTree=0;
	private int numOfMarked=0;
	private static int TotalCut=0; 
	private static int TotalLinks=0;


   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return Min==null;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {   
    	numOfTree++;
    	size++;
    	HeapNode newNode=new HeapNode(key);
        if(Min != null ) {
        	newNode.prev = Min;
        	newNode.next = Min.next;
            Min.next = newNode;
            newNode.next.prev = newNode;

            if( key < Min.key ) {
                Min = newNode;
            }
            return newNode;
        }
        else Min= newNode;
        return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if (size==1) {
    		size=0;
    		Min=null;
        	numOfTree=0;
    	}
    	if(Min==null)
    		return;
    	if(Min.child!=null) {
    		numOfTree=numOfTree+Min.rank;
    		HeapNode firstChild =Min.child;
    		HeapNode lastChild =Min.child.prev;
    		if(Min.next.equals(Min)) {
    			Min=firstChild;
    		}
    		else {
    			Min.prev.next=firstChild;
    			firstChild.prev=Min.prev;
    			Min.next.prev=lastChild;
    			lastChild.next=Min.next;
    			Min=firstChild;
    			size--;
    		}
        }
    	else {
    		Min.prev.next=Min.next;
    		Min.next.prev=Min.prev;
    		Min=Min.next;
    		size--;
    	}
        Consolidating();
        updateMin();
    }

   private void updateMin() {
	HeapNode min=Min,temp=Min.next;
	while(!temp.equals(Min)) {
		if(min.key>temp.key)
			min=temp;
		temp=temp.next;
		}
}

private void Consolidating() {
	if (Min==null)
		return;
	HeapNode[] array = new HeapNode[numOfTree];
    for( int i = 0; i < array.length; i++ ) {
        array[i] = null;
    }
    
    HeapNode x = Min.next;
    int numTree=1;
    while(!x.equals(Min) ) {
    	x=x.next;
    	numTree++;
    }
    while(numTree>0) {
        int rank = x.rank;
        HeapNode next = x.next;
        while( array[rank] != null ) {
        	HeapNode y = array[rank];
        	if(y.key<x.key) {
        		HeapNode temp=x;
        		x=y;
        		y=x;
        	}
            link( y, x );
            array[rank] = null;
            rank++;
        }
        array[rank] = x;
        x = next;
        numTree--;
    }
    numOfTree=0;
    x=null;
    Min=null;
    for( int i = 0; i < array.length; i++ ) {
        if( array[i] != null ) {
        	numOfTree++;
        	if(Min!=null) {
        		x.next=array[i];
        		array[i].prev=x;
        		array[i].next=Min;
        		Min.prev=array[i];
        		x=array[i];
        }
            else {
                Min = array[i];
                Min.next=Min;
                Min.prev=Min;
                x=Min;
            }
        }
    }
}

private void link(HeapNode x, HeapNode y) {
	TotalLinks++;
	if(x.key>y.key) {
		HeapNode temp=x;
		x=y;
		y=temp;
	}
	y.prev.next=y.next;
	y.next.prev=y.prev;
	y.Parent=x;
	if(x.child==null) {
		y.next=y;
		y.prev=y;
	}
	else {
		y.next=x.child.next;
		x.child.next=y;
		y.prev=x.child;
		y.next.prev=y;
	}
	x.child=y;
	x.rank++;
	
}

/**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return Min;// should be replaced by student code
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return size ; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[42];
    	HeapNode temp=Min.next;
    	arr[Min.rank]++;
    	while(!temp.equals(Min)) {
    		arr[temp.rank]++;
    		temp=temp.next;
    	}
    	return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	if(x==null)
    		return;
        decreaseKey(x,x.key-Min.key+1);
        this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    

    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return numOfTree+ numOfMarked*2; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return TotalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return TotalCut; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[42];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

    
	public int key;
	private int rank;
	private int mark;
	private HeapNode child;
	private HeapNode next;
	private HeapNode prev;
	private HeapNode Parent;
	
	public HeapNode(int key) {
		this.key = key;
		this.prev=this;
		this.next=this;
	}
	public int getKey() {
		return this.key;
	}
	public boolean equals(HeapNode t) {
		return t.key==key;
	}
    }

}
