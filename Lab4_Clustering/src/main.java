import java.util.List;

public class main {
	
	private static void hierarchical() {
		// add code here 
		int k = 3;
		HierarchicalClusteringPlotter hcp = new HierarchicalClusteringPlotter(k ,"data/cluster.txt");
//		HierarchicalClusteringPlotter hcp = new HierarchicalClusteringPlotter(k ,"data/cluster_lines.txt");
		
	}
	
	private static void hierarchicalDigits() {
		// add code here
		int k = 10;
		HierarchicalClustering hc = new HierarchicalClustering(k, "data/train_digits.txt");
		while (hc.getClusterSize() > k) {
			hc.update();
		}
		for (int i = 0; i < hc.getClusterSize(); i++) {
			DigitFrame df = new DigitFrame("title", hc.getCluster(i).centroid(), 8, 8);
		}	
	}
	
	private static void kmeans() {
		// add code here
	}
	
	private static void kmeansTuneK() {
		// add code here
	}
	
	private static void kmeansDigits() {
		// add code here
	}

	public static void main(String[] args) {
//		hierarchical();
		hierarchicalDigits();
		//kmeans();
		//kmeansTuneK();
		//kmeansDigits();
	}

}
