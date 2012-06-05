package pattern;

import java.util.ArrayList;

import graph.Graph;
import graph.Node;

// klasa zajmuje sie wyszukiwaniem patternow
public class PatternHandler {
	Graph graph;
	ArrayList<ArrayList<Node>> foundPatternsList;
	AbstractPattern pattern;
	public PatternHandler(Graph graph, AbstractPattern pattern){
		this.graph = graph;
		this.foundPatternsList = new ArrayList<ArrayList<Node>>();
		this.pattern = pattern;
	}
	
	private void mark(){
		for (Node node : graph.getNodes()){
			ArrayList<Node> list = pattern.mark(node);
			if (list != null){
				foundPatternsList.add(list);
			}
		}
	}
	
	public int process (){
		int count = 0;
		// dopasowanie wzorca i zaznaczenie markerami
		mark();
		
		// generuj nowy wezel dla najbardziej zewnetrznego
		
			for (ArrayList<Node> list : foundPatternsList){// to bedzie pewnie wadliwie dzialac
				if ( pattern.isTurnable(list)) // teoretycznie ten warunek powinien wystarczyc
				{
					Node node = pattern.generateNewNode(list);
					graph.addNode(node);
					graph.getNodes().removeAll(list);
					count++;
				}
			}
			//TODO cleanup listy foundPatternsList
			graph.cleanupEdges();
			return count;
	}
		
	
	
}
