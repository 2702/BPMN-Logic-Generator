package generator;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;


import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import graph.DirectedGraph;
import graph.Marker;
import graph.Node;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Pageable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import parser.VisualParadigmXmlParser;


public class AppView extends JPanel implements ActionListener {

	private AppController owner;
	
	private static File mInFile;
	private static File mOutFile;

	private JButton mOpenButton;
	private JButton mSaveButton;
	private JButton mGenerateButton;
	private JFileChooser mFileChooser;
	private JTextArea mLog;

	private JButton mGraphGeneratingButton;
	
	// komponent przechowujacy wizualizacje grafu
	private BasicVisualizationServer<Node,String> mBasicVisualisationServer;
	private JPanel mGraphsPanel;
	
	public AppView(AppController owner){
		this();
		this.owner = owner;
	}
	
	public AppView() {
		super(new BorderLayout());

		
		mLog = new JTextArea(5, 20);
		mLog.setMargin(new Insets(5, 5, 5, 5));
		mLog.setEditable(false);
		
		JScrollPane logScrollPane = new JScrollPane(mLog);
//		logScrollPane.setSize(new Dimension(300, 200));
		


		mOpenButton = new JButton("Open a File...");
		mOpenButton.addActionListener(this);

		mSaveButton = new JButton("Save a File...");
		mSaveButton.addActionListener(this);

		mGenerateButton = new JButton("Generate formula!");
		mGenerateButton.addActionListener(this);
		mGenerateButton.setEnabled(false);

		mGraphGeneratingButton = new JButton ("generate sample graph");
		mGraphGeneratingButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(mOpenButton);
		buttonPanel.add(mSaveButton);
		buttonPanel.add(mGenerateButton);
		buttonPanel.add(mGraphGeneratingButton);
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JLabel inLabel = new JLabel("Input file");
		
		
		mGraphsPanel = new JPanel();
		mGraphsPanel.setSize(new Dimension(300,200));
		mGraphsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		middlePanel.add(logScrollPane);
		middlePanel.add (mGraphsPanel);
		
		add(buttonPanel);
		add(inLabel);
		add(middlePanel);
		
		setLayout( new FlowLayout());
		
		mFileChooser = new JFileChooser(new File("."));
	}

//	private static void createAndShowGUI() {
//		// Create and set up the window.
//		JFrame frame = new JFrame("Generator");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		// Add content to the window.
//		frame.add(new AppView());
//
//		// Display the window.
//		frame.setSize(new Dimension(600, 600));
//		frame.setVisible(true);
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mOpenButton) {
			int returnVal = mFileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				mInFile = mFileChooser.getSelectedFile();
				mOpenButton.setText("In: " + mInFile.getName());
				mGenerateButton.setEnabled(true);	
				owner.loadGraph(new VisualParadigmXmlParser().parse(mInFile.getAbsolutePath()));
				owner.refresh();
				mLog.setText("");
			}
		} else if (e.getSource() == mSaveButton) {
			int returnVal = mFileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				mOutFile = mFileChooser.getSelectedFile();
				mSaveButton.setText("Out: " + mOutFile.getName());
			}
		} else if (e.getSource() == mGenerateButton) {
			generateFormula();
		} else if (e.getSource() == mGraphGeneratingButton){
			generateSampleGraph();
			mGenerateButton.setEnabled(true);
		}
	}

	private void generateSampleGraph(){
		DirectedGraph graph = new DirectedGraph();
		graph.addVertex(new Node ("t1", "t1", Marker.UNMARKED));
		graph.addVertex(new Node ("t2", "t2", Marker.UNMARKED));
		graph.addVertex(new Node ("t3", "t3", Marker.UNMARKED));
		graph.addVertex(new Node ("t4", "t4", Marker.UNMARKED));
		graph.addVertex(new Node ("t5", "t5", Marker.UNMARKED));
		graph.addVertex(new Node ("t6", "t6", Marker.UNMARKED));
		graph.addVertex(new Node ("g1", "g1", Marker.EXCLUSIVE_CHOICE));
		graph.addVertex(new Node ("g2", "g2", Marker.EXCLUSIVE_CHOICE));
		
		graph.addEdge("1", "t1", "g1");
		graph.addEdge("2", "g1", "t2");
		graph.addEdge("3", "g1", "t4");
		graph.addEdge("4", "t2", "t3");
		graph.addEdge("5", "t4", "t5");
		graph.addEdge("6", "t3", "g2");
		graph.addEdge("7", "t5", "g2");
		graph.addEdge("8", "g2", "t6");
		
		owner.loadGraph(graph);
		owner.refresh();
		mLog.setText("");
	}
	
	public void drawGraph(DirectedGraph graph){
		//TODO nie dzialaja rozmiary -.-'
		Layout<Node, String> layout = new CircleLayout(graph);
		layout.setSize(new Dimension(400, 400));
		mGraphsPanel.removeAll();
//		mGraphsPanel.setSize(new Dimension(600,600));
//		layout.setSize(mGraphsPanel.getSize());
		mBasicVisualisationServer = new BasicVisualizationServer<Node,String>(layout);
//		mBasicVisualisationServer.setSize(mGraphsPanel.getSize());
		mBasicVisualisationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
		
		mBasicVisualisationServer.setBorder(BorderFactory.createLineBorder(Color.CYAN));
		
		
		mGraphsPanel.add(mBasicVisualisationServer);
		
		mGraphsPanel.revalidate();
//		System.out.println(mBasicVisualisationServer.getSize());
//		System.out.println(mGraphsPanel.getSize());
//		System.out.println(layout.getSize());
		//revalidate();
	}
	
	void generateFormula() {
		owner.markVertices();
		printToConsole("Wykonano 1 iteracje");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		createAndShowGUI();
		new AppController();
	}
	
	
	public void printToConsole(String message){
		mLog.append("\t" + message + "\n");
	}
}
