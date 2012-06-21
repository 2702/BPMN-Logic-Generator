package pattern;

import graph.Marker;

import java.util.ArrayList;


//TODO zrobic tak zeby czytal z pliku i ladnie obslugiwal na zasadzie {slug1} (moze byc bez klamer)
public class FormulaParser implements FormulaParserInterface{

	@Override
	public String getSequenceFormula(String formula1, String formula2) {
		return "("+formula1+") => <>("+formula2+")";
	}

	@Override
	public String getSplitFormula(ArrayList<String> formulas, Marker marker) {
		if (marker.equals(Marker.EXCLUSIVE_CHOICE))
			return "("+formulas.get(0)+") => (<>("+ formulas.get(1)+") & ~<>("+formulas.get(2)+"))|(~<>("+formulas.get(1)+") & <>("+formulas.get(2)+"))";
		if (marker.equals(Marker.PARALLEL_SPLIT))
			return "("+formulas.get(0)+") => <>("+ formulas.get(1)+") & <>("+formulas.get(2)+")";
		return null;
	}

	@Override
	public String getMergeFormula(ArrayList<String> formulas, Marker marker) {
		if (marker.equals(Marker.PARALLEL_SPLIT))
			return "("+formulas.get(0)+") & ("+ formulas.get(1)+") => <>("+formulas.get(2)+")";
		if (marker.equals(Marker.EXCLUSIVE_CHOICE)) // na razie traktujemy to simple merge'em
			return "("+formulas.get(0)+") | ("+ formulas.get(1)+") => <>("+formulas.get(2)+")";
		return null;
	}

}
