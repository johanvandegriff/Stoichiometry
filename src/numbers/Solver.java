package numbers;

import java.util.ArrayList;
import java.util.List;

/*
 * Original is at:
 * http://www.sanfoundry.com/java-program-solve-linear-equation/
 * Changed to use fractions instead of doubles
 */

public class Solver {
//	private static final char[] varNames = {
//		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
//		'n', 'o', 'p', 'q', 'r', 's', 't', 'u',	'v', 'w', 'x', 'y', 'z'
//		};

	public static List<Fraction> solve(Fraction[][] mat, Fraction[][] constants) {
		int h = mat.length;
		int w = mat[0].length;
		
		if (h > w) {
			//too many equations
			Fraction[][] mat2 = new Fraction[w][w];
			for (int y=0; y<w; y++)
				for (int x=0; x<w; x++)
					mat2[y][x] = mat[y][x];
			List<Fraction> coefficients = solveSquare(mat2, constants);
			for (int y=w; y<h; y++) {
				Fraction total = Fraction.zero();
				for (int x=0; x<w; x++) {
					total.addToSelf(Fraction.multiply(coefficients.get(x), mat[y][x]));
				}
				if (!total.equals(constants[y][0])) {
					throw new IllegalArgumentException("Set of equations has no solutions.");
				}
			}
			return coefficients;
		} else if (h < w) {
			//too few equations
			throw new IllegalArgumentException("Set of equations has too few equations.");
		} else {
			//the correct number of equations
			return solveSquare(mat, constants);
		}
	}
	
	public static List<Fraction> solveSquare(Fraction[][] mat, Fraction[][] constants) {
		int n = mat.length;

		// Matrix representation
		for (int i = 0; i < n; i++) {
			boolean first = true;
			for (int j = 0; j < n; j++) {
				if (first) {
					first = false;
				} else {
					//System.out.print(" + ");
				}
				//System.out.print(mat[i][j] + " " + varNames[j]);
			}
			//System.out.println(" = " + constants[i][0]);
		}
		// inverse of matrix mat[][]
		Fraction inverted_mat[][] = invert(mat);
//		System.out.println("The inverse is: ");
//		for (int i = 0; i < n; ++i) {
//			for (int j = 0; j < n; ++j) {
//				System.out.print(inverted_mat[i][j] + "  ");
//			}
//			System.out.println();
//		}
		// Multiplication of mat inverse and constants
		Fraction result[][] = Fraction.new2DArray(n, 1);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 1; j++) {
				for (int k = 0; k < n; k++) {
					result[i][j].addToSelf(Fraction.multiply(inverted_mat[i][k], constants[k][j]));
				}
			}
		}
		List<Fraction> vars = new ArrayList<>();
		//System.out.println("The product is:");
		for (int i = 0; i < n; i++) {
			//System.out.println(varNames[i] + " = " + result[i][0] + " ");
			vars.add(result[i][0]);
		}
		return vars;

	}

	public static Fraction[][] invert(Fraction a[][]) {
		int n = a.length;
		Fraction x[][] = Fraction.new2DArray(n);
		Fraction b[][] = Fraction.new2DArray(n);
		int index[] = new int[n];
		for (int i = 0; i < n; ++i)
			b[i][i] = Fraction.integer(1);

		// Transform the matrix into an upper triangle
		gaussian(a, index);

		// Update the matrix b[i][j] with the ratios stored
		for (int i = 0; i < n - 1; ++i)
			for (int j = i + 1; j < n; ++j)
				for (int k = 0; k < n; ++k)
					b[index[j]][k].subtractFromSelf(Fraction.multiply(a[index[j]][i], b[index[i]][k]));

		// Perform backward substitutions
		for (int i = 0; i < n; ++i) {
			x[n - 1][i] = Fraction.divide(b[index[n - 1]][i], a[index[n - 1]][n - 1]);
			for (int j = n - 2; j >= 0; --j) {
				x[j][i] = b[index[j]][i];
				for (int k = j + 1; k < n; ++k) {
					x[j][i].subtractFromSelf(Fraction.multiply(a[index[j]][k], x[k][i]));
				}
				x[j][i].divideSelfBy(a[index[j]][j]);
			}
		}
		return x;
	}

	// Method to carry out the partial-pivoting Gaussian
	// elimination. Here index[] stores pivoting order.

	public static void gaussian(Fraction a[][], int index[]) {
		int n = index.length;
		Fraction c[] = new Fraction[n];

		// Initialize the index
		for (int i = 0; i < n; ++i)
			index[i] = i;

		// Find the rescaling factors, one from each row
		for (int i = 0; i < n; ++i) {
			Fraction c1 = Fraction.zero();
			for (int j = 0; j < n; ++j) {
				Fraction c0 = a[i][j].abs();
				if (c0.isGreaterThan(c1))
					c1 = c0;
			}
			c[i] = c1;
		}

		// Search the pivoting element from each column
		int k = 0;
		for (int j = 0; j < n - 1; ++j) {
			Fraction pi1 = Fraction.zero();
			for (int i = j; i < n; ++i) {
				Fraction pi0 = a[index[i]][j].abs();
				pi0.divideSelfBy(c[index[i]]);
				if (pi0.isGreaterThan(pi1)) {
					pi1 = pi0;
					k = i;
				}
			}

			// Interchange rows according to the pivoting order
			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i = j + 1; i < n; ++i) {
				Fraction pj = Fraction.divide(a[index[i]][j], a[index[j]][j]);

				// Record pivoting ratios below the diagonal
				a[index[i]][j] = pj;

				// Modify other elements accordingly
				for (int l = j + 1; l < n; ++l)
					a[index[i]][l].subtractFromSelf(Fraction.multiply(pj, a[index[j]][l]));
			}
		}
	}
}