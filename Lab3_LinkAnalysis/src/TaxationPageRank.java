import java.util.HashMap;
import java.util.Map;


public class TaxationPageRank extends PageRank {
	/**
	 * The taxation value.
	 */
	double beta;

	/**
	 * Constructor.
	 * @param b The beta parameter for the taxation version of PageRank.
	 */
	public TaxationPageRank(double b) {
		super();

		beta = b;
	}

	/**
	 * Calculates the PageRank using the taxation method.
	 */
	@Override
	public Map<String, Double> calculatePageRank(int iterations) {
		// the result
		HashMap<String, Double> result = new HashMap<String, Double>();

		// the tools
		Matrix randomSurfer = null;
		Matrix transitionMatrix = null;
		Matrix eleVector;
		
        // FILL IN YOUR CODE HERE
		transitionMatrix = this.constructTransitionMatrix();
		randomSurfer = this.getRandomSurferVector();
		eleVector  = this.getEleVector();
//		System.out.println(eleVector);
		for (int i = 0; i < iterations; i++) {
			randomSurfer = ( (transitionMatrix.multiply(beta)).dot(randomSurfer) ).add( eleVector.multiply((1-beta)/data.size()) ) ;
		}
		
		// fill the results, match names with PageRank values
		int count = 0;
		for (String s: data.keySet()) {
			result.put(s, randomSurfer.get(count));
			count++;
		}

		return result;
	}
	public Matrix getEleVector() {
		Matrix result = new Matrix(data.size(), 1);
		for (int r = 0; r < data.size(); r++) {
			result.set(r, 0, 1);
		}
		return result;
	}
}
