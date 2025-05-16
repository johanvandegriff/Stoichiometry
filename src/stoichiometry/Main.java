package stoichiometry;

import java.util.Scanner;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

import numbers.Fraction;
import numbers.Solver;

public class Main {
    private static String charWhitelist(String s) {
        if (s == null) return "";
        return s.replaceAll("[^\\w .()+\\-=>→↔⇆]", "");
    }

    public static Map<String, String> queryToMap(String query) {
        if (query == null) return null;
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=", 2);
            String key = URLDecoder.decode(entry[0], StandardCharsets.UTF_8);
            String value = entry.length > 1
                ? URLDecoder.decode(entry[1], StandardCharsets.UTF_8)
                : "";
            result.put(key, value);
        }
        return result;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String result = "";
            String equation = "";
            String compound = "";
            String gramsOrMoles = "";
            String gramsOrMolesValue = "";

            try {
                String uri = httpExchange.getRequestURI().toString();
                String query = uri.substring(uri.indexOf("?") + 1);
                // String query = httpExchange.getRequestURI().getQuery();
                Map<String, String> params = queryToMap(query);

                if (params != null && params.containsKey("equation")) {
                    equation = charWhitelist(params.get("equation"));
                    if (params.containsKey("compound")
                        && params.containsKey("grams_or_moles")
                        && params.containsKey("grams_or_moles_value")) {
                        compound = charWhitelist(params.get("compound"));
                        gramsOrMoles = charWhitelist(params.get("grams_or_moles"));
                        gramsOrMolesValue = charWhitelist(params.get("grams_or_moles_value"));
                    }

                    Reaction r = Reaction.fromString(equation).setMoles(0, 1);
                    if (!compound.isEmpty() && !gramsOrMoles.isEmpty() && !gramsOrMolesValue.isEmpty()) {
                        if (gramsOrMoles.equals("g") || gramsOrMoles.equals("grams")) {
                            double grams = Double.valueOf(gramsOrMolesValue);
                            r.setGrams(compound, grams);
                        } else if (gramsOrMoles.equals("m") || gramsOrMoles.equals("moles")) {
                            double moles = Double.valueOf(gramsOrMolesValue);
                            r.setMoles(compound, moles);
                        } else {
                            result += "Input not recognized: " + gramsOrMoles;
                        }
                    }
                    result += r.getInfo();
                }
            } catch (Exception e) {
                System.out.println(e);
                result += "error: " + e.toString();
            }

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">")
                .append("<title>Stoichiometry</title></head><body>")
                .append("<h2>Stoichiometry</h2>")
                .append("<p>Chemical Equation Balancer!</p>")
                .append("<form action=\"\">\n")
                .append("Enter the chemical equation to be balanced:<br />")
                .append("<input required type=\"text\" name=\"equation\" value=\"")
                .append(equation).append("\" style=\"width:400px;\"><br><br>\n")
                .append("<input value=\"Balance It!\" type=\"submit\"><br>\n")
                .append("<h3>Optional Info</h3>\n")
                .append("Enter a compound within the equation to set its grams or moles:<br />")
                .append("<input type=\"text\" name=\"compound\" value=\"")
                .append(compound).append("\"><br><br>\n")
                .append("Enter the quantity and unit of this compound:<br />")
                .append("<input type=\"text\" name=\"grams_or_moles_value\" value=\"")
                .append(gramsOrMolesValue).append("\"> ")
                .append("<select name=\"grams_or_moles\">\n")
                .append("<option value=\"grams\"" + ("grams".equals(gramsOrMoles) ? " selected" : "") + ">grams</option>\n")
                .append("<option value=\"moles\"" + ("moles".equals(gramsOrMoles) ? " selected" : "") + ">moles</option>\n")
                .append("</select><br>\n")
                .append("<input value=\"Balance It!\" type=\"submit\">")
                .append("</form>\n");

            if (result != null && !result.isEmpty()) {
                html.append("<h3>Result</h3><pre>")
                    .append(result)
                    .append("</pre>\n");
            } else {
                html.append("<h3>Samples</h3>\n")
                    .append("<p>Try out some of these sample equations. It balances charge, too!</p>\n")
                    .append("<pre>\n")
                    .append("   <a href=\"?equation=H2%2BO2%3D>H2O\">H2+O2=>H2O</a>\n")
                    .append("   <a href=\"?equation=Fe%28l%29+%2B+H2O%28l%29+%3D>+Fe%282%2B%29%28aq%29+%2B+OH%28-%29%28aq%29+%2B+H2%28g%29\">Fe(l) + H2O(l) => Fe(2+)(aq) + OH(-)(aq) + H2(g)</a>\n")
                    .append("   <a href=\"?equation=C3H6%28OH%292+%2B+O2+->+CO2+%2B+H2O\">C3H6(OH)2 + O2 -> CO2 + H2O</a>\n")
                    .append("   <a href=\"?equation=Al+%2B+I2+->+AlI3\">Al + I2 -> AlI3</a>\n")
                    .append("   <a href=\"?equation=NH3+%2B+CO2+%3D>+%28NH2%292CO+%2B+H2O\">NH3 + CO2 => (NH2)2CO + H2O</a>\n")
                    .append("   <a href=\"?equation=Zn+%2B+HCl+→+ZnCl2+%2B+H2\">Zn + HCl → ZnCl2 + H2</a>\n")
                    .append("   <a href=\"?equation=TiCl4+%2B+Mg+→+Ti+%2B+MgCl2\">TiCl4 + Mg → Ti + MgCl2</a>\n")
                    .append("   <a href=\"?equation=NaHCO3+↔+Na2CO3+%2B+CO2%2BH2O\">NaHCO3 ↔ Na2CO3 + CO2+H2O</a>\n")
                    .append("   <a href=\"?equation=Na2CO3%28aq%29+%2B+HCl%28aq%29+→+++CO2%28g%29+%2B+++NaCl%28aq%29+%2B+++H2O%28l%29\">Na2CO3(aq) + HCl(aq) →   CO2(g) +   NaCl(aq) +   H2O(l)</a>\n")
                    .append("   <a href=\"?equation=LiBr%28aq%29+%2B+AgNO3%28aq%29+→++LiNO3%28aq%29+%2B+AgBr+%09%09+%28s%29\">LiBr(aq) + AgNO3(aq) →  LiNO3(aq) + AgBr 		 (s)</a>\n")
                    .append("   <a href=\"?equation=NaOH+%28aq%29+%2B++Cu%28NO3%292+%28aq%29+→++Cu%28OH%292+%28s%29+%2B++NaNO3+%28aq%29\">NaOH (aq) +  Cu(NO3)2 (aq) →  Cu(OH)2 (s) +  NaNO3 (aq)</a>\n")
                    .append("   <a href=\"?equation=CaI2%28aq%29+%2B+K3PO4%28aq%29+→++Ca3%28PO4%292+%28s%29+%2B++KI+%28aq%29\">CaI2(aq) + K3PO4(aq) →  Ca3(PO4)2 (s) +  KI (aq)</a>\n")
                    .append("   <a href=\"?equation=Ag%28%2B%29+%28aq%29+%2B+CrO4%282-%29+%28aq%29+→+Ag2CrO4+%28s%29\">Ag(+) (aq) + CrO4(2-) (aq) → Ag2CrO4 (s)</a>\n")
                    .append("   <a href=\"?equation=Pb%28NO3%292%28s%29+%2B+NaI%28aq%29+→+PbI2%28aq%29+%2B+NaNO3%28aq%29\">Pb(NO3)2(s) + NaI(aq) → PbI2(aq) + NaNO3(aq)</a>\n")
                    .append("   <a href=\"?equation=NaN3%28s%29+%2B+Fe2O3%28s%29+→+Na2O%28s%29+%2B+Fe%28s%29+%2B++N2%28g%29\">NaN3(s) + Fe2O3(s) → Na2O(s) + Fe(s) +  N2(g)</a>\n")
                    .append("   <a href=\"?equation=LiOH%28s%29+%2B+CO2%28g%29+→+Li2CO3%28s%29+%2B+H2O%28l%29\">LiOH(s) + CO2(g) → Li2CO3(s) + H2O(l)</a>\n")
                    .append("   <a href=\"?equation=KOH%28s%29+%2B+CO2%28g%29+→+K2CO3%28s%29+%2B+H2O%28l%29\">KOH(s) + CO2(g) → K2CO3(s) + H2O(l)</a>\n")
                    .append("   <a href=\"?equation=H2SO4%28aq%29+%2B+Na2CO3%28aq%29+→+CO2%28g%29+%2B+H2O%28l%29+%2B+Na2SO4%28aq%29\">H2SO4(aq) + Na2CO3(aq) → CO2(g) + H2O(l) + Na2SO4(aq)</a>\n")
                    .append("   <a href=\"?equation=HCl%28aq%29+%2B+Na2CO3%28aq%29+→+CO2%28g%29+%2B+H2O%28l%29+%2B+NaCl%28aq%29\">HCl(aq) + Na2CO3(aq) → CO2(g) + H2O(l) + NaCl(aq)</a>\n")
                    .append("   <a href=\"?equation=C3H5%28NO3%293%28s%29+%3D+CO2%28g%29+%2B+H2O%28g%29+%2B+N2%28g%29+%2B+O2%28g%29\">C3H5(NO3)3(s) = CO2(g) + H2O(g) + N2(g) + O2(g)</a>\n")
                    .append("   <a href=\"?equation=C2H5OH%28l%29+%2B+++O2%28g%29+→++%09%09+CO2%28g%29+%2B++%09%09+H2O%28g%29\">C2H5OH(l) +   O2(g) →  		 CO2(g) +  		 H2O(g)</a>\n")
                    .append("   <a href=\"?equation=CO2%28g%29+%2B+H2O%28l%29+⇆+H3O%28%2B%29%28aq%29+%2B+HCO3%28-%29%28aq%29\">CO2(g) + H2O(l) ⇆ H3O(+)(aq) + HCO3(-)(aq)</a>\n")
                    .append("</pre>\n");
            }

            html.append("<h3>Source Code</h3>\n")
                .append("<p>This app is open source! You can see, modify, and distribute the source code.</p>\n")
                .append("<p>You can see the source code for the actual solver (under the <a target=\"_blank\" href=\"https://tldrlegal.com/license/mit-license\">MIT license</a>) on <a target=\"_blank\" href=\"https://codeberg.org/johanvandegriff/Stoichiometry\">codeberg</a>, <a target=\"_blank\" href=\"https://git.sr.ht/~johanvandegriff/Stoichiometry\">sourcehut</a> or <a target=\"_blank\" href=\"https://github.com/johanvandegriff/Stoichiometry\">github</a>.</p>\n");

            html.append("</body></html>");

            byte[] bytes = html.toString().getBytes(StandardCharsets.UTF_8);
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            httpExchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static void myServer(int port, String prefix) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(prefix, new MyHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port " + port);
    }


	public static void main(String[] args) throws IOException {
		if (args.length > 0) {
			if (args[0].equals("--server")) {
                int port;
                String portEnv = System.getenv("PORT");
                try {
                    port = Integer.parseInt(portEnv);
                }
                catch (NumberFormatException e) {
                    port = 8080; //default port
                }
                String prefix = System.getenv("PREFIX"); // e.g. "/chem"
                if (prefix == null) {
                    prefix = "";
                }
                if (!prefix.startsWith("/")) {
                    prefix = "/" + prefix;
                }
				myServer(port, prefix);
			} else {
				String equation = args[0];
				Reaction r = Reaction.fromString(equation).setMoles(0, 1);
				if (args.length > 3) {
					String compound = args[1];
					String gOrM = args[3];
					if (gOrM.equals("g") || gOrM.equals("grams")) {
						double grams = Double.valueOf(args[2]);
						r.setGrams(compound, grams);
					} else if (gOrM.equals("m") || gOrM.equals("moles")) {
						double moles = Double.valueOf(args[2]);
						r.setMoles(compound, moles);
					} else {
						System.out.println("Input not recognized: " + gOrM);
					}
				}
				r.printInfo();
			}
		} else {
			System.out.print("Enter the chemical equation: ");
			Scanner input = new Scanner(System.in);
			Reaction r = Reaction.fromString(input.nextLine()).setMoles(0, 1);
			r.printInfo();
			System.out.print("Which compound do you want to set the amount? ");
			String compound = input.nextLine();
			System.out.print("Do you want to set the grams or moles? (enter \"g\" or \"m\"): ");
			String gOrM = input.nextLine();
			if (gOrM.equals("g") || gOrM.equals("grams")) {
				System.out.print("Enter the number of grams: ");
				double grams = input.nextDouble();
				r.setGrams(compound, grams);
				r.printInfo();
			} else if (gOrM.equals("m") || gOrM.equals("moles")) {
				System.out.print("Enter the number of moles: ");
				double moles = input.nextDouble();
				r.setMoles(compound, moles);
				r.printInfo();
			} else {
				System.out.println("Input not recognized: " + gOrM);
			}
			input.close();
		}
//        Reaction.fromString("H2 + O2 => H2O").printInfo();
//        Reaction.fromString("Fe(l) + H2O(l) => Fe(2+)(aq) + OH(-)(aq) + H2(g)").printInfo();
//        Reaction.fromString("C3H6(OH)2 + O2 -> CO2 + H2O").printInfo();
//        Reaction.fromString("H(+) + Cl(-) -> HCl").printInfo();
//        Reaction.fromString("2NO2+NO3+CO => NO3+NO+CO2+NO2").printInfo();
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
//		Reaction.fromString("CO2(g) + H2O(l) ⇆ H3O(+)(aq) + HCO3(-)(aq) ").printInfo();

//      System.out.println(Compound.fromString("NaOH").getMolarMass());
		
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
