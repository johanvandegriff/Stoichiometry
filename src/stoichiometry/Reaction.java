package stoichiometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;

import numbers.Fraction;
import numbers.Solver;
import output.StringTable.Align;

public class Reaction {
	private final Expression reactants, products;

	public Reaction(Expression reactants, Expression products) {
		this.reactants = reactants;
		this.products = products;
	}
	
//	public static List<String> splitPlus(String input){
//		List<String> parts = new ArrayList<>();
//		String part = "";
//		int parens = 0;
//		for (int i = 0; i < input.length(); i++) {
//			char ch = input.charAt(i);
//			if (ch == '+' && parens == 0){
//				parts.add(part);
//				part = "";
//			} else {
//				if (ch == '('){
//					parens++;
//				}
//				if (ch == ')'){
//					parens--;
//				}
//				part += ch;
//			}
//		}
//		parts.add(part);
//		return parts;
//	}
	
	public static Reaction fromString(String reaction) {
//		System.out.println("r Input	" + reaction);
		
//		String reaction2 = "";
//		for (int i = 0; i < reaction.length(); i++) {
//			char ch = reaction.charAt(i);
//			if (ch != ' ') {
//				reaction2 += ch;
//			}
//		}
		//System.out.println("!Spaces	" + reaction2);

		String[] leftRight = reaction.split("->|=>|<-|<=|<|>|<<|>>|<>|<->|<=>|=|←|→|↔|«|»|⇆");
		
		//System.out.println("Sides	" + Arrays.asList(leftRight));
		
//		List<String> reactantsString = splitPlus(leftRight[0]); //Arrays.asList(leftRight[0].split("\\+"));
//		List<String> productsString = splitPlus(leftRight[1]); //Arrays.asList(leftRight[1].split("\\+"));

//		Map<Compound, Integer> reactants = new LinkedHashMap<>();
//		Map<Compound, Integer> products = new LinkedHashMap<>();
//		for (String s : reactantsString) {
//			reactants.put(Compound.fromString(s), 1);
//		}
//		for (String s : productsString) {
//			products.put(Compound.fromString(s), 1);
//		}

		Expression reactants = Expression.fromString(leftRight[0]);
		Expression products = Expression.fromString(leftRight[1]);
		
//		System.out.println("Unsolved	" + reactants + " => " + products);

		Map<Compound, Integer> all = new LinkedHashMap<>();
		all.putAll(reactants.getCompounds());
		all.putAll(products.getCompounds());

		Set<Element> elements = new HashSet<>();
		for (Map.Entry<Compound, Integer> entry : all.entrySet()){
			Compound compound = entry.getKey();
			for (Entry<Element, Integer> entry1 : compound.getElements().entrySet()) {
				elements.add(entry1.getKey());
			}
		}
		
//		System.out.println("Elements	" + elements);
		
		Fraction [][]mat = Fraction.new2DArray(1+elements.size(), reactants.size() + products.size());
		mat[0][0] = Fraction.integer(1);
//		for (int i=0; i<mat.length; i++){
//			for (int j=0; j<mat[0].length; j++)
//				System.out.print(mat[i][j] + " ");
//			System.out.println();
//		}
		int i = 0;
//		System.out.println("[1, 0, ... 0] = [1]");
		for (Element e : elements) {
//			List<Integer> factors = countFactors(e, reactants);
//			factors.addAll(negative(countFactors(e, products)));
			List<Integer> factors = reactants.countFactors(e);
			factors.addAll(negative(products.countFactors(e)));
//			System.out.println(factors + " = [0] " + e);
			for (int j=0; j<factors.size(); j++) {
				mat[i+1][j] = Fraction.integer(factors.get(j));
			}
			i++;
		}
		

//		System.out.println(reactants);
//		System.out.println(products);
//		System.out.println(elements);

		Fraction [][]constants = Fraction.new2DArray(1+elements.size(), 1);
		constants[0][0] = Fraction.integer(1);

		
		
        List<Fraction> coefficients = Solver.solve(mat, constants);
        //System.out.println(coefficients);
        
        int lowestCommonDenominator = Fraction.lowestCommonDenominator(coefficients);
//        for (Fraction c : coefficients) {
//        	lowestCommonDenominator *= c.getDenominator();
//        }
        
//    	System.out.println(lowestCommonDenominator);
//        System.out.println(coefficients);
        
        Iterator<Fraction> it1 = coefficients.iterator();
        Iterator<Map.Entry<Compound, Integer>> it2 = reactants.getCompounds().entrySet().iterator();
        Iterator<Map.Entry<Compound, Integer>> it3 = products.getCompounds().entrySet().iterator();
        
        
        while (it1.hasNext()) {
//        	System.out.print(a);
        	int coefficient = it1.next().multiplySelfBy(lowestCommonDenominator).getNumerator();
//        	System.out.println(coefficient);
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
		return new Reaction(reactants, products); //, false);
	}
	
	private static List<Integer> negative(List<Integer> list) {
		List<Integer> negative = new ArrayList<>();
		for (int i : list) {
			negative.add(-i);
		}
		return negative;
	}

//	private static List<Integer> countFactors(Element e, Map<Compound, Integer> compounds){
//		List<Integer> factors = new ArrayList<>();
//		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
//			Compound compound = entry.getKey();
//			if (compound.getElements().containsKey(e)){
//				factors.add(compound.getElements().get(e));
//			} else {
//				factors.add(0);
//			}
//		}
//		return factors;
//	}

	public Expression getReactants() {
		return reactants;
	}

	public Expression getProducts() {
		return products;
	}
	
	public static String getCompoundsString(Map<Compound, Integer> compounds, boolean grams){
		//TODO use a StringBuilder for Reaction.getCompoundsString()
		String s = "";
		boolean first = true;
		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			int quantity = entry.getValue();
			if (quantity != 0) {
				if (first) {
					first = false;
				} else {
					s += " + ";
				}
				if (quantity != 1 || grams) {
					if (grams) {
						s += quantity + "g ";
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
		return reactants + " => " + products;
	}
	
//	public static Map<Compound, Fraction> toGrams(Map<Compound, Integer> compounds){
//		Map<Compound, Fraction> compoundsGramsFraction = new LinkedHashMap<>();
//		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
//			Compound compound = entry.getKey();
//			int quantity = entry.getValue();
//			compoundsGramsFraction.put(compound, Fraction.fromDouble(compound.getMolarMass()).multiplySelfBy(quantity));
//		}
//		return compoundsGramsFraction;
////        int lowestCommonDenominator = Fraction.lowestCommonDenominator(compoundsGramsFraction.values());
////
////		Map<Compound, Integer> compoundsGrams = new LinkedHashMap<>();
////		for (Map.Entry<Compound, Fraction> entry : compoundsGramsFraction.entrySet()){
////			Compound compound = entry.getKey();
////			Fraction quantity = entry.getValue();
////			compoundsGrams.put(compound, quantity.multiplySelfBy(lowestCommonDenominator).getNumerator());
////		}
////		return compoundsGrams;
//	}
	
//	private static Map<Compound, Integer> multiplyCompoundsByLCD(Map<Compound, Fraction> compounds, int lcd){
//		Map<Compound, Integer> compoundsScaled = new LinkedHashMap<>();
//		for (Map.Entry<Compound, Fraction> entry : compounds.entrySet()){
//			Compound compound = entry.getKey();
//			Fraction quantity = entry.getValue();
//			compoundsScaled.put(compound, quantity.multiplySelfBy(lcd).getNumerator());
//		}
//		return compoundsScaled;
//	}
	
//	public Reaction toGrams() {
//		Map<Compound, Fraction> reactantsFraction = toGrams(this.reactants);
//		Map<Compound, Fraction> productsFraction = toGrams(this.products);
//		
////		System.out.println(reactantsFraction + " => " + productsFraction);
//		
//		Map<Compound, Fraction> all = new HashMap<>();
//		all.putAll(reactantsFraction);
//		all.putAll(productsFraction);
//		int lcd = Fraction.lowestCommonDenominator(all.values());
////		System.out.println(lcd);
//		
//		Map<Compound, Integer> reactants = multiplyCompoundsByLCD(reactantsFraction, lcd);
//		Map<Compound, Integer> products = multiplyCompoundsByLCD(productsFraction, lcd);
//		
//		return new Reaction(reactants, products, true);
//	}

	public static void printMolarMasses(Map<Compound, Integer> compounds) {
		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
			Compound c = entry.getKey();
			System.out.print(Fraction.round8(c.getMolarMass()) + "     ");
		}
	}
	public static void printDoubles(Map<Compound, Double> compounds) {
		for (Map.Entry<Compound, Double> entry: compounds.entrySet()){
//			Compound c = entry.getKey();
			System.out.print(entry.getValue() + "         ");
		}
	}
	
	public void printInfo() {
		System.out.println(
				new ReactionStringTableBuilder()
				.add().buildCompounds(reactants).add(" => ").buildCompounds(products).newRow()
				.add("g/mol").buildMolarMasses(reactants).add().buildMolarMasses(products).newRow()
				.add("mol").buildMoles(reactants).add().buildMoles(products).newRow()
				.add("g").buildGrams(reactants).add().buildGrams(products)
				.build(2, Align.CENTER)
				);
	}

	public int getCoefficient(Compound c){
		Integer coefficient = reactants.getCoefficient(c);
		if (coefficient == null){
			coefficient = products.getCoefficient(c);
		}
		if (coefficient == null) {
			throw new InvalidKeyException("Compound \""+c+"\" not contained in the reaction.");
		}
		return coefficient;
	}
	
	public Reaction setMoles(String s, double moles){
		return setMoles(Compound.fromString(s), moles);
	}

	public Reaction setGrams(String s, double grams){
		return setGrams(Compound.fromString(s), grams);
	}
	
	public Compound getAtIndex(int index){
		if(index < reactants.size()){
			return reactants.getAtIndex(index);
		} else if(index-reactants.size() < products.size()){
			return products.getAtIndex(index-reactants.size()); 
		} else {
			return null;
		}
	}
	
	public Reaction setMoles(int i, int moles) {
		Compound compound = getAtIndex(i);
		if (compound == null){
			System.out.println("Index out of range.");
			return this;
		} else {
			return setMoles(compound, moles);
		}
	}

	public Reaction setGrams(int i, double grams) {
		Compound compound = getAtIndex(i);
		if (compound == null){
			System.out.println("Index out of range.");
			return this;
		} else {
			return setGrams(compound, grams);
		}
	}
	
	public Reaction setMoles(Compound compound, double moles){
		double molesPerCoefficient;
		try {
			molesPerCoefficient = moles / getCoefficient(compound);
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			return this;
		}
		reactants.setMolesPerCoefficient(molesPerCoefficient);
		products.setMolesPerCoefficient(molesPerCoefficient);
		return this;
	}

	public Reaction setGrams(Compound compound, double grams){
		return setMoles(compound, grams / compound.getMolarMass());
	}
}
