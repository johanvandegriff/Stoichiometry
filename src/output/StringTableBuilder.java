package output;

import java.util.ArrayList;
import java.util.List;

import output.StringTable.Align;

public class StringTableBuilder {
	private List<List<String>> table = new ArrayList<>();
	private List<String> row = new ArrayList<>();

	public StringTableBuilder add(Object item){
		row.add(item.toString());
		return this;
	}
	public StringTableBuilder add(){
		row.add("");
		return this;
	}
	public StringTableBuilder newRow(){
		table.add(row);
		row = new ArrayList<>();
		return this;
	}
	
	public StringTable build(int spacing, Align align){
		table.add(row);
		
		int w = 0;
		for (List<String> row : table){
			if (row.size() > w){
				w = row.size();
			}
		}
		int h = table.size();
		
		String[][] array = new String[h][w];
		for(int y=0; y < h; y++) {
			for(int x=0; x < w; x++) {
				try {
					array[y][x] = table.get(y).get(x);
				} catch (IndexOutOfBoundsException e) {
					array[y][x] = ""; 
				}
			}
		}
		
		return new StringTable(array, spacing, align);
	}

}
