package graph;


import java.util.ArrayList;
import java.util.Stack;

class MarkerHolder{
	Marker marker;
	public MarkerHolder(Marker marker){
		this.marker = marker;
	}
}

public class Node {

	boolean isInDeadDiamond = false; // sprawdza czy wezel nalezy do deaddiamonda
	
	public boolean isInDeadDiamond() {
		return isInDeadDiamond;
	}

	public void setInDeadDiamond(boolean isInDeadDiamond) {
		this.isInDeadDiamond = isInDeadDiamond;
	}


	Marker marker; // enum oznaczajacy dopasowanie danego wezla do wzorca
	private String id = ""; // id wezla nie majace nic wspolnego z zawartoscia pola formula.
			// potrzebne tylko do sprawnych operacji na grafie
	String formula = ""; // formula jaka przechowuje dany wezel. na poczatku to
					// bedzie identyfikator pobrany z xmla, a potem cos bardziej
					// zlozonego

//	Stack<Marker> branchStack; // przechowuje informacje jak gleboko jest zagniezdzony dany wezel
	
	ArrayList<MarkerHolder> branchStack;
	
	public void pushOnStack (Marker marker){
		branchStack.add(new MarkerHolder(marker));
	}
	
	public void popFromStack (){
		branchStack.remove(branchStack.size()-1);
	}
	
	public Marker peekOnStack(){
		return branchStack.get(branchStack.size()-1).marker;
	}
	
	
	public ArrayList<MarkerHolder> getBranchStack() {
		return branchStack;
	}

	// licznik wezlow, do tworzenia unikalnych ID
	static int counter = 0;

	public Node() {
		Node.counter++;
//		outNodes = new ArrayList<Node>();
//		inNodes = new ArrayList<Node>();
		branchStack = new ArrayList<MarkerHolder>();
	}

	public Node(String formula) {
		this();
		this.formula = formula;
	}

	public Node(String id, String formula) {
		this(formula);
		this.id = id;
		this.marker = Marker.UNMARKED;
	}

	public Node (String id, String formula, Marker marker){
		this (id, formula);
		this.marker=marker;
	}
	
	public Node(String formula, Marker marker) {
		this(formula);
		this.marker = marker;
	}



	public String toString(){
		return (isInDeadDiamond ? "(D) " : "") + this.id + " ("+ marker.toString()+ ") " + formula;
	}
	
	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String printStack() {
		String outputString = new String();
		for (int i=branchStack.size() -1; i >= 0; i--){
			outputString = outputString.concat("  " +i+ "." + branchStack.get(i).marker.toString() + "\n");
		}
		return outputString;
	}
}
