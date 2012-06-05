package parser;

import graph.Graph;

/**
 * Abstract class for parsing BPMN XML files
 * @author Maciek
 *
 */
public abstract class AbstractParser {
	
	public abstract Graph parse(String filename);
}
