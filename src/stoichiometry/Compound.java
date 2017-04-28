package stoichiometry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Compound {
	private final Map<Element, Integer> elements;
	private final String displayString;
	private final int charge;

	public Compound(Map<Element, Integer> elements, String displayString, int charge) {
		this.elements = elements;
		this.displayString = displayString;
		this.charge = charge;
	}

	public static Compound fromString(String compound) {
//		System.out.println("c Input	" + compound);
	    String displayString = compound.replaceAll("\\s+","");
	    
		List<String> parts = new ArrayList<>();

		// loop through the chars of the compound String
		char prev = ' ';
		String part = "";
		for (int i = 0; i < compound.length(); i++) {
			char ch = compound.charAt(i);
			if (Character.isLetter(ch)) {
				if (Character.isLetter(prev) && Character.isLowerCase(ch)) {
					part += ch;
				} else {
					parts.add(part);
					part = "" + ch;
				}
			} else if (Character.isDigit(ch)) {
				if (Character.isDigit(prev)) {
					part += ch;
				} else {
					parts.add(part);
					part = "" + ch;
				}
			} else if (ch == '(' || ch == ')' || ch == '+' || ch == '-') {
				parts.add(part);
				part = "" + ch;
			}
			prev = ch;
		}

		parts.add(part);
		parts.remove(0);

		//System.out.println("Split	" + parts);

//		String stateOfMatter = "";
		int charge = 0;
		
		int i = 0;
		while (i < parts.size()) {
			String p = parts.get(i);
			if (p.equals("(")) {
				int startI = i;
				List<String> subList = new ArrayList<>();
				while (!p.equals(")")) {
					p = parts.get(i);
					subList.add(p);
					i++;
				}
				subList.remove(0);
				subList.remove(subList.size() - 1);
				int quantity = 1;
				if (i < parts.size()){
					try {
						quantity = Integer.valueOf(parts.get(i));
						parts.remove(i);
						i--;
						parts.remove(i);
					} catch (NumberFormatException e) {
						
					}
				}
				if (subList.size() == 1){
					String s = subList.get(0);
					if(s.equals("s") || s.equals("l") || s.equals("g") || s.equals("aq")){
//						stateOfMatter = s;
						quantity = 0;
					} else
					if(s.equals("-")){
						charge = -1;
						quantity = 0;
					} else
					if(s.equals("+")){
						charge = 1;
						quantity = 0;
					}
				}
				if (subList.size() == 2){
					String c = subList.get(0);
					String s = subList.get(1);
					if(s.equals("-")){
						charge = -Integer.valueOf(c);
						quantity = 0;
					} else
					if(s.equals("+")){
						charge = Integer.valueOf(c);
						quantity = 0;
					}
				}
//				System.out.println(subList+" * "+quantity);

//				System.out.println("... "+startI + " " + parts);
				for (int j = 0; j < i - startI; j++) {
					parts.remove(startI);
				}
//				System.out.println("... "+i + " " + parts);

				for (int z = 0; z < quantity; z++) {
					for (int j = 0; j < subList.size(); j++) {
						parts.add(startI + j, subList.get(j));
					}
				}
				i = startI-1;

			}
			i++;
//			System.out.println(i + " " + parts);
		}

		//System.out.println("Simple	" + parts);

		// List<ElementQuantity> elements = new ArrayList<>();
		Map<Element, Integer> elements = new LinkedHashMap<>();

		i = 0;
		while (i < parts.size()) {
			String p = parts.get(i);
			char ch = p.charAt(0);
			if (Character.isLetter(ch)) {
				int quantity = 1;
				if (i + 1 < parts.size()) {
					i++;
					String p2 = parts.get(i);
					char ch2 = p2.charAt(0);
					if (Character.isDigit(ch2)) {
						quantity = Integer.valueOf(p2);
					} else {
						i--;
					}
				}
				Element e = Element.valueOf(p);
				int q = 0;
				if (elements.containsKey(e)) {
					q = elements.get(e);
				}
				elements.put(e, q + quantity);
			}
			i++;
		}

		//System.out.println("Final	" + elements);
		return new Compound(elements, displayString, charge);
	}

	public Map<Element, Integer> getElements() {
		return elements;
	}
	
	public String chargeString(){
	    if (charge < -1) {
            return "(" + -charge + "-)";
	    } else if (charge == -1) {
            return "(-)";
        } else if (charge == 1) {
            return "(+)";
        } else if (charge > 1) {
            return "(" + charge + "+)";
        } else { //charge == 0
	        return "";
	    }
	}
	
	@Override
	public String toString() {
	    return displayString;
//		String s = "";
//		for (Map.Entry<Element, Integer> entry : elements.entrySet()){
//			Element e = entry.getKey();
//			int q = entry.getValue();
//			if (q == 1) {
//				s += e;
//			} else if (q != 0){
//				s += "" + e + q;
//			}
//		}
//		s += chargeString();
//		if (displayString != "") {
//			s += "(" + displayString + ")";
//		}
//		return s;
	}

	public double getMolarMass() {
		double m = 0;
		for (Map.Entry<Element, Integer> entry : elements.entrySet()){
			Element e = entry.getKey();
			int q = entry.getValue();
			m += e.getAtomicMass() * q;
		}
		return m - charge * Element.ELECTRON_ATOMIC_MASS; //account for the mass of electrons
	}

    public int getCharge() {
        return charge;
    }
}
