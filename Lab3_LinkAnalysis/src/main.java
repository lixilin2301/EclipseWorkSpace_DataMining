import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class main {

	public static void main(String[] args) {

        // PageRank Step 1-8
		PageRank pr = new PageRank();
		pr.importData("data/example2.txt");
		Matrix cm = pr.constructTransitionMatrix();
		System.out.println(cm);
		System.out.println("PageRank result: " + pr.calculatePageRank(10) + "\n");
		
		// TaxPageRank Step 9
		TaxationPageRank taxPr = new TaxationPageRank(0.8);
		taxPr.importData("data/example2.txt");
		System.out.println("TaxPageRank result: " + taxPr.calculatePageRank(10) + "\n");
		
		// TaxPageRank Step 10
		TaxationPageRank taxPr_air = new TaxationPageRank(1);
		taxPr_air.importData("data/flight_data.txt");
		Map<String, Double> result_air = taxPr_air.calculatePageRank(100);
		result_air = sortByValues(result_air);
		System.out.println("Air TaxPageRank result: " + result_air);
	}
		
	/*
	 * Java method to sort Map in Java by value e.g. HashMap or Hashtable
	 * throw NullPointerException if Map contains null values
	 * It also sort values even if they are duplicates
	 */
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
		List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		//LinkedHashMap will keep the keys in the order they are inserted
		//which is currently sorted on natural ordering
		Map<K,V> sortedMap = new LinkedHashMap<K,V>();

		for(Map.Entry<K,V> entry: entries){
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}
