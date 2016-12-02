package stoichiometry;

import java.util.Scanner;

import numbers.Fraction;
import numbers.Solver;

public class Main {
	public static void main(String[] args){
		Reaction.fromString("H2+O2->H2O").printInfo();
//		Reaction.fromString("Al + I2 -> AlI3").printInfo();
//		Reaction.fromString("NH3 + CO2 → (NH2)2CO + H2O").printInfo();
//		Reaction.fromString("Zn + HCl → ZnCl2 + H2").printInfo();
//		Reaction.fromString("TiCl4 + Mg → Ti + MgCl2").printInfo();
//		Reaction.fromString("NaHCO3 ↔ Na2CO3 + CO2+H2O").printInfo();
//		Reaction.fromString("Na2CO3(aq) + HCl(aq) →   CO2(g) +   NaCl(aq) +   H2O(l)").printInfo();
//	 	Reaction.fromString("AgNO3(aq) + Na2S(aq) → Ag2S(s) + NaNO3(aq)").printInfo();
//		Reaction.fromString("LiBr(aq) + AgNO3(aq) →  LiNO3(aq) + AgBr 		 (s)").printInfo();
//		Reaction.fromString("K2CrO4 (aq) +  Ba(NO3)2(aq) →  K2()(aq) +  (s)").printInfo();
//		Reaction.fromString("NaOH (aq) +  Cu(NO3)2 (aq) →  Cu(OH)2 (s) +  NaNO3 (aq)").printInfo();
//		Reaction.fromString("3 CaI2(aq) + 2 K3PO4(aq) →  Ca3(PO4)2 (s) +  KI (aq)").printInfo();
//		Reaction.fromString("Ag(+) (aq) + CrO4(2-) (aq) → Ag2CrO4 (s)").printInfo();
//		Reaction.fromString("Pb(NO3)2(s) + 2 NaI(aq) → PbI2(aq) + 2 NaNO3(aq)").setMoles("NaNO3(aq)", .70).printInfo();
//		Reaction.fromString("6 NaN3(s) + Fe2O3(s) → 3 Na2O(s) + 2 Fe(s) +  N2(g)").setMoles("N2(g)", 2.93558143672).printInfo();
//		System.out.println(Compound.fromString("B").getMolarMass());
//		Reaction.fromString("2 LiOH(s) + CO2(g) → Li2CO3(s) + H2O(l)").setGrams("LiOH(s)", 367).printInfo();
//		Reaction.fromString("KOH(s) + CO2(g) → K2CO3(s) + H2O(l)").setGrams("KOH(s)", 367).printInfo();
//		Reaction.fromString("H2SO4(aq) + Na2CO3(aq) → CO2(g) + H2O(l) + Na2SO4(aq) ").setMoles("", 1).printInfo();
//		Reaction.fromString("2 HCl(aq) + Na2CO3(aq) → CO2(g) + H2O(l) + 2 NaCl(aq) ").setMoles("", 1).printInfo();
//		Reaction.fromString("C3H5(NO3)3(s) = CO2(g) + H2O(g) + N2(g) + O2(g)").setGrams("C3H5(NO3)3(s)", 310).printInfo();
//		Reaction.fromString("C2H5OH(l) +   O2(g) →  		 CO2(g) +  		 H2O(g)").setGrams("C2H5OH(l)", 5.01).printInfo();
//		Reaction.fromString("").setMoles("", 1).printInfo();
//		Reaction.fromString("").setMoles("", 1).printInfo();
//		Reaction.fromString("").setMoles("", 1).printInfo();
//		Reaction.fromString("").setMoles("", 1).printInfo();
//		Reaction.fromString("").setMoles("", 1).printInfo();
//		Reaction.fromString("N2(g) +  H2(g) →  NH3").printInfo();
//		Reaction.fromString("").printInfo();
//		Reaction.fromString("").printInfo();
//		Reaction.fromString("").printInfo();
//		Reaction.fromString("").printInfo();
//		Reaction.fromString("").printInfo();
//		System.out.println(Compound.fromString("He").getMolarMass());
//		System.out.println(Compound.fromString("N2").getMolarMass());
//		System.out.println(Compound.fromString("Cl2").getMolarMass());
//		System.out.println(Compound.fromString("").getMolarMass());
//		System.out.println(Compound.fromString("").getMolarMass());
//		System.out.println(Compound.fromString("Al").getMolarMass());
//        System.out.println(Compound.fromString("Cu").getMolarMass());
//        System.out.println(Compound.fromString("Ni").getMolarMass());
//		System.out.println(Compound.fromString("NaCl").getMolarMass());
//		System.out.println(Compound.fromString("MgSO4(H2O)7").getMolarMass());
		Reaction.fromString("CO2(g) + H2O(l) ⇆ H3O(+)(aq) + HCO3(-)(aq) ").printInfo();

		
//		System.out.println(new StringTableBuilder().addItem("A1").addItem("A2").addItem("A3").newRow().addItem("B1").addItem("B2").newRow().addItem("C1").build(4));
		
	    /*
	    int i=1;
	    boolean valid = true;
	    String convert = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890()+-";
	    for (Element e : Element.values()){
	        String s = e.name();
	        int a=0, b=0, c=0;
            if (s.length() >= 1){
                a = convert.indexOf(s.charAt(0))+1;
            }
            if (s.length() >= 2){
                b = convert.indexOf(s.charAt(1))+1;
            }
            if (s.length() >= 3){
                c = convert.indexOf(s.charAt(2))+1;
            }
//            System.out.println(a+","+b+","+c+","+e.getAtomicMass()+", #"+s);
            System.out.println(10000*a+100*b+c+","+e.getAtomicMass()+", #"+s);
	        if(i != e.getAtomicNumber()) {
	            valid = false;
	        }
	        i++;
	    }
	    System.out.println(valid);
	    */
	}
	
	public static void solveFromInput(){
      System.out.println("Enter the number of variables in the equations: ");
      Scanner input = new Scanner(System.in);
      int n = input.nextInt();
      System.out.println("Enter the coefficients of each variable for each equations");
      System.out.println("ax + by + cz + ... = d");
      Fraction [][]mat = new Fraction[n][n];
      Fraction [][]constants = new Fraction[n][1];
      //input
      for(int i=0; i<n; i++)
      {
          for(int j=0; j<n; j++)
          {
              mat[i][j] = Fraction.fromString(input.next());
          }
          constants[i][0] = Fraction.fromString(input.next());
      }
      input.close();
      Solver.solve(mat, constants);
	}
}
