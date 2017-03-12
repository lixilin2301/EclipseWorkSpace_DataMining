import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class NearestNeighbour {
	/**
	 * List of training vectors.
	 */
	Dataset dataset;

	/**
	 * Constructor.
	 */
	public NearestNeighbour() {
		dataset = new Dataset();
	}

	/**
	 * Reads in the data from a text file.
	 * @param fileName
	 */
	public void readData(String fileName) {
		dataset.readData(fileName, false);
	}

	/**
	 * 
	 * @return
	 */
	public Dataset getDataset() {
		return dataset;
	}

	/**
	 * Classifies a query. Finds the k nearest neighbours and scales them if necessary.
	 * @param features The query features.
	 * @param k The number of neighbours to select.
	 * @return Returns the label assigned to the query.
	 */
	public int predict(List<Double> features, int k) {
		// the result
		int label = -1;
		
		// add code here
		List<Measurement> ml = new ArrayList<Measurement>();
		for (FeatureVector fv : dataset) {
			Measurement meas = new Measurement(fv,fv.distance(features));
			ml.add(meas);
		}
		Collections.sort(ml);
		
		ArrayList<Integer> relation = new ArrayList<Integer>();
		
		int[] labels= new int[k+1];
		for (int i = 0; i < k+1; i++) {
			labels[i] = 0;
		}
//		System.out.println(ml.get(0).getFeatureVector().getLabel());  
		for (int i = 0; i < k; i++) {
			int rst = ml.get(0).getFeatureVector().getLabel();
			if(!relation.contains(rst)){
				relation.add(rst);
			}
			labels[relation.indexOf(rst)]++;
		}
		int max = 0;
		for (int i = 0; i < k+1; i++) {
			if (labels[i] > max) {
				max = labels[i];
				label = i;
			}
		}
		return relation.get(label);
	}
}
