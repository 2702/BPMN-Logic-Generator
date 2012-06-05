package generator;

import pattern.MergePattern;
import pattern.PatternHandler;
import pattern.SequencePattern;
import pattern.SplitPattern;
import graph.Graph;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new Graph();
		graph.addNode(1, "a");
		graph.addNode(2, "b1");
		graph.addNode(3, "b2");
		graph.addNode(4, "c1");
		graph.addNode(5, "c2");
		graph.addNode(6, "d1");
		
		//split
		graph.addConnection(1, 2);
		graph.addConnection(1, 3);
		//sequence 1
		graph.addConnection(2, 4);
		graph.addConnection(3, 5);
		
		//merge
		graph.addConnection(4, 6);
		graph.addConnection(5, 6);
		
		System.out.println("*************** przed splitem, merge  i sequencem ************** \n");
		System.out.println(graph);
		System.out.println ("\n\n\n");
		
		
		PatternHandler splitHandler = new PatternHandler(graph, new SplitPattern());
		PatternHandler mergeHandler = new PatternHandler(graph, new MergePattern());
		PatternHandler sequencehandler = new PatternHandler(graph, new SequencePattern());
		splitHandler.process();
		mergeHandler.process();
		sequencehandler.process();
		
		System.out.println("*************** po splicie, merge'u i sequence ************** \n");
		System.out.println(graph);
		
		

	}

}
