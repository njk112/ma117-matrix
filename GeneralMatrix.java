/*
 * PROJECT III: GeneralMatrix.java -- naglis
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
 * are implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file. You will also need to have completed
 * the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

import java.util.Arrays;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] data;

    /**
     * Constructor function: should initialise m and n through the Matrix
     * constructor and set up the data array.
     *
     * @param m  The first dimension of the array.
     * @param n  The second dimension of the array.
     */
    public GeneralMatrix(int m, int n) throws MatrixException {
        // You need to fill in this method.
        super(m,n);
        if (m<= 0 || n<= 0) {
            throw new MatrixException("Dimensions cannot be less than 1!");
        }
        this.data = new double[m][n];
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the matrix A.
     *
     * @param A  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix A) {
        // You need to fill in this method.
        super(A.m,A.n);
        this.data = new double[A.m][A.n];
        for (int i = 0; i <A.m; i++) {
            for (int j =0; j <A.n; j++) {
                this.data[i][j] = A.getIJ(i, j);
            }
        }
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
        if (i < 0 || j < 0 || this.m <= i || this.n <= j) throw new MatrixException("Matrix out of bounds");  
        return this.data[i][j];
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
        this.data[i][j] = val;
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        if (this.n != this.m) throw new MatrixException("Matrix is not a square!");
        double[] sv = new double[1];
        GeneralMatrix A = this.decomp(sv);
        double det = 1;
        for (int i =0; i< A.n; i++) {
            det *= A.getIJ(i, i);
        }
        return det * sv[0];
    }

    /**
     * Add the matrix to another matrix A.
     *
     * @param A  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the matrix A.
     */
    public Matrix add(Matrix A) {
        // You need to fill in this method.
        if (A.m != this.m || A.n != this.n) throw new MatrixException("Matrix dimensions do not agree");
        GeneralMatrix mat = new GeneralMatrix(this.m, this.n);
        for (int i = 0; i <this.m; i++) {
            for (int j =0; j < this.n; j++) {
                double val = this.getIJ(i, j) + A.getIJ(i, j);
                mat.setIJ(i, j, val);
            }
        }
        return mat;
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
        if (this.n != A.m) throw new MatrixException("Matrix dimensions do not agree");
        GeneralMatrix mat = new GeneralMatrix(this.m, A.n);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < A.n; j++) {
                double val = 0;
                for (int k = 0; k <this.n; k++) {
                    val += this.getIJ(i,k) * A.getIJ(k,j);
                }
                mat.setIJ(i, j, val);
            }
        }
        return mat;
    }

    /**
     * Multiply the matrix by a scalar.
     *
     * @param a  The scalar to multiply the matrix by.
     * @return   The product of this matrix with the scalar a.
     */
    public Matrix multiply(double a) {
        // You need to fill in this method.
        GeneralMatrix mat = new GeneralMatrix (this.m, this.n);
        for (int i=0; i < this.m; i++) {
            for (int j = 0; j <this.n; j++) {
                mat.setIJ(i,j,a * this.getIJ(i, j));
            }
        }
        return mat;
    }


    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        for (int i=0; i < this.m; i++) {
            for (int j = 0; j <this.n; j++) {
                this.setIJ(i, j, Math.random());
            }
        }
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     * 
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 l_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * d[0] calculated by the function.
     * 
     * If the matrix is singular, then the routine throws a MatrixException.
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     * 
     * @param d  An array of length 1. On exit, the value contained in here
     *           will either be 1 or -1, which you can use to calculate the
     *           correct sign on the determinant.
     * @return   The LU decomposition of the matrix.
     */
    public GeneralMatrix decomp(double[] d) {
        // This method is complete. You should not even attempt to change it!!
        if (n != m)
            throw new MatrixException("Matrix is not square");
        if (d.length != 1)
            throw new MatrixException("d should be of length 1");
        
        int           i, imax = -10, j, k; 
        double        big, dum, sum, temp;
        double[]      vv   = new double[n];
        GeneralMatrix a    = new GeneralMatrix(this);
        
        d[0] = 1.0;
        
        for (i = 1; i <= n; i++) {
            big = 0.0;
            for (j = 1; j <= n; j++)
                if ((temp = Math.abs(a.data[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }
        
        for (j = 1; j <= n; j++) {
            for (i = 1; i < j; i++) {
                sum = a.data[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.data[i-1][k-1]*a.data[k-1][j-1];
                a.data[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= n; i++) {
                sum = a.data[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.data[i-1][k-1]*a.data[k-1][j-1];
                a.data[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= n; k++) {
                    dum = a.data[imax-1][k-1];
                    a.data[imax-1][k-1] = a.data[j-1][k-1];
                    a.data[j-1][k-1] = dum;
                }
                d[0] = -d[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.data[j-1][j-1] == 0.0)
                a.data[j-1][j-1] = 1.0e-20;
            if (j != n) {
                dum = 1.0/a.data[j-1][j-1];
                for (i = j+1; i <= n; i++)
                    a.data[i-1][j-1] *= dum;
            }
        }
        
        return a;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
		
		//3 x 3 
		GeneralMatrix M = new GeneralMatrix(3,3);
		M.setIJ(0,0,6.8);
		M.setIJ(0,1,1);
		M.setIJ(0,2,3.1);
		M.setIJ(1,0,-66);
		M.setIJ(1,1,5);
		M.setIJ(1,2,22);
		M.setIJ(2,0,5);
		M.setIJ(2,1,-5);
		M.setIJ(2,2,-5);
		
		//5 x 5
		GeneralMatrix M2 = new GeneralMatrix(5,5);
		M2.setIJ(0,0,6.8);
		M2.setIJ(0,1,110);
		M2.setIJ(0,2,3.1);
		M2.setIJ(0,3,3.3);
		M2.setIJ(0,4,33.333);
		M2.setIJ(1,0,-66.333);
		M2.setIJ(1,1,5);
		M2.setIJ(1,2,22);
		M2.setIJ(1,3,3.1);
		M2.setIJ(1,4,3.23);
		M2.setIJ(2,0,501);
		M2.setIJ(2,1,-125);
		M2.setIJ(2,2,5);
		M2.setIJ(2,3,-39.1);
		M2.setIJ(2,4,3.12);
		M2.setIJ(3,0,5);
		M2.setIJ(3,1,-15);
		M2.setIJ(3,2,-15);
		M2.setIJ(3,3,-329.1);
		M2.setIJ(3,4,3.12);
		M2.setIJ(4,0,1231);
		M2.setIJ(4,1,-25);
		M2.setIJ(4,2,-50);
		M2.setIJ(4,3,-39.1);
		M2.setIJ(4,4,3.12);
		
        GeneralMatrix M3 = new GeneralMatrix(3,3);
        		
		M3.setIJ(0,0,12);
		M3.setIJ(0,1,-2);
		M3.setIJ(0,2,1.19);
		M3.setIJ(1,0,0.99);
		M3.setIJ(1,1,1.31);
		M3.setIJ(1,2,0.1);
		M3.setIJ(2,0,-10);
		M3.setIJ(2,1,-9.4);
		M3.setIJ(2,2,-3);
		
		Matrix M_ADD_M3 = M.add(M3);
		Matrix M3_ADD_M = M3.add(M);
		Matrix M_MULT_M3 = M.multiply(M3);
		Matrix M3_MULT_M = M3.multiply(M);
		Matrix XM = M.multiply(5);
		
        System.out.println("<------------------>");
        System.out.println("--------------------");
		System.out.println("M1 string to M det");
		System.out.println();
		System.out.println(M.toString());
		System.out.println("Det:" + M.determinant());
		System.out.println("<------------------>");
		System.out.println("M2 string to M2 det ");
		System.out.println();
		System.out.println(M2.toString());
		System.out.println("Det:" + M2.determinant());
        System.out.println("<------------------>");
        System.out.println("--------------------");
		System.out.println("M3 string to M3 det");
		System.out.println();
		System.out.println(M3.toString());
		System.out.println("Det" + M3.determinant());
        System.out.println("<------------------>");
        System.out.println("--------------------");
		System.out.println("M1 + M3");
		System.out.println();
		System.out.println(M_ADD_M3.toString());
		System.out.println();
		System.out.println("M3 + M1");
		System.out.println(M3_ADD_M.toString());
        System.out.println("<------------------>");
        System.out.println("--------------------");
		System.out.println("M1 * M3");
		System.out.println();
		System.out.println(M_MULT_M3.toString());
		System.out.println();
		System.out.println("M3 * M1");
		System.out.println();
		System.out.println(M3_MULT_M.toString());
		System.out.println("5M");
		System.out.println();
		System.out.println(XM.toString());
        System.out.println("<------------------>");
        System.out.println("--------------------");
		System.out.println("Rand");
		M.random();
		System.out.println(M.toString());
        System.out.println("<------------------>");
        System.out.println("--------------------");
        System.out.println("Empty");
        
		GeneralMatrix E = new GeneralMatrix(2,1);
		System.out.println(E.toString());

	}
}