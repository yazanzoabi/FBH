import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode Min=null;
	private HeapNode First=null;
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
        else {
        	Min= newNode;
        	First= newNode;
        }
        return newNode;
    }
    
    
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
        	Min= newNode;
        	First= newNode;
        }
        return newNode;
    }
    
    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
     */
    
   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if(Min==null)
    		return;
    	size--;
    	numOfTree--;
    	
    	if (size==0) {
    		Min=null;
    		First=null;
        	return;
    	}
    	
    	if(Min.child!=null) {
    		HeapNode firstChild = Min.child;
    		HeapNode temp = firstChild;
    		do {
            	numOfTree++;
    			if (temp.mark==1) {
    				temp.mark=0;
    				numOfMarked--;
    			}
    			temp.Parent=null;			
    			temp=temp.next;
    			
    			}
    		while(!temp.equals(firstChild)); 
    		First=firstChild;
    		temp=temp.prev;
    		if(!Min.next.equals(Min)) {
    			Min.prev.next=firstChild;
    			firstChild.prev=Min.prev;
    			Min.next.prev=temp;
    			temp.next=Min.next;
    		} 
    	}
    	
    	else {
    		Min.prev.next=Min.next;
    		Min.next.prev=Min.prev;
    		First = Min.next;
    	}
    	
        Consolidating();
        updateMin();
    }

   private void updateMin() {
	   HeapNode temp=First.next;
	   Min=First;
	   while(!temp.equals(First)) {
		   if(Min.key>temp.key)
			   Min=temp;
		   temp=temp.next;
		}
   }

   private void Consolidating() {
	   HeapNode[] array = new HeapNode[size+1];    
	   HeapNode x = First.next;
	   int numTree=numOfTree;
    
	   while(numTree>0) {
		   int rank = x.rank;
		   HeapNode next = x.next;
		   while( array[rank] != null ) {
			   HeapNode y = array[rank];
			   if(y.key<x.key) {
				   HeapNode temp=x;
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
    
	   numOfTree=0;
	   First=null;
	   x=null;
    
	   for( int i = 0; i < array.length; i++ ) {
		   if( array[i] != null ) {
			   numOfTree++;
			   if(First!=null) {	
				   array[i].next=x.next;
				   x.next=array[i];
				   array[i].prev=x;
				   x=array[i];
			   }
			   else {
				   First = array[i];
				   First.next=First;
				   First.prev=First;
				   x=First;
			   }
		   }
	   }
	   First.prev=x;
   }
   /**
    *    x.key<y.key  
    */
   private void link(HeapNode y, HeapNode x) {
	   TotalLinks++;
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
    	return Min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if(heap2.isEmpty()) 
    		return;
    	
    	if(this.isEmpty() ) {
    		this.First=heap2.First;
    		this.Min=heap2.Min; 
    		size=heap2.size;
    		numOfTree=heap2.numOfTree;
    		numOfMarked=heap2.numOfMarked;
    		return;
    		}
    	
    	size=size+heap2.size;
		numOfTree=numOfTree+heap2.numOfTree;
		numOfMarked=numOfMarked+heap2.numOfMarked;
    	HeapNode temp1=heap2.First.prev;
    	HeapNode temp2=this.First.prev;
    	heap2.First.prev=temp2;
    	this.First.prev=temp1;
    	temp1.next=this.First;
    	temp2.next=heap2.First;
    	
    	//update Min
    	if(heap2.Min.key<Min.key) 
    		Min=heap2.Min;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return size ; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[size];
    	if(Min==null) return arr;
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
    	if( delta < 0 || x==null ) 
            return;
        
    	x.key = x.key - delta;
    	
    	if(x.equals(Min))
    		return;
    	
        HeapNode y = x.Parent;
        if(y==null) {
        	if(x.key<Min.key)
        		Min=x;
        	return;
        }
        
        if( x.key < y.key) 
            cascading_cut(x,y);
    }

   private void cascading_cut(HeapNode x, HeapNode y)
   {	
	   cut(x,y);
	   if(y.Parent!=null) {
		   if(y.mark==0) {
			y.mark=1;
			numOfMarked++;
			return;
		   }
		   
		   cascading_cut(y,y.Parent);
	   }
	}

   private void cut(HeapNode x, HeapNode y) 
   {
	   TotalCut++;
	   numOfTree++;
	   x.Parent=null;
	   
	   if(x.mark==1) {
		   numOfMarked--;
		   x.mark=0;
	   }
	   
	   y.rank=y.rank-1;
	   if(x.next.equals(x))
		   y.child=null;
	   
	   else {
		   y.child=x.next;
		   x.prev.next=x.next;
		   x.next.prev=x.prev;
	   }
	   
	   Min.next.prev=x;
	   x.next=Min.next;
	   Min.next=x;
	   x.prev=Min;
	   
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
    	return TotalCut; 
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    

        int[] arr = new int[k];
        
        HeapNode xXHEAPNODEXx = H.Min;
        HeapNode xXSONXx;
        HeapNode xXSONXx123;
        FibonacciHeap xXMONSTERXx = new FibonacciHeap();
        //System.out.println(H.numOfTree);
        arr[0] = H.Min.key;
        if(xXHEAPNODEXx.child != null) {
        	xXSONXx = H.Min.child;
        	xXSONXx123 = xXSONXx;
            xXMONSTERXx.insert(xXSONXx.key, xXSONXx);
            while(xXSONXx.next != xXSONXx123) {
            	xXSONXx = xXSONXx.next;
            	xXMONSTERXx.insert(xXSONXx.key, xXSONXx);
            }
        }

        for (int i = 1; i<k; i++) {
        	HeapNode xXHEAPNODEXx1 = xXMONSTERXx.Min.godFather;
        	arr[i] = xXHEAPNODEXx1.key;
        	xXMONSTERXx.deleteMin();
            if(xXHEAPNODEXx1.child != null) {
            	xXSONXx = xXHEAPNODEXx1.child;
            	xXSONXx123 = xXSONXx;
                xXMONSTERXx.insert(xXSONXx.key, xXSONXx);
                while(xXSONXx.next != xXSONXx123) {
                	xXSONXx = xXSONXx.next;
                	xXMONSTERXx.insert(xXSONXx.key, xXSONXx);
                }
            }
        }
        
        return arr;

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
    public static void main(String[] arg) {
//    	FibonacciHeap.HeapNode [] j=new HeapNode[5];
//    	int r=1;
//    	FibonacciHeap.HeapNode y=null;
//    	for(int j1=0;j1<5;j1++) {
//    		j[j1]=null;
//    	}
    	FibonacciHeap d= new FibonacciHeap();
//    	for(int i=1000;i>0;i--) {
//    		for(int j1=0;j1<5;j1++) {
//    			y=d.insert(i);
//    			i--;
//    			j[j1]=y;
//    			
//    		}
//    		System.out.println("min is:"+i);
//    		if(d.findMin().key!=i+1) {
//    			System.out.println("notTrue1");
//    			break;}
//    		d.decreaseKey(d.Min, 5);
//    		System.out.println(d.Min.key);
//    		if(d.findMin().key!=i-4) {
//    			System.out.println("notTrue2");
//    			break;}
//    		d.deleteMin();
//    		if(d.findMin().key!=i+2) {
//    			System.out.println("notTrue3");
//    			break;}
////    		System.out.println(j[3].key);
//    		d.decreaseKey(j[3], 10);
////    		System.out.println(j[3].key);
//    		if(d.findMin().key!=i-8) {
//    			System.out.println("notTrue4");
//    			break;}
//    		d.deleteMin();
//    		System.out.println(d.Min.key);
//    		if(d.findMin().key!=i+3) {
//    			System.out.println("notTrue5");
//    			break;}
//    		d.delete(j[1]);
//    		System.out.println(d.size);
//    		if(d.size!=2*r) {
//    			System.out.println("notTrue6");
//    			break;
//    		}
//    		r++;
//    	}
//    	System.out.println("welldone");
    	d.Q2();
    	d.Q1();

	}
    public void Q2() {
    	int[] a=new int[3];
    	a[0]=1000;
    	a[1]=2000;
    	a[2]=3000;
    	for(int i=0;i<3;i++) {
    		FibonacciHeap h =new FibonacciHeap();
    		System.out.println("m="+a[i]);
    		//System.out.println("to:"+FibonacciHeap.TotalLinks);
    		for(int j=a[i];j>0;j--) {
    			h.insert(j);
    	}
    		for (int j=0;j<a[i]/2;j++) {
    			h.deleteMin();
    		}
    		System.out.println("totalLinks is:"+h.TotalLinks);
    		System.out.println("totalCuts is:"+h.TotalCut);
    		System.out.println("Potential is:"+h.potential());
    	}
    }
    public void Q1() {
    	int[] a=new int[3];
    	a[0]=1024;
    	a[1]=2048;
    	a[2]=4096;
    	for(int i=0;i<3;i++) {
    		FibonacciHeap h =new FibonacciHeap();
    		FibonacciHeap.HeapNode[] nodes=new HeapNode[a[i]+1];
    		System.out.println("m="+a[i]);
    		for(int j=a[i];j>0;j--) {
    			nodes[j]=null;
    	}
    		for(int j=a[i];j>0;j--) {
    			nodes[j]=h.insert(j);
    	}
    		FibonacciHeap.HeapNode y=h.findMin();
    		h.deleteMin();
    		int z=a[i];
    		for (int j=0;j<Math.log(a[i])-2;j++) {
    			int r= (int)(Math.random() * (nodes.length-1 ));
    			while(nodes[r].equals(y)) {
    				r= (int)(Math.random() * (nodes.length-1 ));
    			}
    			z=(int) (z+Math.pow(0.5, j));
    			h.decreaseKey(nodes[r], z);
    		}
    		int r= (int)(Math.random() * (nodes.length-1 ));
    		h.decreaseKey(nodes[r], a[i]-1);
    		System.out.println("totalLinks is:"+h.TotalLinks);
    		System.out.println("totalCuts is:"+h.TotalCut);
    		System.out.println("Potential is:"+h.potential());
    	}
    }

}
