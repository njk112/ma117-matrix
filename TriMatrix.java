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
    	if (N <= 0) {
            throw new MatrixException("N can't be less than 1");
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
    			//5 x 5 Matrix
    			TriMatrix M1 = new TriMatrix(3);
    			M1.setIJ(0,0,3);
    			M1.setIJ(0,1,12);

    			M1.setIJ(1,0,-55);
    			M1.setIJ(1,1,6);
    			M1.setIJ(1,2,21);

    			M1.setIJ(2,1,-40.2);
                M1.setIJ(2,2,5.431);
                
    			
    			TriMatrix M2 = new TriMatrix(3);
    			
    			M2.setIJ(0,0,22);
                M2.setIJ(0,1,-11);
                
    			M2.setIJ(1,0,0.11);
    			M2.setIJ(1,1,2.3142);
    			M2.setIJ(1,2, 4.8);

                M2.setIJ(2,1,-10);
    			M2.setIJ(2,2,-343);
    			

    			
    			Matrix M1_ADD_M2 = M1.add(M2);
    			Matrix M2_ADD_M1 = M2.add(M1);
    			Matrix M1_MULT_M2 = M1.multiply(M2);
    			Matrix M2_MULT_M1 = M2.multiply(M1);
    			Matrix XM1 = M1.multiply(5);
    			
    			
                System.out.println("<------------------>");
                System.out.println("--------------------");
    			System.out.println("M1 string and M1 d");
    			System.out.println();
    			System.out.println(M1.toString());

    			System.out.println("Determinant:" + M1.determinant());

                System.out.println("<------------------>");
                System.out.println("--------------------");
  
    			System.out.println("M2 string and M2 d");
    			System.out.println();
    			System.out.println(M2.toString());
    			System.out.println("Determinant:" + M2.determinant());
                System.out.println("<------------------>");
                System.out.println("--------------------");

                System.out.println("<------------------>");
                System.out.println("--------------------");
    			System.out.println("M1 + M2");
    			System.out.println();
    			System.out.println(M1_ADD_M2.toString());
    			System.out.println();
    			System.out.println("M2 + M1");
    			System.out.println(M2_ADD_M1.toString());
                System.out.println("<------------------>");
                System.out.println("--------------------");
    			System.out.println("M1 * M2");
    			System.out.println();
    			System.out.println(M1_MULT_M2.toString());
    			System.out.println();
    			System.out.println("M2 * M1");
    			System.out.println();
    			System.out.println(M2_MULT_M1.toString());

    			System.out.println("5M");
    			System.out.println();
    			System.out.println(XM1.toString());
                System.out.println("<------------------>");
                System.out.println("--------------------");
    			System.out.println("M1 random");
    			M1.random();
    			System.out.println(M1.toString());
                System.out.println("<------------------>");
                System.out.println("--------------------");
    			TriMatrix EMPTY = new TriMatrix(2);
    			System.out.println(EMPTY.toString());

    }
}