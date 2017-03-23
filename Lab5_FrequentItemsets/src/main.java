import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class main {

	public static void main(String[] args) throws IOException {
		// add code here
		// APriori Part
		APriori ap = new APriori(3);
		readData(ap, "input_example/basket.txt");
		Set<StringSet> fs = ap.getFrequentSets(2);
		System.out.print("\n"+"Result: ");
		for(StringSet ss:fs)
			System.out.print(ss.toString());
		
		//PCY Part
		PCY pcy = new PCY(3,256);
		readData(pcy, "input_example/basket.txt");
		Set<StringSet> fsp = pcy.getFrequentSets(2);
		System.out.print("\n"+"Result: ");
		for(StringSet ss:fsp)
			System.out.print(ss.toString());
	}
	
	public static void readData(APriori ap, String file_path) throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader("input_example/basket.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	ap.addBasket(line);
		    }
		}
	}

}
