package stoichiometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import numbers.Fraction;
import numbers.Solver;

public class ReactionOld {
	private final Map<Compound, Double> reactants, products;
	private final boolean grams;

	public ReactionOld(Map<Compound, Double> reactants, Map<Compound, Double> products, boolean grams) {
		this.reactants = reactants;
		this.products = products;
		this.grams = grams;
	}
	
	public static ReactionOld fromString(String reaction) {
		//System.out.println("Input	" + reaction);
		
		String reaction2 = "";
		for (int i = 0; i < reaction.length(); i++) {
			char ch = reaction.charAt(i);
			if (ch != ' ') {
				reaction2 += ch;
			}
		}
		//System.out.println("!Spaces	" + reaction2);

		String[] leftRight = reaction2.split("->|=>|→");
		
		//System.out.println("Sides	" + Arrays.asList(leftRight));
		
		List<String> reactantsString = Arrays.asList(leftRight[0].split("\\+"));
		List<String> productsString = Arrays.asList(leftRight[1].split("\\+"));

		Map<Compound, Double> reactants = new LinkedHashMap<>();
		Map<Compound, Double> products = new LinkedHashMap<>();
		for (String s : reactantsString) {
			reactants.put(Compound.fromString(s), 1.0);
		}
		for (String s : productsString) {
			products.put(Compound.fromString(s), 1.0);
		}

//		System.out.println("Unsolved	" + reactants + " => " + products);

		Map<Compound, Double> all = new HashMap<>();
		all.putAll(reactants);
		all.putAll(products);

		Set<Element> elements = new HashSet<>();
		for (Map.Entry<Compound, Double> entry : all.entrySet()){
			Compound compound = entry.getKey();
			for (Entry<Element, Integer> entry1 : compound.getElements().entrySet()) {
				elements.add(entry1.getKey());
			}
		}
		
//		System.out.println("Elements	" + elements);
		
		Fraction [][]mat = Fraction.new2DArray(1+elements.size());
		mat[0][0] = Fraction.integer(1);
//		for (int i=0; i<mat.length; i++){
//			for (int j=0; j<mat[0].length; j++)
//				System.out.print(mat[i][j] + " ");
//			System.out.println();
//		}
		int i = 0;
//		System.out.println("[1, 0, 0] = [1]");
		for (Element e : elements) {
			List<Integer> factors = countFactors(e, reactants);
			factors.addAll(negative(countFactors(e, products)));
//			System.out.println(factors + " = [0] " + e);
			for (int j=0; j<factors.size(); j++) {
				mat[i+1][j] = Fraction.integer(factors.get(j));
			}
			i++;
		}

		Fraction [][]constants = Fraction.new2DArray(1+elements.size(), 1);
		constants[0][0] = Fraction.integer(1);
        List<Fraction> coefficients = Solver.solve(mat, constants);
        //System.out.println(coefficients);
        
        int lowestCommonDenominator = Fraction.lowestCommonDenominator(coefficients);
//        for (Fraction c : coefficients) {
//        	lowestCommonDenominator *= c.getDenominator();
//        }
        
//    	System.out.println(lowestCommonDenominator);

        //System.out.println(coefficients);
        
        Iterator<Fraction> it1 = coefficients.iterator();
        Iterator<Map.Entry<Compound, Double>> it2 = reactants.entrySet().iterator();
        Iterator<Map.Entry<Compound, Double>> it3 = products.entrySet().iterator();
        
        
        while (it1.hasNext()) {
//        	System.out.print(a);
        	double coefficient = it1.next().multiplySelfBy(new Fraction(lowestCommonDenominator, 1)).getNumerator();
        	//System.out.println(coefficient);
        	if (it2.hasNext()) {
        		it2.next().setValue(coefficient);
        	} else if (it3.hasNext()){
        		it3.next().setValue(coefficient);
        	}
        }
        
//        
//        int j = 0;
//        for ( entry : reactants.entrySet()){
//        	
//        	j++;
//        }
//        for (int j=0; j<coefficients.size(); j++) {
//        	if (j < reactants.size()) {
//        		reactants.set();
//        	} else {
//        		
//        	}
//        }
		//System.out.println("Solved		" + reactants + " => " + products);
		return new ReactionOld(reactants, products, false);
	}
	
	private static List<Integer> negative(List<Integer> list) {
		List<Integer> negative = new ArrayList<>();
		for (int i : list) {
			negative.add(-i);
		}
		return negative;
	}

	private static List<Integer> countFactors(Element e, Map<Compound, Double> compounds){
		List<Integer> factors = new ArrayList<>();
		for (Map.Entry<Compound, Double> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			if (compound.getElements().containsKey(e)){
				factors.add(compound.getElements().get(e));
			} else {
				factors.add(0);
			}
		}
		return factors;
	}

	public Map<Compound, Double> getReactants() {
		return reactants;
	}

	public Map<Compound, Double> getProducts() {
		return products;
	}
	
	public boolean isGrams() {
		return grams;
	}
	
	public static String getCompoundsString(Map<Compound, Double> compounds, boolean grams){
		String s = "";
		boolean first = true;
		for (Map.Entry<Compound, Double> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			double quantity = entry.getValue();
			if (quantity != 0) {
				if (first) {
					first = false;
				} else {
					s += " + ";
				}
				if (quantity != 1 || grams) {
					if (grams) {
						s += quantity + " g ";
					} else {
						s += quantity + " ";
					}
				}
				s += compound;
			}
		}
		return s;
	}
	
	@Override
	public String toString() {
		return getCompoundsString(reactants, grams) + " => " + getCompoundsString(products, grams);
	}
	
	
	public static Map<Compound, Double> toGrams(Map<Compound, Double> compounds){
		Map<Compound, Double> compoundsGrams = new LinkedHashMap<>();
		for (Map.Entry<Compound, Double> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			double quantity = entry.getValue();
			compoundsGrams.put(compound, quantity * compound.getMolarMass());
		}
		return compoundsGrams;
	}
	
	public ReactionOld toGrams() {
		return new ReactionOld(toGrams(reactants), toGrams(products), true);
	}
}
