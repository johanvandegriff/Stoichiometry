package numbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FractionOld {
	private List<Integer> numeratorFactors, denominatorFactors;
	
	public FractionOld(int numerator, int denominator) {
		if (numerator == 0) {
			denominator = 1;
		}
		if (denominator < 0) {
			numerator *= -1;
			denominator *= -1;
		}
		numeratorFactors = primeFactors(numerator);
		denominatorFactors = primeFactors(denominator);
		//System.out.println(numeratorFactors + "/" + denominatorFactors);
		removeCommonFactors(numeratorFactors, denominatorFactors);
	}
	
	public static FractionOld fromString(String s){
		String[] nd = s.split("/");
		if (nd.length == 0) {
			return zero();
		} else if (nd.length == 1) {
			return integer(Integer.valueOf(nd[0]));
		} else if (nd.length == 2) {
			return new FractionOld(Integer.valueOf(nd[0]), Integer.valueOf(nd[1]));
		} else {
			int denominator = 1;
			for (int i=1; i<nd.length; i++) {
				denominator *= Integer.valueOf(nd[i]);
			}
			return new FractionOld(Integer.valueOf(nd[0]), denominator);
		}
	}
	public static FractionOld zero() {
		return integer(0);
	}
	public static FractionOld integer(int i) {
		return new FractionOld(i, 1);
	}
	
	
	
	
	
	
	
	public int getNumerator() {
//		System.out.println(":::" + numeratorFactors);
		return multiplyFactors(numeratorFactors);
	}

	public int getDenominator() {
		return multiplyFactors(denominatorFactors);
	}
	
	public double getValue() {
		return getNumerator() * 1.0 / getDenominator();
	}

	public List<Integer> getNumeratorFactors() {
		return numeratorFactors;
	}

	public List<Integer> getDenominatorFactors() {
		return denominatorFactors;
	}
	
	@Override
	public String toString(){
		int n = getNumerator();
		int d = getDenominator();
		if (d == 1) {
			return String.valueOf(n);
		} else if (d == 0){
			return "0";
		} else {
			return n + "/" + d;
		}
//		return getNumerator() + "/" + getDenominator();
//		return numeratorFactors + "/" + denominatorFactors;
	}
	
	
	
	
	
	
	public FractionOld abs() {
		return new FractionOld(Math.abs(getNumerator()), Math.abs(getDenominator()));
	}
	public FractionOld negative() {
		return new FractionOld(-getNumerator(), getDenominator());
	}
	
	
	
	
	
	public static FractionOld multiply(FractionOld fraction1, FractionOld fraction2) {
		return new FractionOld(fraction1.getNumerator() * fraction2.getNumerator(), fraction1.getDenominator() * fraction2.getDenominator());
	}

	public static FractionOld add(FractionOld fraction1, FractionOld fraction2) {
		int n1 = fraction1.getNumerator();
		int d1 = fraction1.getDenominator();
		int n2 = fraction2.getNumerator();
		int d2 = fraction2.getDenominator();
//		System.out.println(n1 + "/" + d1 + "+" + n2 + "/" + d2);
//		System.out.println("n2: " + n2);
		return new FractionOld(n1 * d2 + n2 * d1, d1 * d2);
	}
	
	public static FractionOld subtract(FractionOld fraction1, FractionOld fraction2) {
		int n1 = fraction1.getNumerator();
		int d1 = fraction1.getDenominator();
		int n2 = fraction2.getNumerator();
		int d2 = fraction2.getDenominator();
		return new FractionOld(n1 * d2 - n2 * d1, d1 * d2);
	}

	public static FractionOld divide(FractionOld fraction1, FractionOld fraction2) {
		return new FractionOld(fraction1.getNumerator() * fraction2.getDenominator(), fraction1.getDenominator() * fraction2.getNumerator());
	}
	
	
	
	
	
	
	public boolean isGreaterThan(FractionOld other) {
		return this.getNumerator() * other.getDenominator() > other.getNumerator() * this.getDenominator();
	}
	
	
	
	
	

	public FractionOld subtractFromSelf(FractionOld other) {
		int n1 = getNumerator();
		int d1 = getDenominator();
		int n2 = other.getNumerator();
		int d2 = other.getDenominator();
		numeratorFactors = primeFactors(n1 * d2 - n2 * d1);
		denominatorFactors = primeFactors(d1 * d2);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public FractionOld addToSelf(FractionOld other) {
		int n1 = getNumerator();
		int d1 = getDenominator();
		int n2 = other.getNumerator();
		int d2 = other.getDenominator();
		numeratorFactors = primeFactors(n1 * d2 + n2 * d1);
		denominatorFactors = primeFactors(d1 * d2);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;	
	}

	public FractionOld divideSelfBy(FractionOld fraction) {
		int numerator = getNumerator() * fraction.getDenominator();
		int denominator = getDenominator() * fraction.getNumerator();
		numeratorFactors = primeFactors(numerator);
		denominatorFactors = primeFactors(denominator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public FractionOld multiplySelfBy(FractionOld fraction) {
		int numerator = getNumerator() * fraction.getNumerator();
		int denominator = getDenominator() * fraction.getDenominator();
		numeratorFactors = primeFactors(numerator);
		denominatorFactors = primeFactors(denominator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}
	
	
	
	
	
	
	
	public static void removeCommonFactors(List<Integer> factors1, List<Integer> factors2) {
		Set<Integer> union = union(factors1, factors2);
		Map<Integer, Integer> factors1Hist = histogram(factors1);
		Map<Integer, Integer> factors2Hist = histogram(factors2);

		factors1.clear();
		factors2.clear();
		for (int i : union) {
			int v1 = 0;
			int v2 = 0;
			if (factors1Hist.containsKey(i)) {
				v1 = factors1Hist.get(i);
			}
			if (factors2Hist.containsKey(i)) {
				v2 = factors2Hist.get(i);
			}
			int min = Math.min(v1, v2);
			v1 -= min;
			v2 -= min;
			for(int j=0; j<v1; j++){
				factors1.add(i);
			}
			for(int j=0; j<v2; j++){
				factors2.add(i);
			}
		}
	}
	

	public static Set<Integer> union(List<Integer> list1, List<Integer> list2) {
		Set<Integer> union = new HashSet<>();
		union.addAll(list1);
		union.addAll(list2);
		return union;
	}
	
	public static Map<Integer, Integer> histogram(List<Integer> list){
		Map<Integer, Integer> map = new HashMap<>();
		for (int i : list){
			if (map.containsKey(i)){
				map.put(i, map.get(i)+1);
			} else {
				map.put(i, 1);
			}
		}
		return map;
	}

	/**
	 * http://www.vogella.com/tutorials/JavaAlgorithmsPrimeFactorization/article.html
	 * @param number the number to factor
	 * @return the prime factors of the number
	 */
	public static List<Integer> primeFactors(int number) {
		if (number < 0) {
			List<Integer> factors = primeFactors(-number);
			factors.add(-1);
			return factors;
		} else if (number == 0){
			List<Integer> factors = new ArrayList<>();
			factors.add(0);
			return factors;
		} else {
			int n = number;
			List<Integer> factors = new ArrayList<Integer>();
			for (int i = 2; i <= n; i++) {
				while (n % i == 0) {
					factors.add(i);
					n /= i;
				}
			}
			return factors;
		}
	}
	public static int multiplyFactors(List<Integer> factors) {
//		System.out.println("****" + factors);
		int result = 1;
		for (int factor : factors) {
			result *= factor;
		}
		return result;
	}
	
	
	
	
	
	
	
	
	

	public static FractionOld[] newArray(int a){
        FractionOld array[] = new FractionOld[a];
        for (int i=0; i<a; i++)
        	array[i] = FractionOld.zero();
		return array;
	}

	public static FractionOld[][] new2DArray(int a){
		return new2DArray(a, a);
	}
	public static FractionOld[][] new2DArray(int a, int b){
        FractionOld array[][] = new FractionOld[a][b];
        for (int i=0; i<a; i++)
            for (int j=0; j<b; j++)
            	array[i][j] = FractionOld.zero();
		return array;
	}
}
