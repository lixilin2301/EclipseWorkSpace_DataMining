import java.util.List;
import java.util.Set;
import java.util.Iterator;


public class main {
	
	public static void exercise1_1() {
		//ShingleString Test
		
		ShingleSet test1 = new ShingleSet(2);
		test1.shingleString("abcdabd");
		
		
		Iterator i = test1.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
		
		//Jaccard Test
		
//		ShingleSet test2 = new ShingleSet(2);
//		test2.shingleString("absadasdasdasd");
//		System.out.printf("%f", test1.jaccardDistance(test2));
		
		//Exercise 1.1
		ShingleSet s1 = new ShingleSet(5);
		s1.shingleString("The plane was ready for touch down");
		ShingleSet s2 = new ShingleSet(5);
		s2.shingleString("The quarterback scored a touchdown");
		System.out.println(s1.jaccardDistance(s2));
		
		ShingleSet s3 = new ShingleSet(5);
		s3.shingleStrippedString("The plane was ready for touch down");
		ShingleSet s4 = new ShingleSet(5);
		s4.shingleStrippedString("The quarterback scored a touchdown");
		System.out.println(s3.jaccardDistance(s4));
		
		
	}
	
	public static void exercise1_2() {
		
		ShingleSet s1 = new ShingleSet(1);
		ShingleSet s2 = new ShingleSet(1);
		ShingleSet s3 = new ShingleSet(1);
		ShingleSet s4 = new ShingleSet(1);
		s1.shingleString("ad");
		s2.shingleString("c");
		s3.shingleString("bde");
		s4.shingleString("acd");
		
		MinHash mh = new MinHash();
//		mh.addHashFunction(new HashFunction(1,1));
//		mh.addHashFunction(new HashFunction(3,1));
		mh.addRandomHashFunctions(100);
		mh.addSet(s1);
		mh.addSet(s2);
		mh.addSet(s3);
		mh.addSet(s4);
		System.out.println(mh.computeSignature().toString());
		exercise1_3(mh);
	}
	
	public static void exercise1_3(MinHash mh) {
		// ADD CODE HERE
		LSH lsh = new LSH();
//		System.out.println(lsh.computeCandidates(mh.computeSignature(), 1000, 1).toString());
		System.out.println(lsh.computeCandidates(mh.computeSignature(), 1000, 5).toString());
//		System.out.println(lsh.computeCandidates(mh.computeSignature(), 1000, mh.computeSignature().rows()).toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// exercise 1.1
		exercise1_1();
		
		// exercise 1.2
		exercise1_2();
	}

}
