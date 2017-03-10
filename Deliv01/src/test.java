import java.util.HashMap;
import java.util.Map;

public class test{
	public static void main(String[] args) {
//		Map<Integer,Double> x = new HashMap<Integer,Double>();
//		Map<Integer,Double> y = new HashMap<Integer,Double>();
//		x.put(1, 1.0);
//		x.put(2, 3.0);
//		y.put(1, 2.0);
//		y.put(2, 2.0);
//		System.out.println(Util.calculateAverage(x));
//		System.out.println(Util.euclideanNorm(x));
//		System.out.println(Util.euclideanNorm(y));
//		System.out.println(Util.innerProduct(x, y));
//		System.out.println(Util.calculateCosine(x, y, 2));
	
		// Read user list
		UserList userList = new UserList();
		userList.readFile("data/users.csv");
		
		// Read movie list
		MovieList movieList = new MovieList();
		movieList.readFile("data/movies.csv");
		
		// Read rating list
		RatingList ratings = new RatingList();
		ratings.readFile("data/ratings.csv", userList, movieList);


		// Add ratings to user and movie lists
		userList.addRatings(ratings);
		movieList.addRatings(ratings);
		
		
//		System.out.println(ratings.get(index));
//		for(int j=0;j<userList.size();j++){
//			if(userList.get(j).getRatings().containsKey(1654)){
//				System.out.println(j);
//			}
//		}
		HashMap<Integer, Double> rating = movieList.get(836).getRatings();
//		for(Integer user_id:rating.keySet()){
//			System.out.println(rating.get(user_id));
//		}
		System.out.println(rating.get(2006));
	}
}