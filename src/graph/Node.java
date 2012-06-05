package graph;

import java.util.ArrayList;
import java.util.HashSet;

public class Node {

	// podwojne wiazanie jest dla ulatwienia implementacji algorytmow
	ArrayList<Node> outNodes; // lista wezlow z krawedzi wychodzacych
	ArrayList<Node> inNodes; // lista wezlow z krawedzi przychodzacych
	Marker marker; // enum oznaczajacy dopasowanie danego wezla do wzorca
	int id;  // id wezla nie majace nic wspolnego z zawartoscia pola formula. potrzebne tylko do sprawnych operacji na grafie
	String formula; // formula jaka przechowuje dany wezel. na poczatku to bedzie identyfikator pobrany z xmla, a potem cos bardziej zlozonego
	
	// licznik wezlow, do tworzenia unikalnych ID
	static int counter = 0;	
	
	public Node(){
		Node.counter++;
		outNodes = new ArrayList<Node>();
		inNodes = new ArrayList<Node>();
	}
	
	public Node (String formula){
		this();
		this.formula = formula;
	}
	
	public Node(int id, String formula){
		this(formula);
		this.id=id;
		this.marker = Marker.UNTOUCHED;
	}
	
	public Node(String formula, Marker marker){
		this(formula);
		this.marker = marker;
	}
	
	public void connectWithOtherNode(Node other) {
		outNodes.add(other);
		other.connectInNode(this);
	}
	
	public void connectInNode(Node other){
		inNodes.add(other);
	}
	
	public String toString(){
		String outputString =
		 "Node id: " + id + " formula: " + formula + " marker: " + marker + " hash: "+ this.hashCode()+" \n";
				
		 if (!outNodes.isEmpty()){
			 outputString +=  "   OutNodes:\n";
			 for (Node node : outNodes){
				 outputString += "   -Node id: " + node.id + " formula: " + node.formula + " marker: " + node.marker +" hash: "+ node.hashCode()+ " \n";
			 }
		 }
		 
		 if (!inNodes.isEmpty()){
			 outputString +=  "   InNodes:\n";
			 for (Node node : inNodes){
				 outputString += "   -Node id: " + node.id + " formula: " + node.formula + " marker: " + node.marker +" hash: "+ node.hashCode()+" \n";
			 }
		 }
		 return outputString+ "\n";
	}

	public ArrayList<Node> getOutNodes() {
		return outNodes;
	}

	public void setOutNodes(ArrayList<Node> outNodes) {
		this.outNodes = outNodes;
	}

	public ArrayList<Node> getInNodes() {
		return inNodes;
	}

	public void setInNodes(ArrayList<Node> inNodes) {
		this.inNodes = inNodes;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	public void removeInNode(Node node){
		inNodes.remove(node);
	}

	
	public void removeOutNode(Node node){
		outNodes.remove(node);
	}
	
	public void cleanupEdges(){
		HashSet<Node> hs = new HashSet<Node>();
		hs.addAll(inNodes);
		inNodes.clear();
		inNodes.addAll(hs);
		hs.clear();
		hs.addAll(outNodes);
		outNodes.clear();
		outNodes.addAll(hs);
		
	}
	
}
