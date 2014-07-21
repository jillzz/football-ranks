package ranking;

import weka.core.matrix.Matrix;

public class PageRanker {
	
	
	/**
	 * Calculates pagerank with restarts
	 * 
	 * @param A
	 * @param dampingFactor
	 * @return
	 */
	public static Matrix pagerank (Matrix A, double dampingFactor) {
		Matrix Q = buildTransitionMatrix(A, dampingFactor);
		Matrix p = powerMethod(Q);
			
		return p;
	}
	
	
	/**
	 * Builds the transition matrix (Google matrix)
	 * 
	 * @param A
	 * @param dampingFactor
	 * @return Matrix
	 */
	public static Matrix buildTransitionMatrix (Matrix A, double dampingFactor) {
		// (1-d) * A
		Matrix Q = A.copy();
		Q.timesEquals(1 - dampingFactor);
				
		// (1-d) * A / rowSum(A) 
		Matrix sums = new Matrix(A.getRowDimension(), A.getColumnDimension(), 1);
		sums = A.times(sums);
		
		// TODO
		//sums.plusEquals(new Matrix(
				//A.getRowDimension(), A.getColumnDimension(), 1e-15));
		Q.arrayRightDivideEquals(sums);
				
		// (1-d) * A / rowSum(A) + d/n
		Q.plusEquals(new Matrix(
				A.getRowDimension(), A.getColumnDimension(), dampingFactor / (double) A.getRowDimension()));
		
		return Q;
	}
	
	
	/**
	 * Applies the power method
	 * 
	 * @param Q
	 * @return Matrix
	 */
	public static Matrix powerMethod (Matrix Q) {
		Matrix p = new Matrix (Q.getRowDimension(), 1, 1.0/Q.getRowDimension());
		Matrix oldP;
		Matrix Qt = Q.transpose();
		
		do {
			oldP = p;
			p = Qt.times(oldP);
			
			oldP.minusEquals(p);
		} while (oldP.norm1() < 1e-10);
		
		return p;
	}
	
}
