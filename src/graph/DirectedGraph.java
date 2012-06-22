package graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import exceptions.NodeNotFoundException;

@SuppressWarnings("serial")
public class DirectedGraph extends  DirectedSparseMultigraph <Node, Edge>{
	
	//TODO dorobic obsluge wyjatku gdy nie ma id
	private Node findVertexById(String id) throws NodeNotFoundException{
		for (Node node : this.getVertices()){
			if (node.getId().equals(id))
				return node;
		}
		throw new NodeNotFoundException();
	}
	
	public void addEdge (Node from, Node to){
		addEdge (from.getId(), to.getId());
	}
	
	public void addEdge (String from,String to){
		try{
			super.addEdge(new Edge(),findVertexById(from),findVertexById(to), EdgeType.DIRECTED );
		}catch (NodeNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public Node getRemaningNode(){
		for (Node node: this.getVertices()){
			return node;
		}
		return null;
	}
	
}
