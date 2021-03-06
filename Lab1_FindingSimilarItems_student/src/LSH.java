import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author hansgaiser
 *
 */
public class LSH {
	
	/**
	 * Computes the candidate pairs using the LSH technique.
	 * @param mhs The minhash signature object.
	 * @param bs The number of buckets to be used in the LSH table.
	 * @param r The number of rows per band to be used.
	 * @return Returns a set of indices pairs that are candidate to being similar.
	 */
	public static Set<List<Integer>> computeCandidates(MinHashSignature mhs, int bs, int r) {
		// assert that the number of rows can be evenly divided by r
		assert(mhs.rows() % r == 0);
		
		// the number of bands
		int b = mhs.rows() / r;
		
		// the result
		Set<List<Integer>> candidates = new HashSet<List<Integer>>();
		
		List<List<List<Integer>>> buckets = new ArrayList<List<List<Integer>>>();//projection
		for(int i=0;i<bs;i++){
			buckets.add(new ArrayList<List<Integer>>());
			for (int j=0;j<b;j++){
				buckets.get(i).add(new ArrayList<Integer>());
			}
		}
		for(int bi=0;bi<b;bi++){
			int row_start = bi*r;
			int row_end = (bi+1)*r;
			for(int col=0;col<mhs.cols();col++){
				String s = mhs.colSegment(col, row_start, row_end);
//				System.out.println((s.hashCode()%bs+bs)%bs);
				buckets.get((s.hashCode()%bs+bs)%bs).get(bi).add(col);
			}
		}
		for(int bi=0;bi<b;bi++){
			int row_start = bi*r;
			int row_end = (bi+1)*r;
			for(int col=0;col<mhs.cols();col++){
				String s = mhs.colSegment(col, row_start, row_end);
				List<Integer> bucket = buckets.get((s.hashCode()%bs+bs)%bs).get(bi);
				
				for(Integer candidate1 : bucket){
					for(Integer candidate2 : bucket){
						if(!candidate1.equals(candidate2)){
							ArrayList<Integer> a = new ArrayList<Integer>();
							a.add(candidate1);
							a.add(candidate2);
							Collections.sort(a);
							if(a.size()==2&&!candidates.contains(a)){
								candidates.add(a);						
							}
						}
					}
				}
				
			}
		}
		
		return candidates;
	}
	
}
