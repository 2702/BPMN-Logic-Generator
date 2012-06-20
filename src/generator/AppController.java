package generator;

import graph.DirectedGraph;

import java.awt.Dimension;

import javax.swing.JFrame;

import pattern.PatternFinder;

public class AppController {

	AppView mAppView;
	DirectedGraph graph;
	PatternFinder patternFinder;
	
	AppController(){
		createAndShowGUI();
		patternFinder = new PatternFinder();
	}
	
	public void loadGraph(DirectedGraph graph){
		this.graph = graph;
		patternFinder.setGraph(graph);
	}
	
	//rysuje na nowo graf
	public void refresh(){
		mAppView.drawGraph(graph);
	}
	
	private void createAndShowGUI() {
		
		// Create and set up the window.
		JFrame frame = new JFrame("Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mAppView = new AppView(this);
		// Add content to the window.
		frame.add(mAppView);

		// Display the window.
		frame.setSize(new Dimension(1000, 700));
		frame.setVisible(true);
	}

	public void markVertices(){
		int count = patternFinder.markNodes();
		mAppView.printToConsole(count + " nowych markerow");
		refresh();
	}
	
	public DirectedGraph getGraph() {
		return graph;
	}

	public void setGraph(DirectedGraph graph) {
		this.graph = graph;
	}
	
}
