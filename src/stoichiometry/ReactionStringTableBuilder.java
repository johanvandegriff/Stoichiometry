package stoichiometry;

import java.util.Map;

import numbers.Fraction;
import output.StringTableBuilder;

public class ReactionStringTableBuilder extends StringTableBuilder{
	
	
	@Override
	public ReactionStringTableBuilder add(Object item) {
		super.add(item);
		return this;
	}

	@Override
	public ReactionStringTableBuilder add() {
		super.add();
		return this;
	}

	@Override
	public ReactionStringTableBuilder newRow() {
		super.newRow();
		return this;
	}

	public ReactionStringTableBuilder buildCompounds(Expression e){
		for (Map.Entry<Compound, Integer> entry : e.getCompounds().entrySet()){
			Compound compound = entry.getKey();
			int quantity = entry.getValue();
			if (quantity != 0) {
				if (quantity != 1) {
					add(quantity + " " + compound);
				} else {
					add(compound);
				}
			}
		}
		return this;
	}
	
	public ReactionStringTableBuilder buildMolarMasses(Expression e) {
		for (Map.Entry<Compound, Integer> entry: e.getCompounds().entrySet()){
			add(Fraction.round8(entry.getKey().getMolarMass()));
		}
		return this;
	}

	public ReactionStringTableBuilder buildMoles(Expression e) {
		for (Map.Entry<Compound, Double> entry: e.getCompoundsMoles().entrySet()){
			add(Fraction.round8(entry.getValue()));
		}
		return this;
	}
	public ReactionStringTableBuilder buildGrams(Expression e) {
		for (Map.Entry<Compound, Double> entry: e.getCompoundsGrams().entrySet()){
			add(Fraction.round8(entry.getValue()));
		}
		return this;
	}

}
