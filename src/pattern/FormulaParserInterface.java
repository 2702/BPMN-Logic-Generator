package pattern;

import graph.Marker;

import java.util.ArrayList;

// intefejs klasy parsujacej plik wejsciowy z formulami
public interface FormulaParserInterface {
	public String getSequenceFormula(String formula1, String formula2);
	String getSplitFormula(ArrayList<String> formulas, Marker marker);//troche lamersko ale coz
	String getMergeFormula(ArrayList<String> formulas, Marker marker);//troche lamersko ale coz
}
