package output;

public class StringTable {
	public enum Align {
		LEFT,
		CENTER,
		RIGHT
	}
	
	private final String[][] table;
	private final String tableString;
	private final int[] maxWidths;
	private final String bar;
	private final String spaces;

	public StringTable(String[][] table, int spacing, Align align) {
		this.table = table;
		this.spaces = repeat(" ", spacing);
		maxWidths = new int[table[0].length];
		StringBuilder sb = new StringBuilder();
		for (int x=0; x<table[0].length; x++) {
			int maxWidth = 0;
			for (int y=0; y<table.length; y++) {
				int width = table[y][x].length();
				if (width > maxWidth) {
					maxWidth = width;
				}
			}
			maxWidths[x] = maxWidth;
			sb.append("+").append(repeat("-", maxWidth + 2 * spacing));
		}
		sb.append("+");
		bar = sb.toString();
//		bar = repeat("-", t + (1+2*spacing)*table[0].length + 1);
		
		
		sb = new StringBuilder();
		sb.append(bar).append("\n");
		for (int y=0; y<table.length; y++) {
			for (int x=0; x<table[0].length; x++) {
				sb.append("|").append(spaces);
				if (align == Align.RIGHT){
					sb.append(padLeft(table[y][x], maxWidths[x]));
				} else if (align == Align.LEFT){
					sb.append(padRight(table[y][x], maxWidths[x]));
				} else {
					int extra = maxWidths[x] - table[y][x].length();
					sb.append(padRight(repeat(" ", extra/2) + table[y][x], maxWidths[x]));
				}
				sb.append(spaces);
			}
			sb.append("|\n").append(bar).append("\n");
		}
		tableString = sb.toString();
	}
	
	public String[][] getTable() {
		return table;
	}

	public String getStringAt(int x, int y){
		return table[y][x];
	}

	// pad with " " to the right to the given length (n)
	public static String padRight(String s, int n) {
		if (s == "") return repeat(" ", n);
		return String.format("%1$-" + n + "s", s);
	}

	// pad with " " to the left to the given length (n)
	public static String padLeft(String s, int n) {
		if (s == "") return repeat(" ", n);
		return String.format("%1$" + n + "s", s);
	}
	
	public static String repeat(String s, int n){
		return new String(new char[n]).replace("\0", s);
	}
	
	@Override
	public String toString() {
		return tableString;
	}
}
