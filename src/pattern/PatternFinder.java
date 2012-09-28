package pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import exceptions.NodeTypeMismatchException;

import graph.DirectedGraph;
import graph.Marker;
import graph.Node;

public class PatternFinder {
	
	private int markCounter = 0; // oznacza ile bylo markow
	
	DirectedGraph graph;
	FormulaParser mFormulaParser;
	
	

	ArrayList<Node> mCleanupTripleSequenceList; // lista przechowujaca trojki
	
	public DirectedGraph getGraph() {
		return graph;
	}

	public void setGraph(DirectedGraph graph) {
		this.graph = graph;
	}
	
	public PatternFinder(){
		mCleanupTripleSequenceList = new ArrayList<Node>();
		mFormulaParser = new FormulaParser();
                mFormulaParser.readPatternDefinitions("patterns-TL.txt");
	}
	
	//TODO zwraca nulla zamiast rzucac wyjatek
	private Node findRoot(){
		for (Node node : graph.getVertices()){
			if (graph.getPredecessors(node).size() == 0 )
				return node;
		}
		return null;
	}	

	
	
	public int generateFormulas(){
		int counter =0;
		while (cleanupTripleSequences() > 0){
			counter++;
		}
		
		counter += dispatchHighestLevelMerge();
		counter += dispatchHighestLevelSplit();
		if (counter == 0){ // czyli zostala prosta linia (same sekwencje)
			dispatchLastSequence();
		}
		markNodes();
		return counter;
	}

	// wyszukuje ostatni wezel ktory nie ma nastepcow
	//TODO dorobic wyrzucanie wyjatku zamiast nulla
	private Node findLastNode(){
		for (Node node : graph.getVertices()){
			if (graph.getSuccessorCount(node) == 0 )
				return node;
		}
		return null;
	}
	
	private void dispatchLastSequence(){
		while (graph.getVertexCount()>1){
			deleteSequenceNode(findLastNode());
		}
	}
	
	
	// zakladamy ze nie ma juz zadnych nastepcow
	// czyli wywolujemy na ostatnim
	private void deleteSequenceNode(Node node){
		try{
			Node previousNode = getPredecessor(node);
			previousNode.setFormula(mFormulaParser.getSequenceFormula(previousNode.getFormula(), node.getFormula()));
			graph.removeVertex(node);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// oznacza graf markerami
	public int markNodes(){
		markCounter = 0;
		markSequences(findRoot());
		clearBranchStacks();
		fillBranchStacks(findRoot());
                markInformalNodes(findRoot());
		return markCounter;
	}
	
	private void clearBranchStacks(){
		for (Node node : graph.getVertices()){
			node.getBranchStack().clear();
		}
	}
	
	
	// jesli nastepny wezel nie jest wszelkiego rodzaju zlaczeniem, to kopiujemy caly stos
	// jezeli jestemy w splicie, to dodajemy biezacy marker
	private void fillBranchStacks(Node node){
		for (Node nextNode : graph.getSuccessors(node)){
			nextNode.getBranchStack().clear();
			nextNode.getBranchStack().addAll(node.getBranchStack());
			if (graph.getPredecessorCount(nextNode)>1){
				nextNode.popFromStack();
			}
			if (graph.getSuccessorCount(node)>1){
				nextNode.pushOnStack(node.getMarker());
			}
			fillBranchStacks(nextNode);
		}
	}
	
	// TODO w zasadzie mozna to usunac kiedys
	// spelcjalnie nie zaznacza ostatniego jezeli pasuje
	public void markSequences(Node node){
		if (graph.getSuccessors(node).size()==1 && node.getMarker().equals(Marker.UNMARKED)){
			node.setMarker(Marker.SEQUENCE);
			markCounter++;
		}
		for (Node nextNode : graph.getSuccessors(node)){
			markSequences(nextNode);
		}
	}
	
	// zapewnia ze ma za i przed soba JEDEN wezel
	private boolean isPureSequenceNode(Node node){
		if (graph.getPredecessorCount(node) == 1 && graph.getSuccessorCount(node) == 1)
			return true;
		else return false;	
	}

	private Node getPredecessor(Node node) throws NodeTypeMismatchException{
		if (graph.getPredecessorCount(node) > 1)
			throw (new NodeTypeMismatchException()); 
		return (Node)graph.getPredecessors(node).toArray()[0];
	}

	
	private Node getSuccessor(Node node) throws NodeTypeMismatchException{
		if (graph.getSuccessorCount(node) > 1)
			throw (new NodeTypeMismatchException()); 
		return (Node)graph.getSuccessors(node).toArray()[0];
	}
	
	// wykrywa sekwencje ktore nie narusza bramek (czyli potrojne)
	// zwraca ilosc wykrytych
	private int findTripleSequences(){
		mCleanupTripleSequenceList.clear();
		int counter = 0;
		for (Node node : graph.getVertices()){
			if (isPureSequenceNode(node)){
				try{
					Node nextNode = getSuccessor(node);
					if (isPureSequenceNode(nextNode)){
						if (isPureSequenceNode(getSuccessor(nextNode))){
							// mamy odnaleziona trojke
							mCleanupTripleSequenceList.add(nextNode);
							counter++;
						}
					}
				}catch (NodeTypeMismatchException e){
					e.printStackTrace();
				}
			}
		}
		return counter;
	}
	
	// usuwa srodkowy wezel z potrojnej sekwencji
	private void dispatchTripleSequences(){
		for (Node node : mCleanupTripleSequenceList){
			try{
				Node predecessor = getPredecessor(node);
				graph.addEdge(predecessor, getSuccessor(node));
				predecessor.setFormula(mFormulaParser.getSequenceFormula(predecessor.getFormula(), node.getFormula()));
				System.out.println(node.getId()+ ": usuwam (srodkowy z trojki)");
				graph.removeVertex(node);
			}catch (NodeTypeMismatchException e){
				e.printStackTrace();
			}
		}
	}
	
	// wyszukuje i zajmuje sie wykrytymi ciagami wiecej niz 3 wezlow
	private int cleanupTripleSequences(){
		int counter = 0;
		while (findTripleSequences() > 0){
			counter++;
			dispatchTripleSequences();
		}
		return counter;
	}
	
	// split oznacza dowolnego typu rozgalezienie (typ nie ma znaczenia, marker zalatwia sprawe)
	// zwraca ile usunal
	// nie usuwa pojedynczo, zeby zapobiec zlym sytuacjom przed deathDiamondem
	// teoretycznie usuwa z sytuacji idealnej (tj. tuz przed deathDiamondem)
	// jezeli bedzie blad to prawdopodobnie wynikajacy z tego zalozenia
	private int dispatchHighestLevelSplit(){
		int counter  = 0;
		ArrayList<Node> gatewayList = new ArrayList<Node>(); 
		for (Node node : graph.getVertices()){
			if ( graph.getSuccessorCount(node)>1){
				gatewayList.add(node);
			}
		}
		if (gatewayList.size() == 0) return 0;
		Collections.sort(gatewayList, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				if(o1.getBranchStack().size() > o2.getBranchStack().size())
					return 1;
				if(o1.getBranchStack().size() < o2.getBranchStack().size())
					return -1;
				return 0;
			}
		});
		// wyglada bezsensu ;) ale usuwa wszystkie splity na tym samym poziomie w roznych miejscach
		int highestLevel = gatewayList.get(gatewayList.size()-1).getBranchStack().size();
		System.out.println(highestLevel);
		int i = gatewayList.size()-1;
		while (gatewayList.get(i).getBranchStack().size() == highestLevel){
			System.out.println("split i = " + i);
			deleteSplitNode(gatewayList.get(i));
			i = i-1;
			counter++;
			if (i <0) break; // zabezpieczenie przed array out of bonds
		}
		return counter;
	}
	
	// obsluga usuwania wezla split
	private void deleteSplitNode(Node node){
		System.out.println(node.getId()+ ": usuwam (split)");
		ArrayList<Node> nodesToDelete = new ArrayList<Node>();
		ArrayList<String> neighbourFormulas = new ArrayList<String>();// do przekazania parserowi formul
		neighbourFormulas.add(node.getFormula());
		for (Node neighbour : graph.getSuccessors(node)){
			try{
				if (!(graph.isSuccessor(node, getSuccessor(neighbour))))
					graph.addEdge(node, getSuccessor(neighbour));
				nodesToDelete.add(neighbour);
				neighbourFormulas.add(neighbour.getFormula());
			}catch(NodeTypeMismatchException e){
				e.printStackTrace();
			}
		}
		for (Node nodeToDelete : nodesToDelete){
			graph.removeVertex(nodeToDelete);
		}
		node.setFormula(mFormulaParser.getSplitFormula(neighbourFormulas, node.getMarker()));
		node.setMarker(Marker.UNMARKED);
	}
	
	// copy+paste tego co dla splita
	private int dispatchHighestLevelMerge(){
		int counter  = 0;
		ArrayList<Node> gatewayList = new ArrayList<Node>(); 
		for (Node node : graph.getVertices()){
			if ( graph.getPredecessorCount(node)>1){
				gatewayList.add(node);
			}
		}
		System.out.println ("gatewayList size: " + gatewayList.size());
		if (gatewayList.size() == 0) return 0;
		Collections.sort(gatewayList, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				if(o1.getBranchStack().size() > o2.getBranchStack().size())
					return 1;
				if(o1.getBranchStack().size() < o2.getBranchStack().size())
					return -1;
				return 0;
			}
		});
		// wyglada bezsensu ;) ale usuwa wszystkie splity na tym samym poziomie w roznych miejscach
		int highestLevel = gatewayList.get(gatewayList.size()-1).getBranchStack().size();
		int i= gatewayList.size()-1;
		while (gatewayList.get(i).getBranchStack().size() == highestLevel){
			System.out.println("merge i = " + i);
			deleteMergeNode(gatewayList.get(i));
			i = i-1;
			counter++;
			if (i <0) break; // zabezpieczenie przed array out of bonds
		}
		
		return counter;
	}
	
	// obsluga usuwania wezla split
	private void deleteMergeNode(Node node){
		System.out.println(node.getId()+ ": usuwam (merge)");
		ArrayList<Node> nodesToDelete = new ArrayList<Node>();
		ArrayList<String> neighbourFormulas = new ArrayList<String>();// do przekazania parserowi formul
		neighbourFormulas.add(node.getFormula());
		for (Node neighbour : graph.getPredecessors(node)){
			try{
				if (!(graph.isPredecessor(getPredecessor(neighbour), node)))
					graph.addEdge(getPredecessor(neighbour), node);
				nodesToDelete.add(neighbour);
				neighbourFormulas.add(neighbour.getFormula());
			}catch(NodeTypeMismatchException e){
				e.printStackTrace();
			}
		}
		for (Node nodeToDelete : nodesToDelete){
			graph.removeVertex(nodeToDelete);
		}
		node.setFormula(mFormulaParser.getMergeFormula(neighbourFormulas, node.getMarker()));
		node.setMarker(Marker.UNMARKED);
	}

    // ustawia markery wszystkim nie do konca prawidlowym splitom i merom 
    private void markInformalNodes(Node node) {
        if (graph.getPredecessorCount(node)>1){
                node.setMarker(Marker.PARALLEL_SPLIT);
        }
        if (graph.getSuccessorCount(node)>1){
                node.setMarker(Marker.PARALLEL_SPLIT);
        }
        for (Node nextNode : graph.getSuccessors(node)){
                markInformalNodes(nextNode);
        }
    }
	
}

