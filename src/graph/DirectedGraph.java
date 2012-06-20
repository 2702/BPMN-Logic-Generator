package graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import exceptions.NodeNotFoundException;

@SuppressWarnings("serial")
public class DirectedGraph extends  DirectedSparseMultigraph <Node, String>{
	
	//TODO dorobic obsluge wyjatku gdy nie ma id
	private Node findVertexById(String id) throws NodeNotFoundException{
		for (Node node : this.getVertices()){
			if (node.getId().equals(id))
				return node;
		}
		throw new NodeNotFoundException();
	}
	
	public void addEdge (String edgeName,String from,String to){
		try{
			super.addEdge(edgeName,findVertexById(from),findVertexById(to), EdgeType.DIRECTED );
		}catch (NodeNotFoundException e){
			System.out.println("problem...");
		}
		
	}
}
