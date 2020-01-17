/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode Min = null;
	private HeapNode First = null;
	private int size = 0;
	private int numOfTree = 0;
	private int numOfMarked = 0;
	private static int TotalCut = 0; 
	private static int TotalLinks = 0;


   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    * 
    * Time Complexity:= O(1).
    *   
    */
    public boolean isEmpty()
    {
    	return Min == null;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * precondition: key >= 0.
    * 
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    * returns address of the node.
    * 
    * Time Complexity:= O(1).
    */
    public HeapNode insert(int key)
    {   
    	numOfTree++;
    	size++;
    	HeapNode newNode = new HeapNode(key);
    	// if heap is not empty.
        if(First != null ) {
        	newNode.next = First;
        	newNode.prev = First.prev;
            First.prev = newNode;
            newNode.prev.next = newNode;

            if( key < Min.key ) {
                Min = newNode;
            }
            return newNode;
        }
        // if heap is empty.
        else {
        	Min = newNode;
        	First = newNode;
        }
        return newNode;
    }
    
    /**
     * public HeapNode insert(int key, HeapNode godFather)
     *
     * precondition: key >= 0.
     * 
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
     * returns address of the node.
     * sets the field godFather to the address given in godFather.(this helps in kmin)
     * 
     * Time Complexity:= O(1).
     */
    
    public HeapNode insert(int key, HeapNode godFather) {
    	numOfTree++;
    	size++;
    	HeapNode newNode = new HeapNode(key, godFather);
        if(Min != null ) {
        	newNode.next = First;
        	newNode.prev = First.prev;
            First.prev = newNode;
            newNode.prev.next = newNode;

            
            if( key < Min.key ) {
                Min = newNode;
            }
            return newNode;
        }
        else {
        	Min = newNode;
        	First = newNode;
        }
        return newNode;
    }
    
    
   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key, and calls consolidating.
    * 
    * precondition: none.
    * 
    * Time Complexity:= Amortized O(log(n)).
    */
    public void deleteMin()
    {
    	// heap is empty
    	if(Min==null) {
    		return;
    	}    	
    	size--;
    	numOfTree--;
    	// if heap is exist one node only. 
    	if (size == 0) {
    		Min = null;
    		First = null;
        	return;
    	}
    	// there are more than one node in heap
    	// node of Min have children added 
    	//each one of him to the heap as trees. 
    	if(Min.child != null) {
    		HeapNode firstChild = Min.child;
    		HeapNode temp = firstChild;
    		
    		do {
            	numOfTree++;
    			if (temp.mark == 1) {
    				temp.mark = 0;
    				numOfMarked--;
    			}
    			temp.Parent = null;			
    			temp = temp.next;
    			
    			}
    		while(!temp.equals(firstChild)); 
    		First = firstChild;
    		temp = temp.prev;
    		// if Min has one child.
    		if(!Min.next.equals(Min)) {
    			Min.prev.next = firstChild;
    			firstChild.prev = Min.prev;
    			Min.next.prev = temp;
    			temp.next = Min.next;
    		} 
    	}
    	
    	else {
    		Min.prev.next = Min.next;
    		Min.next.prev = Min.prev;
    		First = Min.next;
    	}
    	
        Consolidating();
        updateMin();
    }

   private void updateMin() {
	   HeapNode temp = First.next;
	   Min = First;
	   while(!temp.equals(First)) {
		   if(Min.key > temp.key)
			   Min = temp;
		   temp = temp.next;
		}
   }

   /*
    * consolidates the Fibanacci heap as we learned in class.
    */
   private void Consolidating() {
	   HeapNode[] array = new HeapNode[size+1];    
	   HeapNode x = First;
	   int numTree=numOfTree;
    
	   while(numTree>0) {
		   int rank = x.rank;
		   HeapNode next = x.next;
		   while( array[rank] != null ) {
			   HeapNode y = array[rank];
			   if(y.key<x.key) {
				   HeapNode temp = x;
				   x=y;
				   y=temp;
			   }
			   link( y, x );
			   array[rank] = null;
			   rank++;
		   }
		   array[rank] = x;
		   x = next;
		   numTree--;
	   }
    
	   numOfTree = 0;
	   First = null;
	   x = null;
    
	   for( int i = 0; i < array.length; i++ ) {
		   if( array[i] != null ) {
			   numOfTree++;
			   if(First != null) {	
				   array[i].next = x.next;
				   x.next = array[i];
				   array[i].prev = x;
				   x = array[i];
			   }
			   else {
				   First = array[i];
				   First.next = First;
				   First.prev = First;
				   x = First;
			   }
		   }
	   }
	   First.prev = x;
   }
   

   /**
    *    precondition: x.key<y.key.
    *    
    *    links the two nodes.
    *    
    *    Time Complexity:= O(1).
    */

   private void link(HeapNode y, HeapNode x) {
	   TotalLinks++;
	   	y.prev.next = y.next;
		y.next.prev = y.prev;
		y.Parent = x;
		
		if(x.child == null) {
			y.next = y;
			y.prev = y;
		}
		else {
			y.next = x.child;
			x.child.prev.next = y;
			y.prev = x.child.prev;
			x.child.prev = y;
			
		}
		
		x.child = y;
		x.rank++;
	
   }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    * Time Complexity:= O(1).
    * 
    */
    public HeapNode findMin()
    {
    	return Min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds the current heap with the given heap2.
    * 
    * Time Complexity:= O(log(n)).
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if(heap2.isEmpty()) 
    		return;
    	
    	if(this.isEmpty() ) {
    		this.First = heap2.First;
    		this.Min = heap2.Min; 
    		size = heap2.size;
    		numOfTree = heap2.numOfTree;
    		numOfMarked = heap2.numOfMarked;
    		return;
    		}
    	
    	size = size+heap2.size;
		numOfTree = numOfTree+heap2.numOfTree;
		numOfMarked = numOfMarked+heap2.numOfMarked;
    	HeapNode temp1 = heap2.First.prev;
    	HeapNode temp2 = this.First.prev;
    	heap2.First.prev = temp2;
    	this.First.prev = temp1;
    	temp1.next = this.First;
    	temp2.next = heap2.First;
    	
    	//update Min
    	if(heap2.Min.key < Min.key) 
    		Min = heap2.Min;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    * 
    * Time Complexity:= O(1).
    *   
    */
    public int size()
    {
    	return size ; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Returns a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    * Time Complexity:= O(n).
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[size];
    	if(Min == null) {
    		return arr;
    	}
    	HeapNode temp = Min.next;
    	arr[Min.rank]++;
    	while(!temp.equals(Min)) {
    		arr[temp.rank]++;
    		temp = temp.next;
    	}
    	return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap by doing decreaseKey and deletMin.
    * 
    * Time Complexity:= Amortized O(log(n)).
    *
    */
    public void delete(HeapNode x) 
    {    
    	if(x == null)
    		return;
        decreaseKey(x, x.key-Min.key+1);
        this.deleteMin();
        
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * 
    * Time Complexity:= Worst case O(log(n)).
    * 
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	if( delta < 0 || x==null ) 
            return;
        
    	x.key = x.key - delta;
    	
    	if(x.equals(Min))
    		return;
    	
        HeapNode y = x.Parent;
        if(y == null) {
        	if(x.key < Min.key)
        		Min = x;
        	return;
        }
        
        if( x.key < y.key) 
            cascading_cut(x,y);
    }

    /*
     * does the cascading cuts learned in class.
     */
      
   private void cascading_cut(HeapNode x, HeapNode y)
   {	
	   cut(x,y);
	   if(y.Parent != null) {
		   if(y.mark == 0) {
			y.mark = 1;
			numOfMarked++;
			return;
		   }
		   
		   cascading_cut(y,y.Parent);
	   }
	}

   /*
    * 
    * cuts the node and joins it to the linked list.
    * 
    */
   private void cut(HeapNode x, HeapNode y) 
   {
	   TotalCut++;
	   numOfTree++;
	   x.Parent = null;
	   
	   if(x.mark == 1) {
		   numOfMarked--;
		   x.mark = 0;
	   }
	   
	   y.rank = y.rank-1;
	   if(x.next.equals(x))
		   y.child = null;
	   
	   else {
		   y.child = x.next;
		   x.prev.next = x.next;
		   x.next.prev = x.prev;
	   }
	   
	   Min.next.prev = x;
	   x.next = Min.next;
	   Min.next = x;
	   x.prev = Min;
	   
	   if(x.key < Min.key) {
		   Min = x;
	   }

   }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap.
    * 
    *  Time Complexity:= O(1).
    *  
    */
    public int potential() 
    {    
    	return numOfTree + numOfMarked*2; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    * 
    * Time Complexity:= O(1).
    * 
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
    *  
    *  Time Complexity:= O(1).
    *  
    */
    public static int totalCuts()
    {    
    	return TotalCut; 
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * 
    * Time Complexity:= O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    

    	if(k == 0) {
    		return null;
    	}
    	
        int[] retArr = new int[k];
        //first min.
        HeapNode node = H.Min;
        HeapNode sonNode;
        HeapNode otherSonNode;
        FibonacciHeap FHNode = new FibonacciHeap();
        retArr[0] = H.Min.key;
        //gets the the first min's children.
        if(node.child != null) {
        	sonNode = H.Min.child;
        	otherSonNode = sonNode;
        	FHNode.insert(sonNode.key, sonNode);
            while(sonNode.next != otherSonNode) {
            	sonNode = sonNode.next;
            	FHNode.insert(sonNode.key, sonNode);
            }
        }

        for (int i = 1; i<k; i++) {
        	HeapNode node1 = FHNode.Min.godFather;
        	retArr[i] = node1.key;
        	FHNode.deleteMin();
            if(node1.child != null) {
            	sonNode = node1.child;
            	otherSonNode = sonNode;
            	FHNode.insert(sonNode.key, sonNode);
                while(sonNode.next != otherSonNode) {
                	sonNode = sonNode.next;
                	FHNode.insert(sonNode.key, sonNode);
                }
            }
        }
        
        return retArr;

    }
    
   /**
    * public class HeapNode
    * 
    * an implementation of the node that holds the the value of the key.
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
    	private HeapNode godFather;
    	
    	public HeapNode(int key) {
    		this.key = key;
    		this.prev=this;
    		this.next=this;
		}
    	
    	public HeapNode(int key, HeapNode godFather) {
    		this(key);
    		this.godFather = godFather;
		}
    	
    	public int getKey() {
    		return this.key;
		}
    	
    	public boolean equals(HeapNode t) {
    		return t.key==key;
		}
    }
}
