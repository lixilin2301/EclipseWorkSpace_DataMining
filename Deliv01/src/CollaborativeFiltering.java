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
import java.util.Map;

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
		
		//Modeling biases 
		double[] bias_user = new double[nU];
		double[] bias_movie = new double[nM];
		double miu=0.0;
		for(Rating r:ratingList){
			miu+=r.getRating();
		}
		miu = miu / ratingList.size();
		
		//Store subtracted rating data, Euclidean distance to RAM for speed
		//After some optimization, the code could finish in few minutes with the cost of RAM
		ArrayList<HashMap<Integer,Double>> sim_map = new  ArrayList<HashMap<Integer,Double>>();
		ArrayList<HashMap<Integer,Double>> subratingList = new  ArrayList<HashMap<Integer,Double>>();
		double[] eucList = new double[nU];
		for (int i=0; i<nU ; i++) {
			HashMap<Integer,Double> tmp = new HashMap<Integer,Double>(userList.get(i).getRatings());
			double avg_tmp = Util.calculateAverage(tmp);
			bias_user[i] = avg_tmp - miu;		
			Util.subtractScalarVector(avg_tmp,tmp);
			subratingList.add(tmp);
			eucList[i] = Util.euclideanNorm(tmp);
			sim_map.add(new HashMap<Integer,Double>());
		}		
		for(int i=0; i<nM ; i++){
			bias_movie[i] = Util.calculateAverage( movieList.get(i).getRatings() ) - miu;
		}
		
		// Loop over to-be-predicted ratings predRatings.size()
		System.out.println("Running predictions..");
		for (int i=0; i<predRatings.size() ; i++) {			 
			// Inform progress
			if (i%1000==0) {
				System.out.println("Running predictions "+(i+1)+"/"+predRatings.size());
			}
			
			// Compute similarity with other users 
			// Cosine similarity OR Pearson similarity can be chosen from
			int premovie_index = predRatings.get(i).getMovie().getIndex()-1;
			int preuser_index = predRatings.get(i).getUser().getIndex()-1;		
			double[] sim = new double[nU];
			HashMap<Integer, Double> sub_preuser_rating = subratingList.get(preuser_index);
			for(int j=0;j<nU;j++){				
				if( j!=preuser_index && userList.get(j).getRatings().containsKey(premovie_index)){
//					System.out.println("Movie: "+ premovie_index+"; user:"+ j +";score:"+userList.get(j).getRatings().get(premovie_index));
//					sim[j] = Util.calculateCosine(preuser_rating, userList.get(j).getRatings(), nM);
//					sim[j] = Util.calculatePearson(preuser_rating, userList.get(j).getRatings(), nM);
					if(sim_map.get(preuser_index).containsKey(j)){
						sim[j] = sim_map.get(preuser_index).get(j);
					}else if(sim_map.get(j).containsKey(preuser_index)){
						sim[j] = sim_map.get(j).get(preuser_index);
					}else{
						sim[j] = Util.calculateCosine_v2(sub_preuser_rating, subratingList.get(j), eucList[preuser_index], eucList[j]);
//						sim[j] = Util.calculatePearson_v2(sub_preuser_rating, subratingList.get(j));
						sim_map.get(preuser_index).put(j, sim[j]);
					}
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
			
			//Calculate weighted prediction
			double prediction_nume=0.0;
			double prediction_deno=0.0;
			double sum_rating=0.0;
			int no_effect_data=0;
			for(int j=0;j<N;j++){
				int user_index = user_index_list[j];
				if(sim[user_index]!=-100 && userList.get(user_index).getRatings().containsKey(premovie_index)){					
//					System.out.println("User: "+user_index+"; score:"+userList.get(user_index).getRatings().get(premovie_index));
					double rating = userList.get(user_index).getRatings().get(premovie_index);
					prediction_nume += sim[user_index] * (rating-(miu + bias_user[j]+ bias_movie[premovie_index]));				
					prediction_deno += sim[user_index];
					sum_rating += rating;
					no_effect_data++;
				}				
			}
			//Check if the sum of prediction is zero
			double prediction=0.0;
			if(prediction_deno!=0){
				prediction = prediction_nume/prediction_deno + (miu + bias_user[preuser_index] + bias_movie[premovie_index] );
			}else{
				prediction = sum_rating/(double) no_effect_data;
			}
			
			// Set predicted rating
			if(prediction<0.0)prediction=0.0;
			if(prediction>5.0)prediction=5.0;
			if(Double.isNaN(prediction))prediction=3.0;
//			System.out.println( "User " + (preuser_index+1) +" + Movie "+(premovie_index+1)+" = "+prediction);
			predRatings.get(i).setRating(prediction);
		}
		System.out.print("done.");
		// Return predictions
		return predRatings;
	}
}
