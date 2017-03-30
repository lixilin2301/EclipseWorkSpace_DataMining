import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Util {
	// Util class implements a number of basic operations
	
	public static Map<Integer,Map<Integer,Double>> initializeLatentFactor(int nK, int nF) {
		// Initialize factors with randomly generated numbers
		Map<Integer,Map<Integer,Double>> F = new HashMap<>();
		
		// Random number generator (uniform)
		Random randomInteger = new Random();
		
		// Initialize a vector and add to map
		for (int k=0; k<nK; k++) {
			Map<Integer,Double> A = new HashMap<Integer,Double>();
			for (int f=0; f<nF; f++) {
				A.put(f, randomInteger.nextDouble()-0.5);  
			}
			F.put(k, A);
		}
		return F;
	}
	
	public static double calculateAverage(Map<Integer,Double> a) {
		// Compute average
		double sum = 0.0;
		for (double value : a.values()) {
			sum += value;
		}
		return sum/a.size();
	}
	
	public static double innerProduct(Map<Integer,Double> a, Map<Integer,Double> b) {
		// Compute sum of the product of each element of two vectors
		double ab = 0.0;
		for (int f=0; f<b.size(); f++) {
			ab += a.get(f)*b.get(f);
		}
		return ab;
	}
	
	public static double rootMeanSquaredError(ArrayList<User> Ru, 
			Map<Integer,Map<Integer,Double>> Q, Map<Integer,Map<Integer,Double>> P) {
		// Compute the square root of the mean of squared errors
		double norm = 0.0;
		double rmse = 0.0;
		for (int u=0; u<Ru.size(); u++) {
			for (int m : Ru.get(u).getRatings().keySet()) {
				// Compute squared difference between true and predicted rating 
				rmse += Math.pow(Ru.get(u).getRatings().get(m) - innerProduct(Q.get(u),P.get(m)),2);
				norm += 1;
			}
		}
		return Math.sqrt(rmse/norm);
	}
	
	public static void subtractRating(Map<Integer,Double> rating, double avg){
		// Subtract average from each rating
		for(Integer i : rating.keySet()){
			rating.put(i, rating.get(i)-avg);
		}
	}
	
	public static double updateQuf(ArrayList<User> Ru, Map<Integer,Map<Integer,Double>> Q, 
			Map<Integer,Map<Integer,Double>> P, int u, int f, int nF, double lambdaQ) {
		// Compute update in Q for user u and factor f

		int movie_total_num = P.size();
		// Denominator and numerator for updated Q
		double updated_Quf_deno = 0.0;
		double updated_Quf_nume = 0.0;
		for(int i=0;i<movie_total_num;i++){
			// Check if user rates movie
			if(Ru.get(u).getRatings().containsKey(i)){
				// Compute the derivative for the element
				updated_Quf_deno += Math.pow( P.get(i).get(f), 2);
				double Ci = innerProduct(P.get(i),Q.get(u)) - Q.get(u).get(f)*P.get(i).get(f);
				updated_Quf_nume += P.get(i).get(f)*(Ru.get(u).getRatings().get(i)-Ci);
			}
		}
		// Add regularization (lambda) when computing update to avoid overfitting
		double updated_Quf = updated_Quf_nume/(updated_Quf_deno + lambdaQ);	
		return updated_Quf;
	}
	
	public static double updatePmf(ArrayList<Movie> Rm, Map<Integer,Map<Integer,Double>> Q, 
			Map<Integer,Map<Integer,Double>> P, int m, int f, int nF, double lambdaP) {
		// Compute update in P for movie m and factor f
		int user_total_num = Q.size();
		// Denominator and numerator for updated P
		double updated_Pmf_deno = 0.0;
		double updated_Pmf_nume = 0.0;		
		for(int i=0;i<user_total_num;i++){
			// Check if none
			if(Rm.get(m).getRatings().containsKey(i)){
				// Compute the derivative for the element
				updated_Pmf_deno += Math.pow( Q.get(i).get(f), 2);
				double Ci = innerProduct(Q.get(i),P.get(m)) - Q.get(i).get(f)*P.get(m).get(f);
				updated_Pmf_nume += Q.get(i).get(f)*(Rm.get(m).getRatings().get(i)-Ci);
			}
		}
		// Add regularization (lambda) when computing update to avoid overfitting
		double update_Pmf = updated_Pmf_nume/(updated_Pmf_deno + lambdaP);
		return update_Pmf;
	}	
}
