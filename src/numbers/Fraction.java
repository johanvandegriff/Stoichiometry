package numbers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Fraction {
	private Map<Integer, Integer> numeratorFactors, denominatorFactors;
	
	
	public static double round8(double value){
		return Math.round(value * 100000000.0)/100000000.0;
	}
	
	public static Fraction fromDouble(double d) {
		d = round8(d);
        String s = String.valueOf(d);
        int digitsDec = s.length() - 1 - s.indexOf('.');
        int denom = 1;
        for (int i = 0; i < digitsDec; i++) {
            d *= 10;    
            denom *= 10;
        }

        int num = (int) Math.round(d);
//        int g = gcd(num, denom);
        return new Fraction(num, denom);
//        this.num = num / g;
//        this.denom = denom /g;
		
	}
	
	public Fraction(int numerator, int denominator) {
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
	
	public static Fraction fromString(String s){
		String[] nd = s.split("/");
		if (nd.length == 0) {
			return zero();
		} else if (nd.length == 1) {
			return integer(Integer.valueOf(nd[0]));
		} else if (nd.length == 2) {
			return new Fraction(Integer.valueOf(nd[0]), Integer.valueOf(nd[1]));
		} else {
			int denominator = 1;
			for (int i=1; i<nd.length; i++) {
				denominator *= Integer.valueOf(nd[i]);
			}
			return new Fraction(Integer.valueOf(nd[0]), denominator);
		}
	}
	public static Fraction zero() {
		return integer(0);
	}
	public static Fraction integer(int i) {
		return new Fraction(i, 1);
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

	public Map<Integer, Integer> getNumeratorFactors() {
		return numeratorFactors;
	}

	public Map<Integer, Integer> getDenominatorFactors() {
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
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    Fraction fraction = (Fraction) o;
	    // field comparison
	    int n1 = getNumerator();
	    int n2 = fraction.getNumerator();
	    int d1 = getDenominator();
	    int d2 = fraction.getDenominator();
//	    System.out.println(n1+"/"+d1+" =?= "+n2+"/"+d2);
	    return (n1 == 0 && n2 == 0) ||
	    		(d1 == 0 && d2 == 0) ||
	    		(n1 == n2 && d1 == d2);
	}	
	
	
	
	
	
	public Fraction abs() {
		return new Fraction(Math.abs(getNumerator()), Math.abs(getDenominator()));
	}
	public Fraction negative() {
		return new Fraction(-getNumerator(), getDenominator());
	}
	
	
	
	
	
	public static Fraction multiply(Fraction fraction1, Fraction fraction2) {
		return new Fraction(fraction1.getNumerator() * fraction2.getNumerator(), fraction1.getDenominator() * fraction2.getDenominator());
	}

	public static Fraction add(Fraction fraction1, Fraction fraction2) {
		int n1 = fraction1.getNumerator();
		int d1 = fraction1.getDenominator();
		int n2 = fraction2.getNumerator();
		int d2 = fraction2.getDenominator();
//		System.out.println(n1 + "/" + d1 + "+" + n2 + "/" + d2);
//		System.out.println("n2: " + n2);
		return new Fraction(n1 * d2 + n2 * d1, d1 * d2);
	}
	
	public static Fraction subtract(Fraction fraction1, Fraction fraction2) {
		int n1 = fraction1.getNumerator();
		int d1 = fraction1.getDenominator();
		int n2 = fraction2.getNumerator();
		int d2 = fraction2.getDenominator();
		return new Fraction(n1 * d2 - n2 * d1, d1 * d2);
	}

	public static Fraction divide(Fraction fraction1, Fraction fraction2) {
		return new Fraction(fraction1.getNumerator() * fraction2.getDenominator(), fraction1.getDenominator() * fraction2.getNumerator());
	}
	
	
	
	
	
	
	public boolean isGreaterThan(Fraction other) {
		return this.getNumerator() * other.getDenominator() > other.getNumerator() * this.getDenominator();
	}
	
	
	
	
	

	public Fraction subtractFromSelf(Fraction fraction) {
		int n1 = getNumerator();
		int d1 = getDenominator();
		int n2 = fraction.getNumerator();
		int d2 = fraction.getDenominator();
		numeratorFactors = primeFactors(n1 * d2 - n2 * d1);
		denominatorFactors = primeFactors(d1 * d2);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public Fraction addToSelf(Fraction fraction) {
		int n1 = getNumerator();
		int d1 = getDenominator();
		int n2 = fraction.getNumerator();
		int d2 = fraction.getDenominator();
		numeratorFactors = primeFactors(n1 * d2 + n2 * d1);
		denominatorFactors = primeFactors(d1 * d2);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;	
	}

	public Fraction divideSelfBy(Fraction fraction) {
		int numerator = getNumerator() * fraction.getDenominator();
		int denominator = getDenominator() * fraction.getNumerator();
		numeratorFactors = primeFactors(numerator);
		denominatorFactors = primeFactors(denominator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public Fraction multiplySelfBy(Fraction fraction) {
		int numerator = getNumerator() * fraction.getNumerator();
		int denominator = getDenominator() * fraction.getDenominator();
		numeratorFactors = primeFactors(numerator);
		denominatorFactors = primeFactors(denominator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}
	
	
	
	
	
	public Fraction subtractFromSelf(int number) {
		int n = getNumerator();
		int d = getDenominator();
		numeratorFactors = primeFactors(n - number * d);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public Fraction addToSelf(int number) {
		int n = getNumerator();
		int d = getDenominator();
		numeratorFactors = primeFactors(n + number * d);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;	
	}

	public Fraction divideSelfBy(int number) {
		int denominator = getDenominator() * number;
		denominatorFactors = primeFactors(denominator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}

	public Fraction multiplySelfBy(int number) {
//		return multiplySelfBy(new Fraction(number, 1));
		int numerator = getNumerator() * number;
		numeratorFactors = primeFactors(numerator);
		removeCommonFactors(numeratorFactors, denominatorFactors);
		return this;
	}
	
	
	
	
	
	
	
	public static void removeCommonFactors(Map<Integer, Integer> factors1, Map<Integer, Integer> factors2) {
		Set<Integer> union = union(factors1.keySet(), factors2.keySet());

		for (int i : union) {
			int v1 = 0;
			int v2 = 0;
			if (factors1.containsKey(i)) {
				v1 = factors1.get(i);
			}
			if (factors2.containsKey(i)) {
				v2 = factors2.get(i);
			}
			int min = Math.min(v1, v2);
			v1 -= min;
			v2 -= min;
			if (v1 == 0) {
				factors1.remove(i);
			} else {
				factors1.put(i, v1);
			}
			if (v2 == 0) {
				factors2.remove(i);
			} else {
				factors2.put(i, v2);
			}
		}
	}
	

	public static Set<Integer> union(Set<Integer> list1, Set<Integer> list2) {
		Set<Integer> union = new HashSet<>();
		union.addAll(list1);
		union.addAll(list2);
		return union;
	}
	
//	public static Map<Integer, Integer> histogram(List<Integer> list){
//		Map<Integer, Integer> map = new HashMap<>();
//		for (int i : list){
//			if (map.containsKey(i)){
//				map.put(i, map.get(i)+1);
//			} else {
//				map.put(i, 1);
//			}
//		}
//		return map;
//	}

	/**
	 * http://www.vogella.com/tutorials/JavaAlgorithmsPrimeFactorization/article.html
	 * @param number the number to factor
	 * @return the prime factors of the number
	 */
	public static Map<Integer, Integer> primeFactors(int number) {
		if (number < 0) {
			Map<Integer, Integer> factors = primeFactors(-number);
			factors.put(-1, 1);
			return factors;
		} else if (number == 0){
			Map<Integer, Integer> factors = new HashMap<>();
			factors.put(0, 1);
			return factors;
		} else {
			int n = number;
			Map<Integer, Integer> factors = new HashMap<>();
			for (int i = 2; i <= n; i++) {
				int exp = 0;
				while (n % i == 0) {
					exp++;
//					factors.add(i);
					n /= i;
				}
				if (exp != 0) {
					factors.put(i, exp);
				}
			}
			return factors;
		}
	}
	public static int multiplyFactors(Map<Integer, Integer> factors) {
		int result = 1;
		for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
			int number = entry.getKey();
			int exp = entry.getValue();
			result *= Math.pow(number, exp);
		}
		return result;
//		System.out.println("****" + factors);
//		int result = 1;
//		for (int factor : factors) {
//			result *= factor;
//		}
//		return result;
	}

	public static int lowestCommonDenominator(Fraction fraction1, Fraction fraction2){
		List<Fraction> fractions = new ArrayList<>();
		return lowestCommonDenominator(fractions);
	}

	public static int lowestCommonDenominator(Collection<Fraction> fractions){
		List<Map<Integer, Integer>> factors = new ArrayList<>();
		for (Fraction fraction : fractions) {
//			if (fraction.getNumerator() != 0 && fraction.getDenominator() != 0){
//				System.out.println("fraction: " + fraction.getValue());
				factors.add(fraction.getDenominatorFactors());
//			}
		}
		return lowestCommonMultiple(factors);
	}
	public static int lowestCommonMultiple(List<Map<Integer, Integer>> factors){
		Set<Integer> union = new HashSet<>();
		for (Map<Integer, Integer> factor : factors) {
			union.addAll(factor.keySet());
		}

		int lcm = 1;
		for (int i : union) {
//			List<Integer> values = new ArrayList<>();

			int max = 0;
			for (Map<Integer, Integer> factor : factors) {
				if (factor.containsKey(i)){
					int value = factor.get(i);
//					values.add(value);
					if (value > max) {
						max = value;
					}
				}
//				else {
//					values.add(0);
//				}
			}
			lcm *= Math.pow(i, max);
			
		}
		
		return lcm;
	}
	
	
	
	
	
	
	
	
	

	public static Fraction[] newArray(int a){
        Fraction array[] = new Fraction[a];
        for (int i=0; i<a; i++)
        	array[i] = Fraction.zero();
		return array;
	}

	public static Fraction[][] new2DArray(int a){
		return new2DArray(a, a);
	}
	public static Fraction[][] new2DArray(int a, int b){
        Fraction array[][] = new Fraction[a][b];
        for (int i=0; i<a; i++)
            for (int j=0; j<b; j++)
            	array[i][j] = Fraction.zero();
		return array;
	}
}
