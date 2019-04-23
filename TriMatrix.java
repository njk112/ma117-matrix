/*
 * PROJECT III: TriMatrix.java -- naglis
 *
 * This file contains a template for the class TriMatrix. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file. You will also need to have completed
 * the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diag;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upper;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lower;
    
    /**
     * Constructor function: should initialise m and n through the Matrix
     * constructor and set up the data array.
     *
     * @param N  The dimension of the array.
     */
    public TriMatrix(int N) {
        // You need to fill in this method.
    	super(N,N);
    	if (N == 0) {
            throw new MatrixException("N cannot be 0");
        }
        this.m = N; 
        this.n = N;	
        this.diag = new double[N];
        this.upper = new double[N - 1];
        this.lower = new double[N - 1];
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        // You need to fill in this method.
        if (i < 0 || j < 0 || this.m <= i || this.n <= j) {
            throw new MatrixException("Matrix out of bounds");
        }
        if (i == j) {
            return this.diag[i];
        }
        else if (j == i + 1) {
            return this.upper[i];
        }
        else if (j == i - 1) {
            return this.lower[j];
        }
        else return 0;
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i    The location in the first co-ordinate.
     * @param j    The location in the second co-ordinate.
     * @param val  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double val) {
        // You need to fill in this method.
        if (i < 0 || j < 0 || this.m <= i || this.n <= j) {
            throw new MatrixException("Matrix out of bounds");
        }
        if (i != j && j != i+1 && j != i-1) {
            throw new MatrixException("These elements cannot be set. They are always 0");
        }
        if (i == j) {
            this.diag[i] = val;
        }
        else if (j == i + 1) {
            this.upper[i] = val;
        }
        else if (j == i - 1) {
            this.lower[j] = val;
        }
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        if (this.n != this.m) {
            throw new MatrixException("Matrix is not a square!");
        }
        TriMatrix A = this.decomp();
        double det =1;
        for (int i =0; i < A.n; i++) {
            det = det * A.diag[i];
        }
        return det;
    }
    
    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     * 
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix decomp() {
        // You need to fill in this method.
        TriMatrix matri = new TriMatrix(this.n);
        matri.diag[0] = this.diag[0];
        for (int i =1; i < this.n; i++) {
            matri.upper[i-1] = this.upper[i-1];
            matri.lower[i-1] = this.lower[i-1] / matri.diag[i-1];
            matri.diag[i] = this.diag[i] - matri.lower[i-1] * matri.upper[i-1];
        }
        return matri;
    }

    /**
     * Add the matrix to another matrix A.
     *
     * @param A  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the matrix A.
     */
    public Matrix add(Matrix A){
        // You need to fill in this method.
        if (A.m != this.m || A.n != this.n) {
            throw new MatrixException("Matrix dimensions do not agree");
        } 
        GeneralMatrix matri = new GeneralMatrix(this.m, this.n);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j< this.n; j++) {
                double val = this.getIJ(i, j) + A.getIJ(i, j);
                matri.setIJ(i, j, val);
            }
        }
        return matri;
    }
    
    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {
        // You need to fill in this method.
        if (this.n != A.m) {
            throw new MatrixException("Matrix dimensions do not agree");
        }
        GeneralMatrix matri = new GeneralMatrix(this.m, A.n);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < A.n; j++) {
                double val = 0;
                for (int k = 0; k <this.n; k++) {
                    val = val + this.getIJ(i,k) * A.getIJ(k,j);
                }
                matri.setIJ(i, j, val);
            }
        }
        return matri;
    }
    
    /**
     * Multiply the matrix by a scalar.
     *
     * @param a  The scalar to multiply the matrix by.
     * @return   The product of this matrix with the scalar a.
     */
    public Matrix multiply(double a) {
        // You need to fill in this method.
        TriMatrix matri = new TriMatrix(this.m);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i == j || j == i+1 || j == i - 1) {
                    matri.setIJ(i, j, a * this.getIJ(i, j));
                }
            }
        }
        return matri;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        for (int i = 0; i < this.n -1; i++) {
            this.diag[i] = Math.random();
        	this.upper[i] = Math.random();
        	this.lower[i] = Math.random();
        }
        diag[this.n - 1] = Math.random();
    }
    
    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // You need to fill in this method.
        TriMatrix M = new TriMatrix(3);
    			M.setIJ(0,0,6.8);
    			M.setIJ(0,1,1);
    			//M.setIJ(0,2,3.1);
    			M.setIJ(1,0,-66);
    			M.setIJ(1,1,5);
    			M.setIJ(1,2,22);
    			//M.setIJ(2,0,5);
    			M.setIJ(2,1,-5);
    			M.setIJ(2,2,-5);
    			    			
    			//5 x 5 Matrix
    			TriMatrix M2 = new TriMatrix(5);
    			M2.setIJ(0,0,6.8);
    			M2.setIJ(0,1,110);
    			//M2.setIJ(0,2,3.1);
    			//M2.setIJ(0,3,3.3);
    			//M2.setIJ(0,4,33.3);
    			M2.setIJ(1,0,-66);
    			M2.setIJ(1,1,5);
    			M2.setIJ(1,2,22);
    			//M2.setIJ(1,3,3.1);
    			//M2.setIJ(1,4,3.23121);
    			//M2.setIJ(2,0,50001);
    			M2.setIJ(2,1,-125);
    			M2.setIJ(2,2,5);
    			M2.setIJ(2,3,-39.1);
    			//M2.setIJ(2,4,3.123);
    			//M2.setIJ(3,0,5);
    			//M2.setIJ(3,1,-15);
    			M2.setIJ(3,2,-15);
    			M2.setIJ(3,3,-329.1);
    			M2.setIJ(3,4,3.123);
    			//M2.setIJ(4,0,1231);
    			//M2.setIJ(4,1,-25);
    			//M2.setIJ(4,2,-50);
    			M2.setIJ(4,3,-39.1);
    			M2.setIJ(4,4,3.123);
    			
    			TriMatrix M3 = new TriMatrix(3);
    			/*M.setIJ(0,0,67);
    			M.setIJ(0,1,18);
    			M.setIJ(0,2,5);
    			M.setIJ(1,0,0.2);
    			M.setIJ(1,1,-10);
    			M.setIJ(1,2,-0.19);
    			M.setIJ(2,0,48);
    			M.setIJ(2,1,100);
    			M.setIJ(2,2,-2);*/
    			
    			M3.setIJ(0,0,12);
    			M3.setIJ(0,1,-2);
    			//M3.setIJ(0,2,1.19);
    			M3.setIJ(1,0,0.99);
    			M3.setIJ(1,1,1.313);
    			M3.setIJ(1,2,0.1);
    			//M3.setIJ(2,0,-10);
    			M3.setIJ(2,1,-9.4);
    			M3.setIJ(2,2,-3);
    			
    	/*		GeneralMatrix M4 = new GeneralMatrix(3,3);
    			M4.setIJ(0,0,6.8);
    			M4.setIJ(0,1,1);
    			M4.setIJ(0,2,3.1);
    			M4.setIJ(1,0,-66);
    			M4.setIJ(1,1,5);
    			M4.setIJ(1,2,22);
    			M4.setIJ(2,0,5);
    			M4.setIJ(2,1,-5);
    			M4.setIJ(2,2,-5);*/
    			
    			Matrix M_ADD_M3 = M.add(M3);
    			Matrix M3_ADD_M = M3.add(M);
    			Matrix M_MULT_M3 = M.multiply(M3);
    			Matrix M3_MULT_M = M3.multiply(M);
/*    			Matrix M4_MULT_M3 = M4.multiply(M3);
    			Matrix M3_MULT_M4 = M3.multiply(M4);*/
    			Matrix XM = M.multiply(5);
    			
    			
    			System.out.println("----------------------------------------");
    			System.out.println("M.toString() and M.determinant()");
    			System.out.println();
    			System.out.println(M.toString());
    			//System.out.println(M.decomp().toString());
    			System.out.println("Determinant:" + M.determinant());
    			//System.out.println("Determinant:" + M.determinant());
    			//System.out.println("Determinant:" + M.determinant());
    			System.out.println("----------------------------------------");
    			/*System.out.println("M2.toString() and M2.determinant()");
    			System.out.println();
    			System.out.println(M2.toString());
    			System.out.println("Determinant:" + M2.determinant());*/
    			System.out.println("M3.toString() and M3.determinant()");
    			System.out.println();
    			System.out.println(M3.toString());
    			System.out.println("Determinant:" + M3.determinant());
    			System.out.println("----------------------------------------");
    /*			System.out.println("M4.toString() and M4.determinant()");
    			System.out.println();
    			System.out.println(M4.toString());
    			System.out.println("Determinant:" + M4.determinant());*/
    			System.out.println("----------------------------------------");
    			System.out.println("M + M3");
    			System.out.println();
    			System.out.println(M_ADD_M3.toString());
    			System.out.println();
    			System.out.println("M3 + M");
    			System.out.println(M3_ADD_M.toString());
    			System.out.println("----------------------------------------");
    			System.out.println("M * M3");
    			System.out.println();
    			System.out.println(M_MULT_M3.toString());
    			System.out.println();
    			System.out.println("M3 * M");
    			System.out.println();
    			System.out.println(M3_MULT_M.toString());
    	/*		System.out.println("M4 * M3");
    			System.out.println();
    			System.out.println(M4_MULT_M3.toString());
    			System.out.println();
    			System.out.println("M3 * M");
    			System.out.println();
    			System.out.println(M3_MULT_M4.toString());*/
    			System.out.println("5M");
    			System.out.println();
    			System.out.println(XM.toString());
    			System.out.println("----------------------------------------");
    			System.out.println("M.random()");
    			M.random();
    			System.out.println(M.toString());
    			System.out.println("----------------------------------------");
    			TriMatrix EMPTY = new TriMatrix(2);
    			System.out.println(EMPTY.toString());
    			//System.out.println(EMPTY.determinant());
    			//System.out.println( ((double) -66)*((double) 12)+((double) 5)*0.99+22*0);
    }
}