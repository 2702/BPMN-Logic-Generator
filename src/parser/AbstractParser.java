package parser;

import graph.DirectedGraph;

/**
 * Abstract class for parsing BPMN XML files
 * @author Maciek
 *
 */
public abstract class AbstractParser {
	
	public abstract DirectedGraph parse(String filename);
}
