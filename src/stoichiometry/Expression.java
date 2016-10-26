package stoichiometry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import numbers.Fraction;

public class Expression {
	private final Map<Compound, Integer> compounds;
	private Map<Compound, Double> compoundsMoles;
	private Map<Compound, Double> compoundsGrams;

	public Expression(Map<Compound, Integer> compounds) {
		this.compounds = compounds;
		compoundsMoles = new LinkedHashMap<>();
		compoundsGrams = new LinkedHashMap<>();
		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
			Compound c = entry.getKey();
			compoundsMoles.put(c, 0.0);
			compoundsGrams.put(c, 0.0);
		}
	}
	
	public static List<String> splitPlus(String input){
		List<String> parts = new ArrayList<>();
		String part = "";
		int parens = 0;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch == '+' && parens == 0){
				parts.add(part);
				part = "";
			} else {
				if (ch == '('){
					parens++;
				}
				if (ch == ')'){
					parens--;
				}
				part += ch;
			}
		}
		parts.add(part);
		return parts;
	}
	
	public static Expression fromString(String inputStr) {
//		System.out.println("e Input	" + inputStr);
		
		String noSpacesStr = "";
		for (int i = 0; i < inputStr.length(); i++) {
			char ch = inputStr.charAt(i);
			if (ch != ' ') {
				noSpacesStr += ch;
			}
		}

//		System.out.println("e !Spaces	" + noSpacesStr);
		List<String> compoundsString = splitPlus(noSpacesStr);
//		System.out.println("e list	" + compoundsString);

		Map<Compound, Integer> compounds = new LinkedHashMap<>();
		for (String s : compoundsString) {
			compounds.put(Compound.fromString(s), 1);
		}
		
		return new Expression(compounds);
	}
	
//	private static List<Integer> negative(List<Integer> list) {
//		List<Integer> negative = new ArrayList<>();
//		for (int i : list) {
//			negative.add(-i);
//		}
//		return negative;
//	}
//
	public List<Integer> countFactors(Element e){
		List<Integer> factors = new ArrayList<>();
		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			if (compound.getElements().containsKey(e)){
				factors.add(compound.getElements().get(e));
			} else {
				factors.add(0);
			}
		}
		return factors;
	}
	
	@Override
	public String toString() {
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
				if (quantity != 1) {
					s += quantity + " ";
				}
				s += compound;
			}
		}
		return s;
	}
	
	public static Map<Compound, Fraction> toGrams(Map<Compound, Integer> compounds){
		Map<Compound, Fraction> compoundsGramsFraction = new LinkedHashMap<>();
		for (Map.Entry<Compound, Integer> entry : compounds.entrySet()){
			Compound compound = entry.getKey();
			int quantity = entry.getValue();
			compoundsGramsFraction.put(compound, Fraction.fromDouble(compound.getMolarMass()).multiplySelfBy(quantity));
		}
		return compoundsGramsFraction;
//        int lowestCommonDenominator = Fraction.lowestCommonDenominator(compoundsGramsFraction.values());
//
//		Map<Compound, Integer> compoundsGrams = new LinkedHashMap<>();
//		for (Map.Entry<Compound, Fraction> entry : compoundsGramsFraction.entrySet()){
//			Compound compound = entry.getKey();
//			Fraction quantity = entry.getValue();
//			compoundsGrams.put(compound, quantity.multiplySelfBy(lowestCommonDenominator).getNumerator());
//		}
//		return compoundsGrams;
	}
	
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

//	public void printMolarMasses() {
//		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
//			Compound c = entry.getKey();
//			System.out.print(Fraction.round8(c.getMolarMass()) + "     ");
//		}
//	}
//	public static void printDoubles(Map<Compound, Double> compounds) {
//		for (Map.Entry<Compound, Double> entry: compounds.entrySet()){
////			Compound c = entry.getKey();
//			System.out.print(entry.getValue() + "         ");
//		}
//	}
	
//	public void printInfo() {
//		System.out.println(
//				new ReactionStringTableBuilder()
//				.add().buildCompounds(reactants).add(" => ").buildCompounds(products).newRow()
//				.add("g/mol").buildMolarMasses(reactants).add().buildMolarMasses(products).newRow()
//				.add("mol").buildDoubles(reactantsMoles).add().buildDoubles(productsMoles).newRow()
//				.add("g").buildDoubles(reactantsGrams).add().buildDoubles(productsGrams)
//				.build(2, Align.CENTER)
//				);
//	}

	public Integer getCoefficient(Compound c){
		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
			Compound c2 = entry.getKey();
			if(c.toString().equals(c2.toString())){
				return entry.getValue();
			}
		}

		return null;
	}
	
	public void setMolesPerCoefficient(double molesPerCoefficient){
		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
			Compound c = entry.getKey();
			double m = molesPerCoefficient * entry.getValue();
			compoundsMoles.put(c, m);
			compoundsGrams.put(c, m * c.getMolarMass());
		}
	}

	public void setGramsPerCoefficient(double gramsPerCoefficient){
		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
			Compound c = entry.getKey();
			double m = gramsPerCoefficient * entry.getValue();
			compoundsGrams.put(c, m);
			compoundsMoles.put(c, m / c.getMolarMass());
		}
	}

	public Map<Compound, Integer> getCompounds() {
		return compounds;
	}
	
	public Map<Compound, Double> getCompoundsMoles() {
		return compoundsMoles;
	}

	public Map<Compound, Double> getCompoundsGrams() {
		return compoundsGrams;
	}

	public int size() {
		return compounds.size();
	}

	public Compound getAtIndex(int index) {
		int i = 0;
		for (Map.Entry<Compound, Integer> entry: compounds.entrySet()){
			if (i == index){
				return entry.getKey();
			}
			i++;
		}
		return null;
	}
}
