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
		
			
		// Loop over to-be-predicted ratings predRatings.size()
		System.out.println("Running predictions..");
		for (int i=0; i< predRatings.size(); i++) {
			 
			// Inform progress
			if (i%1000==0) {
				System.out.println("Running predictions "+(i+1)+"/"+predRatings.size());
			}
			
			
			// Compute similarity with other users (tip: cosine similarity)
			int premovie_index = predRatings.get(i).getMovie().getIndex();
			int preuser_index = predRatings.get(i).getUser().getIndex();
			
			double[] sim = new double[nU];
			HashMap<Integer, Double> preuser_rating = predRatings.get(i).getUser().getRatings();			
			for(int j=0;j<nU;j++){				
				if(j!=(predRatings.get(i).getUser().getIndex()-1) && userList.get(j).getRatings().containsKey(premovie_index)){
//					System.out.println("Movie: "+ premovie_index+"; user:"+ j +";score:"+userList.get(j).getRatings().get(premovie_index));
					sim[j] = Util.calculateCosine(preuser_rating, userList.get(j).getRatings(), nM);
//					sim[j] = Util.calculatePearson(preuser_rating, userList.get(j).getRatings(), nM);
//					System.out.println("haha");
//					System.out.println(sim[j]);
				}else{
					sim[j] = -100;
				}
			}
			
			// Hard-code size of neighborhood
			int N = 25;
			
			// Construct weighted average
			Sorter sorter = new Sorter(sim);
			Integer[] user_index_list = sorter.createIndexArray(); //0 based
			Arrays.sort(user_index_list, sorter.reversed());

			double prediction_nume=0.0;
			double prediction_deno=0.0;
			double sum_rating=0.0;
			int no_effect_data=0;
			for(int j=0;j<N;j++){
				int ind = user_index_list[j];
//				System.out.println(sim[ind]);
				if(sim[ind]!=-100){					
//					System.out.println("User: "+ind+"; score:"+userList.get(ind).getRatings().get(premovie_index));
//					System.out.println(sim[ind]+";  "+ userList.get(user_index_list[j]).getRatings().get(premovie_index));
					sum_rating += userList.get(ind).getRatings().get(premovie_index);
					prediction_nume += sim[ind] * userList.get(ind).getRatings().get(premovie_index);				
					prediction_deno += sim[ind];
					no_effect_data++;
				}
				
			}
			double prediction;
			if(prediction_deno!=0){
				prediction = prediction_nume/prediction_deno;
			}else{
				prediction = sum_rating/(double) no_effect_data;
			}
//			System.out.println(i);
			// Set predicted rating
			if(prediction<0.0)prediction=0.0;
			if(prediction>5.0)prediction=5.0;
			if(Double.isNaN(prediction))prediction=0.0;
//			System.out.println( "User " + preuser_index +" + Movie "+premovie_index+" = "+prediction);
			predRatings.get(i).setRating(prediction);
			
		}
		System.out.print("done.");
		
		// Return predictions
		return predRatings;
	}
}
