
public class main {

	public static void perceptron() {
		// add code here
		Dataset dataa = new Dataset("data/gaussian.txt",true);
		Perceptron perceptron = new Perceptron(1);
		PerceptronPlotter plotter = new PerceptronPlotter("Class_neg","Class_pos");
		
		perceptron.updateWeights(dataa);
		plotter.plotData(dataa, perceptron);
	}

	public static void perceptronDigits() {
		// add code here
		Dataset data_train = new Dataset("data/train_digits.txt",true);
		Perceptron perceptron = new Perceptron(1);
		//Show image and see size of the dataset
//		System.out.println(data_train.size());
//		DigitFrame df_origin1 = new DigitFrame("Data No5",data_train.get(5), 8, 8);
//		DigitFrame df_origin2 = new DigitFrame("Data No90",data_train.get(90), 8, 8);
		
		//Train
		int iter = 2;		
		for(int i=0;i<iter;i++){
			perceptron.updateWeights(data_train);
		}
//		DigitFrame df_origin2 = new DigitFrame("Weight",perceptron.getWeights(),8,8);
		
		//Test
		Dataset data_test = new Dataset("data/test_digits.txt",true);
		int size_train = data_test.size();
		int count_correct= 0;
		for(FeatureVector fv:data_test){
			if(perceptron.predict(fv)==fv.label){
				count_correct++;
			}else{
				DigitFrame df_orig = new DigitFrame("Wrong",fv,8,8);
			}
		}
		System.out.println((double)count_correct/(double)size_train);
					
	}

	public static void nearestNeighbour() {
		// add code here
	}
	
	public static void nearestNeighbourDigits() {
		// add code here
	}

	public static void main(String[] args) {
//		perceptron();
		perceptronDigits();
		//nearestNeighbour();
		//nearestNeighbourDigits();
	}

}