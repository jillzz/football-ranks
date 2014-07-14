package ranking;

import weka.core.matrix.Matrix;

public class PageRanker {
	
	public static Matrix pagerank (Matrix A, double dampingFactor) {
		Matrix Q = buildTransitionMatrix(A, dampingFactor);
		Matrix p = powerMethod(Q);
		return p;
	}
	
	
	public static Matrix buildTransitionMatrix (Matrix A, double dampingFactor) {
		// (1-d) * A
		String deb = A.toMatlab();
		System.out.println(deb.indexOf("NaN"));
		Matrix Q = A.copy();
		Q.timesEquals(1 - dampingFactor);
		deb = Q.toMatlab();
		System.out.println(deb.indexOf("NaN"));
		
		// (1-d) * A / rowSum(A) 
		Matrix sums = new Matrix(A.getRowDimension(), A.getColumnDimension(), 1);
		sums = A.times(sums);
		sums.plusEquals(new Matrix(
				A.getRowDimension(), A.getColumnDimension(), 1e-15));
		Q.arrayRightDivideEquals(sums);
		deb = Q.toMatlab();
		System.out.println(deb.indexOf("NaN"));
		
		// (1-d) * A / rowSum(A) + d/n
		Q.plusEquals(new Matrix(
				A.getRowDimension(), A.getColumnDimension(), dampingFactor / (double) A.getRowDimension()));
		deb = Q.toMatlab();
		System.out.println(deb.indexOf("NaN"));
		return Q;
	}
	
	
	public static Matrix powerMethod (Matrix Q) {
		Matrix p = new Matrix (Q.getRowDimension(), 1, 1.0/Q.getRowDimension());
		Matrix oldP;
		Matrix Qt = Q.transpose();
		
		do {
			oldP = p;
			p = Qt.times(oldP);
			
			oldP.minusEquals(p);
		} while (oldP.norm1() < 1e-6);
		
		return p;
	}
	
}
