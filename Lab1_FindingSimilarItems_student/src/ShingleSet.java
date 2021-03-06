import java.util.SortedSet;
import java.util.TreeSet;


/**
 * The ShingleSet class contains a sorted set of shingles.
 * @author hansgaiser
 *
 */
public class ShingleSet extends TreeSet<String> implements SortedSet<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2143400328259342668L;
	
	/**
	 * The size of the shingles.
	 */
	private int k;
	
	/**
	 * Constructor for the ShingleSet.
	 * @param k The size of the shingles.
	 */
	public ShingleSet(int k) {
		this.k = k;
	}
	
	/**
	 * Add shingles of size k to the set from String s.
	 * @param s The string that is to be transformed to shingles.
	 */
	public void shingleString(String s) {
		int len = s.length();
		if(len<this.k){
			System.out.println("Error! k>word length");
		}else{
			for(int i=0;i<(len-this.k+1);i++){
				String shingle = s.substring(i, i+this.k);
				if(!this.contains(shingle)){
					this.add(shingle);
				}			
			}
		}
//		System.out.println(this.toString());
	}
	
	/**
	 * Add shingles of size k to the set from String s, where all white spaces from s are removed.
	 * @param s The string that is to be transformed to shingles.
	 */
	public void shingleStrippedString(String s) {
		
		String s_stripped = s.replaceAll("\\s","");
//		System.out.println(s_stripped);
		this.shingleString(s_stripped);
	}
	
	/**
	 * Calculates the Jaccard distance between this set and the other set.
	 * @param other The other set of shingles that this set will be compared to.
	 * @return The Jaccard distance between this set and the other set.
	 */
	public double jaccardDistance(TreeSet<String> other) {
		if(other.isEmpty()||this.isEmpty()||this.first().length()!=other.first().length()){
			return 0;
		}
		ShingleSet intersection = new ShingleSet(other.first().length());
		ShingleSet union = new ShingleSet(other.first().length());
		intersection.addAll(other);
		union.addAll(other);
//		System.out.println(this.toString()+other.toString());
		intersection.retainAll(this);
		union.addAll(this);
//		System.out.println(intersection.toString()+union.toString());
//		System.out.println(intersection.size()+"  "+union.size());		
		return 1 - (double) intersection.size()/ (double) union.size(); 
	}

}
