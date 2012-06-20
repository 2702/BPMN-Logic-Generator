package pattern;

import graph.DirectedGraph;
import graph.Marker;
import graph.Node;

public class PatternFinder {
	
	private int markCounter = 0; // oznacza ile bylo markow
	DirectedGraph graph;
	

	public DirectedGraph getGraph() {
		return graph;
	}

	public void setGraph(DirectedGraph graph) {
		this.graph = graph;
	}

	//TODO zwraca nulla zamiast rzucac wyjatek
	private Node findRoot(){
		for (Node node : graph.getVertices()){
			if (graph.getPredecessors(node).size() == 0 )
				return node;
		}
		return null;
	}
	
	// dziala na roocie dead diamonda
	private boolean identifyDeadDiamond(Node node){
		for (Node inNode : graph.getSuccessors(node)){
			if (graph.getSuccessors(inNode).size()!=1)
				return false;
			if (graph.getPredecessors((Node)graph.getSuccessors(inNode).toArray()[0]).size() == graph.getSuccessors(node).size()){// mamy dead diamonda!
				node.setInDeadDiamond(true);
				for (Node inNodeAgain : graph.getSuccessors(node))
					inNodeAgain.setInDeadDiamond(true);
				((Node)graph.getSuccessors(inNode).toArray()[0]).setInDeadDiamond(true);
				return true;
			}
		}
		return false;
	}
	
	// markuje pierwszy wezel (wlasciwie sprawdza czy mozna zrobic z niego sekwencje
	private void markRoot(Node node){
		if (graph.getSuccessorCount(node) == 1){
			node.setMarker(Marker.SEQUENCE);
		}
		markNode((Node)graph.getSuccessors(node).toArray()[0]);
	}
	
	
	/* 
	 * wywoluje sie rekurencyjnie dla calego grafu
	 * przy splicie wrzuca następnym węzłom swój typ na ich branchStacka 
	 */
	private void markNode(Node node){
		//sprawdzamy czy juz jestesmy w oznaczonym wezle (np. merge'u)
		if (!node.getMarker().equals(Marker.UNMARKED)){
			for (Node inNode : graph.getSuccessors(node)){
			}
		}
		// w przeciwnym przypadku normalnie sprawdzamy
		if (graph.getSuccessors(node).size() > 1){ // jestesmy przy wszelkiego rodzaju splicie
			identifyDeadDiamond(node);
			for (Node inNode : graph.getSuccessors(node)){
				inNode.pushOnStack(node.getMarker());
			}
		} else if (graph.getSuccessors(node).size() == 1){ // sequence lub merge (wszelkiego rodzaju)
			//czy nastepny jest rootem merge'a
			Node nextNode = (Node)graph.getSuccessors(node).toArray()[0];					
			
			if (node.getBranchStack().size()>0) 
				if (nextNode.getMarker().equals(node.peekOnStack())){// doszlismy do oznaczonego merge'a, wiec STOP
					return;
				}else{ // pierwszy raz dochodzimy do merge'a i idziemy dalej
					nextNode.setMarker(node.getMarker());	
			}
			if (graph.getPredecessors(nextNode).size()==1){ // czy dalej mam fragment sequence'a
				if (node.getBranchStack().size()>0)
					nextNode.pushOnStack(node.peekOnStack());
				node.setMarker(Marker.SEQUENCE);
			}
		} 
		// wywolanie dla nastepnych wezlow
		for (Node inNode : graph.getSuccessors(node)){			
			markNode(inNode);			
		}
	}
	
	public void generateFormulas(DirectedGraph graph){
		
	}
	
	// oznacza graf markerami
	public int markNodes(){
		markCounter = 0;
		markRoot(findRoot());
		return markCounter;
	}
}
