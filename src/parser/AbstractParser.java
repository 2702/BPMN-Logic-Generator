package parser;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graph.Node;


/**
 * Abstract class for parsing BPMN XML files
 * @author Maciek
 *
 */
public abstract class AbstractParser {
	
	public abstract DirectedSparseMultigraph<Node,String> parse(String filename);
}
