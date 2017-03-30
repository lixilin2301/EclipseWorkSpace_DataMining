import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PCY extends APriori {

	private int bucketSize;

	private List<Integer> buckets = null;

	/**
	 * Constructor of the PCY algorithm class.
	 * @param s The support threshold.
	 * @param bs The bucket size.
	 */
	public PCY(int s, int bs) {
		super(s);

		bucketSize = bs;
	}

	/**
	 * Constructs candidates based on the previous set of frequent itemsets (L_k-1)
	 * @param filteredCandidates The set of frequent k-1 itemsets
	 * @return A set of candidate itemsets of size k
	 */
	@Override
	public Set<StringSet> constructCandidates(Set<StringSet> filteredCandidates, int k) {
		// PCY only acts on the frequent pairs
		if (k != 2)
			return super.constructCandidates(filteredCandidates, k);
		// the result
		Set<StringSet> candidates = new HashSet<StringSet>();

		if (filteredCandidates == null) {
			// add all initial words to the items set
			for (Set<String> basket: baskets) {
				for (String s: basket) {
					StringSet sl = new StringSet();
					sl.add(s);
					candidates.add(sl);
				}
			}
		} else {
			// add code here
			StringSet[] fC_array = filteredCandidates.toArray(new StringSet[filteredCandidates.size()]);
			for(int i=0;i<(fC_array.length-1);i++){
				for(int j=i+1;j<fC_array.length;j++){
					StringSet union = new StringSet(fC_array[i]);
					union.addAll(fC_array[j]);	
					int hash = (union.hashCode()%bucketSize+bucketSize)%bucketSize;
					// Modification -------------------------------------------------- 
					if(union.size()==k && buckets.get(hash)>=supportThreshold)
						candidates.add(union);				
				}
			}
		}

		return candidates;
	}

	/**
	 * Calculates the support value of each set in candidates.
	 * @param candidates The set of candidate sets.
	 * @param k The size of the candidates.
	 * @return A mapping of sets to support value.
	 */
	@Override
	public Map<StringSet, Integer> countCandidates(Set<StringSet> candidates, int k) {
		// PCY only acts on the frequent pairs
		if (k != 1)
			return super.countCandidates(candidates, k);
		
		// initialize the buckets
		buckets = new ArrayList<Integer>(bucketSize);
		for (int i = 0; i < bucketSize; i++)
			buckets.add(0);

		// the result
		Map<StringSet, Integer> candidatesCount = new HashMap<StringSet, Integer>();

		// add code here
		for(Set<String> basket: baskets){
			//Modification ----------------------------------------------start
			Set<StringSet> subsets_1 = getSubsets(basket,k);
			Set<StringSet> subsets_2 = getSubsets(basket,k+1);
			for(StringSet ss : subsets_2){
				int hash = (ss.hashCode()%bucketSize+bucketSize)%bucketSize;
				buckets.set(hash,buckets.get(hash)+1);
			}
			//Modification ----------------------------------------------end
			for(StringSet ss : subsets_1){
				if(candidates.contains(ss)){
					if(!candidatesCount.containsKey(ss))
						candidatesCount.put(ss, 1);
					else
						candidatesCount.put(ss, candidatesCount.get(ss)+1);
				}
			}
		}
		return candidatesCount;
	}

}
