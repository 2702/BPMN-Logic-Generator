package pattern;

import java.util.ArrayList;

public class PatternManager {
	ArrayList<PatternHandler> handlerList;
	
	
	public PatternManager(){
		this.handlerList = new ArrayList<PatternHandler>();
	}
	
	public void addPatternHandler (PatternHandler handler){
		this.handlerList.add(handler);
	}
	
	// w domysle ma zwracac ilosc zmian
	public int findAndReplacePatterns(){
		int count =0;
		for (PatternHandler handler : handlerList){
			if (handler.process() > 0) 
				count++;
		}
		return count;
	}
	
}
