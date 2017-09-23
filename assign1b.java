import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



class assign1b{

	public static class hashed implements Comparable<hashed>{
		private int size;
		public String file_name;
		public int rel_dist;
		public int arr[];
		public Vector< Vector < Integer > > v;  
		public hashed(int n){
			this.size=n;
			this.file_name="Initialvalue";
			this.rel_dist=-1;
			this.arr=new int[n];
			this.v=new Vector<Vector<Integer>>();
			for(int i=0;i<n;i++){
				v.addElement(new Vector<Integer>());
			}
		}
		public int getrelativedistance(){
			return rel_dist;
		}

		public int getsize(){
			return size;
		}


		@Override
		public int compareTo(hashed o){

			if(this.getrelativedistance()>o.getrelativedistance()){
				return 1;
			}
			else if(this.getrelativedistance()<o.getrelativedistance()){
				return -1;
			}
			else {
				if(this.getrelativedistance()!=-1){
					for(int i=0;i<this.getsize();i++){
						if(this.arr[i]>o.arr[i]){
							return 1;
						}
						else if(this.arr[i]<o.arr[i]){
							return -1;
						}
					}
				}
				return 0;
			}
		}
		

	}


	public class MergeSort extends RecursiveTask< hashed[] > {

	private hashed[] obj;

	public MergeSort( hashed[] obj) {
		
		this.obj = obj;
	}
	

	@Override
	protected hashed[] compute(){

		if (obj.length == 1)
			return obj;
		
		int n1=obj.length/2;
		int n2=obj.length - (obj.length/2);

		hashed[] left=new hashed[n1];
		for(int i=0;i<obj.length/2;i++){
			left[i]=obj[i];
		}

		hashed[] right=new hashed[n2];
		for(int i=obj.length/2;i<obj.length;i++){
			right[i-(obj.length/2)]=obj[i];
		}



		MergeSort firstSubthread = new MergeSort(left), secondSubthread = new MergeSort(right);

		invokeAll(firstSubthread, secondSubthread);

		hashed[] leftres=firstSubthread.join(),rightres=secondSubthread.join();

		return merge(leftres, rightres);
	}


	private hashed[] merge(hashed[] left, hashed[] right) {
		

		Integer i = 0, j = 0, k = 0;

		hashed[] res = new hashed[left.length + right.length];
		

	    while ((i < left.length) && (j < right.length)) {
		    if (left[i].rel_dist < right[j].rel_dist)
	        	{
	        		res[k++] = left[i++];
	        	}
	      	else if(left[i].rel_dist> right[j].rel_dist)
	    	    {
	    	    	res[k++] = right[j++];
	    	    }
	    	else{
	    		if(left[i].rel_dist!=-1)
	    		{
	    			for(int l=0;l<left[i].getsize();l++){
						if(left[i].arr[l]<right[j].arr[l]){
							res[k++]=left[i++];
							break;
						}
						else if(left[i].arr[l]>right[j].arr[l]){
							res[k++]=right[j++];
							break;
						}
						else if(left[i].arr[l]==right[j].arr[l] && l+1==left[i].getsize()){
							//System.out.println(left[i].getsize());
							res[k++]=left[i++];
							break;
							
						}
					}
	    		}
	    		else{
		    			res[k++]=left[i++];
		    	}

	    	}
	    }

	    while(i<left.length){
	    	res[k++]=left[i++];
	    }
	    while(j<right.length){
	    	res[k++]=right[j++];
	    }

	   	return res;
	}

}




	public int find_the_least_relative(String filename,String[] str,hashed o){
		int n=str.length,low,high,lowold,flag=1;
		int mini=o.rel_dist;
		for(int i=0;i<o.v.get(0).size();i++){
			for(int j=0;j<o.v.get(n-1).size();j++){

				if(o.v.get(0).get(i)<o.v.get(n-1).get(j)){
					low=o.v.get(0).get(i);high=o.v.get(n-1).get(j);lowold=o.v.get(0).get(i);
					Vector<Integer> source= new Vector<Integer>();
					source.add(low);

					for(int k=1;k<n-1;k++){
						lowold=low;
						int h;
						for(h=0;h<o.v.get(k).size();h++){
							if(o.v.get(k).get(h)>low && o.v.get(k).get(h)<high){
								low=o.v.get(k).get(h);
								source.add(low);
								break;
							}

						}

						if(h==o.v.get(k).size()){

							flag=0;break;
						}
						
						
					}
					source.add(high);
					//System.out.println(mini+" "+o.v.get(n-1).get(j)+" "+o.v.get(0).get(i));
					if(flag==1){
						if(mini>(o.v.get(n-1).get(j)-o.v.get(0).get(i)))
							{
								mini=(o.v.get(n-1).get(j)-o.v.get(0).get(i));

								for(int p=0;p<n;p++){
									o.arr[p]=source.get(p);
									//System.out.print(source.get(p)+" ");
									//System.out.println();
							}
						}
							//System.out.println();
					}
				}
			}
		}
		
		return mini;

	}





	public int min_relative(String filename,String[] str,hashed o){
		

		for(int i=0;i<str.length;i++)
		{
			int prev=0,sum=0;
			
		try{
    	BufferedReader br = new BufferedReader(new FileReader(filename));
   	 	String line = null;
   	 	int k=0,lineno=1;
    	while( (line = br.readLine())!= null ){
	        // \\s+ means any number of whitespaces between tokens
	        String [] tokens = line.split("\\s+");
	        	int n=tokens.length;
	        	while(k<n)
	        	{
	        		
	        		if(str[i].equals(tokens[k]))
	        		{
	        
	        			o.v.get(i).add(k+lineno);
	        		}

	        		k++;
	        	}
	        	lineno=lineno+k;
	        	k=0;

    	}

    	}
    	catch(Exception e){
    		System.out.println("Problem with the opening files");
    	}


    }

    	return find_the_least_relative(filename,str,o);
    	

	}
	





	public void ReadInput(String filename,String[] str,hashed o){
		
		o.file_name=filename;
		int i=0,prev=0,sum=0;
		try{
    	BufferedReader br = new BufferedReader(new FileReader(filename));
   	 	String line = null;
   	 	int k=0,lineno=1;
    	while( (line = br.readLine())!= null ){
	        // \\s+ means any number of whitespaces between tokens
	        String [] tokens = line.split("\\s+");
	        	int n=tokens.length;
	        	while(k<n)
	        	{
	        		//System.out.println(tokens[k]);
	        		if(str[i].equals(tokens[k]))
	        		{
	        			o.arr[i]=k+lineno;
	        			//System.out.println(k+lineno);
	        			if(str.length>1)
	        			{
		        			if(i==0)prev=k+lineno;
		        			sum=sum+(k+lineno-prev);
		        			prev=k+lineno;
		        			i++;
		        			if(i==str.length)
		        			{
		        				o.rel_dist=sum;
		        				int b=min_relative(filename,str,o);
		        				//System.out.println("before going least is"+o.rel_dist);
		        				if(b < o.rel_dist){
		        					o.rel_dist=b;	
		        				}
		        				//System.out.println("After calculating least is"+min_relative(filename,str,o));
		        				return;
	        				}
	        			}
	        			else if(str.length==1)
	        			{
	        				
	        				sum=k+lineno;
	        				o.rel_dist=sum;
	        				return;
	        			}
	        		}

	        		k++;
	        	}
	        	lineno=lineno+k;
	        	k=0;

    	}

    	}
    	catch(Exception e){
    		System.out.println("Problem with the opening files");
    	}


    	return;
	}




	public class MyThread implements Runnable {

		private String filename;
		private String[] str;
		public hashed o;

	   	public MyThread(String filename,String[] str,hashed o) {
	     
	   		this.filename=filename;
	   		this.str=str;
	   		this.o=o;
	   	}

	   	public void run() {

	   		ReadInput(this.filename,this.str,this.o);
	   		
	   	}

	}


	public static void main(String args[]){


		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the string to be searched");
		String str1=sc.nextLine();
		System.out.println("Enter the number of files");
		int N=sc.nextInt();

		String[] str=str1.split("\\s+");

		String[] filename=new String[N];

		hashed[] o = new hashed[N];
		for(int i=0;i<N;i++){
			o[i]=new hashed(str.length);
		}
		
		for(int i=0;i<N;i++){
			System.out.println("Enter the name of the "+(i+1)+" file");
			filename[i]=sc.next();
		}


		ExecutorService executor = Executors.newFixedThreadPool(N);
		assign1b obj=new assign1b();
		for(int i=0;i<N;i++){
			Runnable worker = obj.new MyThread(filename[i],str,o[i]);
			executor.execute(worker);
		}

		executor.shutdown();

		while (!executor.isTerminated()) {
 			
		}

		assign1b ob=new assign1b();
  		MergeSort object=ob.new MergeSort(o);
  		hashed[] final_obj=object.compute();


		try{

		    PrintWriter writer = new PrintWriter("OUTPUT.txt", "UTF-8");
		    writer.println(str1+" is found in");
		    for(hashed temp: final_obj){
		    	if(temp.getrelativedistance()!=-1)
		    	{
		    		String res=temp.file_name+":";
		    		for(int i=0;i<str.length;i++){
		    			res=res+str[i]+" is found as "+temp.arr[i]+"th word,";
		    		}
		    		writer.println(res);
		    	}
			}

		    writer.close();

		    System.out.println("OUTPUT.txt named file is generated in the current directory.");
		} catch (IOException e) {
		   System.out.println("System I/O error");
		}
	}
		
	
}