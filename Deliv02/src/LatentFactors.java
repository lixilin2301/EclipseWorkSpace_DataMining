/* TU Delft
 * BSc Computer Science
 * TI2735-C Data Mining 2016-2017
 * Project Deliverable 02: Latent Factors
 */

import java.lang.System;
import java.util.Map;
import java.util.Random;

public class LatentFactors {
	
	public static void main(String[] args) {
		
		// Read user list
		UserList userList = new UserList();
		userList.readFile("data/users.csv");
		
		// Read movie list
		MovieList movieList = new MovieList();
		movieList.readFile("data/movies.csv");
		
		// Read rating list
		RatingList ratings = new RatingList();
		ratings.readFile("data/ratings.csv", userList, movieList);	

		// Make predictions file
		RatingList predRatings = new RatingList();
		predRatings.readFile("data/predictions.csv", userList, movieList);

		// Add ratings to user and movie lists
		userList.addRatings(ratings);
		movieList.addRatings(ratings);

		// Perform rating predictions
		predictRatings(userList, movieList, ratings, predRatings);

		// Write result file
		predRatings.writeResultsFile("submission.csv");
	}
	
	public static RatingList predictRatings(UserList userList,
			MovieList movieList, RatingList ratingList, RatingList predRatings) {

		// Number of users and movies
		int nU = userList.size();
		int nM = movieList.size();
		
		// Normalize ratings for every user separately
		// Xilin Code
		double[] avg_user = new double[nU]; 
		for(int i=0;i<nU;i++){
			Map<Integer, Double> rating = userList.get(i).getRatings();
			// Compute average and subtract average from each rating
			avg_user[i] = Util.calculateAverage(rating);
			Util.subtractRating(rating,avg_user[i]);
		}
		// Modify the ratings in Movie list also
		for(int i=0;i<nM;i++){
			Map<Integer, Double> rating = movieList.get(i).getRatings();
			// Get rating from normalized User list 
			for(Integer j:rating.keySet()){
				rating.put(j, userList.get(j).getRatings().get(i));
			}
		}
		
		// Number of factors
		int nF = 5;
		
		// Regularization parameters
		double lambdaQ = 1.0;
		double lambdaP = 1.0;
		
		// Optimization parameters
		double xTolerance = 0.0001;
		int maxIterations = 20;
		double RMSE_ = Double.POSITIVE_INFINITY;
		double RMSE_history = 0.0;
		// Initialize random number generator
		Random rng = new Random();
		
		// Initialize factors
		Map<Integer,Map<Integer,Double>> Q = Util.initializeLatentFactor(nU,nF);
		Map<Integer,Map<Integer,Double>> P = Util.initializeLatentFactor(nM,nF);
				
		// Perform optimization using Gradient Descent
		// Xilin Code	
		for(int iter=0;iter<maxIterations && Math.abs(RMSE_history-RMSE_)>xTolerance;iter++){			
			// Update Q matrix Row by row
			for(int i=0;i<nU;i++){
				for(int j=0;j<nF;j++){
					Q.get(i).put(j,Util.updateQuf(userList, Q, P, i, j, nF, lambdaQ));
				}
			}
			// Update P matrix Row by row
			for(int i=0;i<nM;i++){
				for(int j=0;j<nF;j++){
					P.get(i).put(j, Util.updatePmf(movieList, Q, P, i, j, nF, lambdaP));
				}
			}
			// Record RMSE history and compute new RMSE
			RMSE_history = RMSE_;
			RMSE_ = Util.rootMeanSquaredError(userList, Q, P);
			System.out.println(iter+": "+RMSE_);
		}
		// Loop over to-be-predicted ratings
		System.out.print("Running predictions..");
		for (int i=0; i<predRatings.size(); i++) {
			// Compute prediction
			int pred_user_no = predRatings.get(i).getUser().getIndex()-1;
			int pred_movie_no = predRatings.get(i).getMovie().getIndex()-1;
			double prediction = Util.innerProduct(Q.get(pred_user_no), P.get(pred_movie_no)) + avg_user[pred_user_no];
			// Check if the prediction is special 
			if(prediction<0.0)prediction=1.0;
			if(prediction>5.0)prediction=5.0;
			if(Double.isNaN(prediction))prediction=3.0;
			// Set predicted rating
			predRatings.get(i).setRating(prediction);
		}
		System.out.print("done.");

		// Return predictions
		return predRatings;
	}
}
