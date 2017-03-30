import java.util.ArrayList;
import java.util.List;


public class main {
	
	/**
	 * Computes nrVectors eigen vectors of m where e is the
	 * stopping criterion for the norm of the difference for an
	 * eigenvector in between two rounds.
	 * @param m The matrix of which eigenvectors should be computed.
	 * @param nrVectors The number of eigenvectors to compute.
	 * @param e The threshold for the stopping criterion.
	 * @return A list of eigenvectors in m.
	 */
	public static List<Matrix> powerIteration(Matrix m, int nrVectors, double e) {
		assert(m.cols() == m.rows() && m.cols() >= nrVectors);
		
		List<Matrix> eigenvectors = new ArrayList<Matrix>();
		
		// add code here
		Matrix v = new Matrix(m.rows(), 1, 1);
		for (int i = 0; i < nrVectors; i++) {
			double error = 100;
			while (error > e) {
				Matrix v2 = m.dot(v).multiply(1 / m.dot(v).norm());
//				System.out.println(v2);
				error = Math.abs(v.norm() - v2.norm());
				v = v2;
			}
			eigenvectors.add(v);
			double lambda = v.transpose().dot(m).dot(v).get(0,0);
			m = m.add(v.dot(v.transpose()).multiply(-lambda));
		}
		
		return eigenvectors;
	}
	
	/**
	 * Computes two eigenvectors of a small matrix example.
	 */
	public static void powerIterationTest() {
		// add code here
		Matrix m = new Matrix();
		m = Matrix.readData("data/matrix.txt");
		List<Matrix> eigenvectors = new ArrayList<Matrix>();
		eigenvectors = powerIteration(m, 2, 10e-5);
		System.out.println(eigenvectors);
	}

	/**
	 * Computes the principal components from a Gaussian
	 * distributed dataset.
	 */
	public static void pca() {
		// add code here
	}
	
	/**
	 * Computes some principal components from a dataset
	 * of face images.
	 */
	public static void pcaFaces() {
		// add code here
	}

	public static void main(String[] args) {
		powerIterationTest();
		//pca();
		//pcaFaces();
	}

}
