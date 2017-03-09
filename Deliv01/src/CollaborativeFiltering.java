/* TU Delft
 * BSc Computer Science
 * TI2735-C Data Mining 2016-2017
 * Project Deliverable 01: Collaborative Filtering
 */

import java.lang.System;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class CollaborativeFiltering {

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
			
		// Loop over to-be-predicted ratings
		System.out.println("Running predictions..");
		for (int i=0; i<predRatings.size(); i++) {
			 
			// Inform progress
			if (i%1000==0) {
				System.out.println("Running predictions "+(i+1)+"/"+predRatings.size());
			}
			
			double[] sim = new double[nU];
			
			
			// Compute similarity with other users (tip: cosine similarity)
			HashMap<Integer, Double> preuser_rating = predRatings.get(i).getUser().getRatings();
			for(int j=0;j<nU;j++){				
				if(j!=i){
					sim[j] = Util.calculateCosine(preuser_rating, userList.get(j).getRatings(), nM);
//					if(j==3597){
//						System.out.println(Util.euclideanNorm(preuser_rating)*Util.euclideanNorm(userList.get(j).getRatings()));
//						System.out.println(Util.innerProduct(preuser_rating, userList.get(j).getRatings()));
//						System.out.println(Util.innerProduct(preuser_rating, userList.get(j).getRatings())/( Util.euclideanNorm(preuser_rating)*Util.euclideanNorm(userList.get(j).getRatings()) ));
//					}
				}else{
					sim[j] = Double.MIN_VALUE;
				}
//				System.out.println("sim"+j+": "+sim[j]);
			}
			
			// Hard-code size of neighborhood
			int N = 25;
			
			// Construct weighted average
			Sorter sorter = new Sorter(sim);
			Integer[] user_index_list = sorter.createIndexArray(); //0 based
//			for(int j=0;j<N;j++){
//				System.out.println("sim"+j+": "+sim[j]);
//			}
			Arrays.sort(user_index_list, sorter.reversed());
			System.out.println("After!");
//			for(int j=0;j<N;j++){
//				System.out.println("sim"+user_index_list[j]+": "+sim[user_index_list[j]]);
//			}
//			System.out.println();
			System.out.println("sim[3597]"+": "+sim[3597]+"euc: " + Util.calculateAverage(userList.get(3597).getRatings()));
//			System.out.println(userList.get(3597).getRatings().toString());
			
			double prediction_nume=0;
			double prediction_deno=0;
			int premovie_index = predRatings.get(i).getMovie().getIndex();
			for(int j=0;j<N;j++){
				if(userList.get(user_index_list[j]).getRatings().containsKey(premovie_index)){
					prediction_nume += sim[user_index_list[j]] * userList.get(user_index_list[j]).getRatings().get(premovie_index) ;
				}				
				prediction_deno += sim[user_index_list[j]] ;
			}
			
			double prediction = prediction_nume/prediction_deno;
			System.out.println("rst"+i+" = "+prediction);
			// Set predicted rating
			if(prediction<0.0)prediction=0.0;
			if(prediction<5.0)prediction=5.0;
			predRatings.get(i).setRating(prediction);
			
		}
		System.out.print("done.");
		
		// Return predictions
		return predRatings;
	}
}