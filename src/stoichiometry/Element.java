package stoichiometry;

public enum Element {
	H("Hydrogen", 1, 1.008),
	He("Helium", 2, 4.003),
	Li("Lithium", 3, 6.941),
	Be("Beryllium", 4, 9.012),
	B("Boron", 5, 10.811),
	C("Carbon", 6, 12.011),
	N("Nitrogen", 7, 14.007),
	O("Oxygen", 8, 15.999),
	F("Fluorine", 9, 18.998),
	Ne("Neon", 10, 20.180),
	Na("Sodium", 11, 22.990),
	Mg("Magnesium", 12, 24.305),
	Al("Aluminum,", 13, 26.982),
	Si("Silicon", 14, 28.096),
	P("Phosphorus", 15, 30.974),
	S("Sulfur", 16, 32.066),
	Cl("Chlorine", 17, 35.453),
	Ar("Argon", 18, 39.948),
	K("Potassium", 19, 39.098),
	Ca("Calcium", 20, 40.078),
	Sc("Scandium", 21, 44.956),
	Ti("Titanium", 22, 47.867),
	V("Vanadium", 23, 50.942),
	Cr("Chromium", 24, 51.996),
	Mn("Manganese", 25, 54.938),
	Fe("Iron", 26, 55.845),
	Co("Cobalt", 27, 58.933),
	Ni("Nickel", 28, 58.693),
	Cu("Copper", 29, 63.546),
	Zn("Zinc", 30, 65.38),
	Ga("Gallium", 31, 69.723),
	Ge("Germanium", 32, 72.631),
	As("Arsenic", 33, 74.922),
	Se("Selenium", 34, 78.971),
	Br("Bromine", 35, 79.904),
	Kr("Krypton", 36, 84.798),
	Rb("Rubidium", 37, 84.468),
	Sr("Strontium", 38, 87.62),
	Y("Yttrium", 39, 88.906),
	Zr("Zirconium", 40, 91.224),
	Nb("Niobium", 41, 92.906),
	Mo("Molybdenum", 42, 95.95),
	Tc("Technetium", 43, 98.907),
	Ru("Ruthenium", 44, 101.07),
	Rh("Rhodium", 45, 102.906),
	Pd("Palladium", 46, 106.42),
	Ag("Silver", 47, 107.868),
	Cd("Cadmium", 48, 112.414),
	In("Indium", 49, 114.818),
	Sn("Tin", 50, 118.711),
	Sb("Antimony", 51, 121.760),
	Te("Tellurium", 52, 127.6),
	I("Iodine", 53, 126.904),
	Xe("Xenon", 54, 131.294),
	Cs("Cesium", 55, 132.905),
	Ba("Barium", 56, 137.328),
	La("Lanthanum", 57, 138.905),
	Ce("Cerium", 58, 140.116),
	Pr("Praseodymium", 59, 140.908),
	Nd("Neodymium", 60, 144.243),
	Pm("Promethium", 61, 144.913),
	Sm("Samarium", 62, 150.36),
	Eu("Europium", 63, 151.964),
	Gd("Gadolinium", 64, 157.25),
	Tb("Terbium", 65, 158.925),
	Dy("Dysprosium", 66, 162.500),
	Ho("Holmium", 67, 164.930),
	Er("Erbium", 68, 167.259),
	Tm("Thulium", 69, 168.934),
	Yb("Ytterbium", 70, 173.055),
	Lu("Lutetium", 71, 174.967),
	Hf("Hafnium", 72, 178.49),
	Ta("Tantalum", 73, 180.948),
	W("Tungsten", 74, 183.84),
	Re("Rhenium", 75, 186.207),
	Os("Osmium", 76, 190.23),
	Ir("Iridium", 77, 192.217),
	Pt("Platinum", 78, 195.085),
	Au("Gold", 79, 196.967),
	Hg("Mercury", 80, 200.592),
	Tl("Thallium", 81, 204.383),
	Pb("Lead", 82, 207.2),
	Bi("Bismuth", 83, 208.980),
	Po("Polonium", 84, 208.982),
	At("Astatine", 85, 209.987),
	Rn("Radon", 86, 222.018),
	Fr("Francium", 87, 223.020),
	Ra("Radium", 88, 226.025),
	Ac("Actinium", 89, 227.028),
	Th("Thorium", 90, 232.038),
	Pa("Protactinium", 91, 231.036),
	U("Uranium", 92, 238.029),
	Np("Neptunium", 93, 237.048),
	Pu("Plutonium", 94, 244.064),
	Am("Americium", 95, 243.061),
	Cm("Curium", 96, 247.070),
	Bk("Berkelium", 97, 247.070),
	Cf("Californium", 98, 247.070),
	Es("Einsteinium", 99, 251.080),
	Fm("Fermium", 100, 257.095),
	Md("Mendelevium", 101, 258.1),
	No("Nobelium", 102, 259.101),
	Lr("Lawrencium", 103, 262),
	Rf("Rutherfordium", 104, 261),
	Db("Dubnium", 105, 262),
	Sg("Seaborgium", 106, 266),
	Bh("Bohrium", 107, 264),
	Hs("Hassium", 108, 269),
	Mt("Meitnerium", 109, 268),
	Ds("Darmstadtium", 110, 269),
	Rg("Roentgenium", 111, 272),
	Cn("Copernicium", 112, 277),
	Uut("Ununtrium", 113, 284),
	Fl("Flerovium", 114, 289),
	Uup("Ununpentium", 115, 288),
	Lv("Livermorium", 116, 298),
	Uus("Ununseptium", 117, 294),
	Uuo("Ununoctium", 118, 294);
	
	public static final double ELECTRON_ATOMIC_MASS = 5.48579909070e-4;

	private final String fullName;
	private final int atomicNumber;
	private final double atomicMass;

	private Element(String fullName, int atomicNumber, double atomicMass){
		this.fullName = fullName;
		this.atomicNumber = atomicNumber;
		this.atomicMass = atomicMass;
	}

	public String getFullName() {
		return fullName;
	}

	public int getAtomicNumber() {
		return atomicNumber;
	}

	public double getAtomicMass() {
		return atomicMass;
	}
}