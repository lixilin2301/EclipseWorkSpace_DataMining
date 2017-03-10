/* TU Delft
 * BSc Computer Science
 * TI2735-C Data Mining 2016-2017
 * Project Deliverable 01: Collaborative Filtering
 */

import java.lang.System;
import java.util.Arrays;
import java.util.Collections;

public class lalala {

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
		for (int i=7; i<8; i++) {
			 
			// Inform progress
			if (i%1000==0) {
				System.out.println("Running predictions "+(i+1)+"/"+predRatings.size());
			}
			
			// Compute similarity with other users (tip: cosine similarity)
			double[] similarity = new double[nU];
//			for (int k = 0; k < nU; k++) {
//				similarity[k] = 0;
//			}
			int predUserIndex = predRatings.get(i).getUser().getIndex()-1;	// Index of current user
			int predMovieIndex = predRatings.get(i).getMovie().getIndex()-1;	// Index of current movie
			for (int j=0; j < nU && j != predUserIndex; j++) {
				similarity[j] = Util.calculateCosine(userList.get(predUserIndex).getRatings(), userList.get(j).getRatings(), nM);
				System.out.println(similarity[j]);
			}
				
			// Hard-code size of neighborhood
			int N = 25;
			Sorter ss = new Sorter(similarity);
			Integer[] userIndexList = ss.createIndexArray();
			Arrays.sort(userIndexList, ss.reversed());
			
			// Construct weighted average
			//..CODE HERE..
			double ws = 0.0;
			double sum = 0.0;
			int userIndex = 0;
			double tmp1 = 0.0;
			double tmp2 = 0.0;
			int countInvalid = 0;
			for (int j = 0; j < N; j++) {
				userIndex = userIndexList[j];
//				System.out.println("User: "+userIndex+"; score:"+userList.get(userIndex).getRatings().get(predMovieIndex));
				if (userList.get(userIndex).getRatings().get(predMovieIndex) == null) {
					ws += 0;
					sum += 0;
					countInvalid++;
				}
				else {
					ws += userList.get(userIndex).getRatings().get(predMovieIndex) * similarity[userIndex];
					sum += similarity[userIndex];
					tmp1 = userList.get(userIndex).getRatings().get(predMovieIndex) * similarity[userIndex];
					tmp2 = similarity[userIndex];
				}
				
			}
			double prediction = 0.0;
			if (countInvalid == N ) {
				prediction = 3.0;
//				System.out.println("no invalid");
			}
			else {
				if (sum == 0) {
					ws -= tmp1;
					sum -= tmp2;
				}
				prediction = ws / sum;
			}	
			System.out.println(prediction);
			
			// Set predicted rating
			predRatings.get(i).setRating(prediction);
//			for (int k = 0; k < nU; k++) {
//				similarity[k] = 0;
//			}
			
		}
		System.out.print("done.");
		
		// Return predictions
		return predRatings;
	}
}