package pattern;

import java.util.ArrayList;
import java.util.Collection;

import graph.DirectedGraph;
import graph.Marker;
import graph.Node;

public class PatternFinder {
	
	private int markCounter = 0; // oznacza ile bylo markow
	private int formulasGenerationCounter = 0;
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
		System.out.println(node.getId() +"sprawdzamy deadDiamonda");
		if (graph.getSuccessorCount(node) >0){
			Node inNode = (Node)graph.getSuccessors(node).toArray()[0];
			if (graph.getSuccessorCount(inNode) >0){
				Node deadDiamondEnd = (Node)graph.getSuccessors(inNode).toArray()[0];
				if ((graph.getPredecessorCount(deadDiamondEnd) == graph.getSuccessorCount(node)) && 
						graph.getPredecessorCount(deadDiamondEnd)>1) {// mamy dead diamonda!
					node.setInDeadDiamond(true);
					return true;
				}
			}
		}
		else {
			return false;
		}
		
		
//		for (Node inNode : graph.getSuccessors(node)){
//			System.out.println(inNode.getId() +"sprawdzamy deadDiamonda");
//			if (graph.getSuccessorCount(inNode)!=1)
//				return false;
//			Node deadDiamondEnd = (Node)graph.getSuccessors(inNode).toArray()[0];
//			if ((graph.getPredecessorCount(deadDiamondEnd) == graph.getSuccessorCount(node)) && 
//					graph.getPredecessorCount(deadDiamondEnd)>1) {// mamy dead diamonda!
//				node.setInDeadDiamond(true);
////				for (Node inNodeAgain : graph.getSuccessors(node))
////					inNodeAgain.setInDeadDiamond(true);
////				deadDiamondEnd.setInDeadDiamond(true);
//				return true;
//			}
//		}
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
		markCounter++;
		//sprawdzamy czy juz jestesmy w oznaczonym wezle (np. merge'u)
//		if (!node.getMarker().equals(Marker.UNMARKED)){
//			for (Node inNode : graph.getSuccessors(node)){
//			}
//		}
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
	
	
	
	
	private int generateSequences(){
		int counter = 0;
		ArrayList <Node> nodesToRemove = new ArrayList<Node>();
		ArrayList <Node> nodes = new ArrayList<Node>();
		nodes.addAll(graph.getVertices());
		
		for (Node node: nodes){
			if (node.getMarker().equals(Marker.SEQUENCE)){
				Node newNode = new Node();
				newNode.setId(node.getId());
				graph.addVertex(newNode);
				Node nodeToDelete = (Node)graph.getSuccessors(node).toArray()[0];
				//przypisanie wszystkich krawedzi wychodzacych
				
				newNode.setMarker(nodeToDelete.getMarker());
				System.out.println(node.getId() + ": probuje zrealizowac sekwencje");
				//f1 => <>f2
				newNode.setFormula("("+node.getFormula()+") => <>("+ nodeToDelete.getFormula()+")" );
				if (graph.getSuccessorCount(nodeToDelete)>0)
				for (Object outNode :  graph.getSuccessors(nodeToDelete) ){
					graph.addEdge(((Node)outNode).getId() + newNode.hashCode(), newNode, ((Node)outNode) );
				}
				if (graph.getPredecessorCount(node)>0){
					for (Object inNode :  graph.getPredecessors(node)){
						graph.addEdge(((Node)inNode).getId() + newNode.hashCode(),  ((Node)inNode) ,newNode);
					}
				}
				nodesToRemove.add(nodeToDelete);
				nodesToRemove.add(node);
				counter++;
			}
		}
		for (Node nodeToDelete : nodesToRemove){
			graph.removeVertex( nodeToDelete);
		}
		return counter;
	}
	
	public void generateFormulas(){
		formulasGenerationCounter = 0;
		while (generateSequences()>0){
			
		}
		
	}
	
	// oznacza graf markerami
	public int markNodes(){
		markCounter = 0;
		markRoot(findRoot());
		return markCounter;
	}
}
