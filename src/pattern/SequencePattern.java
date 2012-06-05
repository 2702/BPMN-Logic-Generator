package pattern;

import graph.Marker;
import graph.Node;

import java.util.ArrayList;

public class SequencePattern extends AbstractPattern{

	@Override
	public ArrayList<Node> mark(Node node) {
		// bedzie male spaghetti. potrzebne sa markery 
		if (node.getOutNodes().size()==1){
			if (
					(node.getInNodes().size()==0 && node.getOutNodes().get(0).getMarker().equals(Marker.SPLIT_ROOT))
					|| (node.getMarker().equals(Marker.SPLIT) && node.getOutNodes().get(0).getMarker().equals(Marker.UNTOUCHED))
					|| (node.getMarker().equals(Marker.MERGE_ROOT) && node.getOutNodes().get(0).getMarker().equals(Marker.UNTOUCHED))
					|| (node.getMarker().equals(Marker.UNTOUCHED) && node.getOutNodes().get(0).getMarker().equals(Marker.UNTOUCHED))
					){
				ArrayList<Node> outlist = new ArrayList<Node>();
				outlist.add(node);
				outlist.add(node.getOutNodes().get(0));
				return outlist;
			}
		}
		
		
		return null;
	}	
	
	@Override
	protected boolean isWellMarked(Node node) {
		// TODO Auto-generated method stub
		// useless here
		return false;
	}

//Sequence(f1,f2):
	// f1 => <>f2
	@Override
	protected String generateNewFormula(ArrayList<Node> nodeList) {
		return "(" +nodeList.get(0).getFormula() + ") => <>(" + nodeList.get(1).getFormula() + ")";
		
	}

	@Override
	public boolean isTurnable(ArrayList<Node> nodelist) {
		return true;
	}

	//TODO bug - nie usuwa po ostatecznym scaleniu
	@Override
	protected Node generateNewNode(ArrayList<Node> nodelist) { //TODO do poprawy
		Node node = new Node(nodelist.get(0).getId(), this.generateNewFormula(nodelist));
		node.getOutNodes().addAll(nodelist.get(0).getOutNodes());
		node.getInNodes().addAll(nodelist.get(0).getInNodes());
		
		for (Node innode : nodelist.get(0).getInNodes()){
			innode.removeOutNode(nodelist.get(0));
			innode.connectWithOtherNode(node);
		}
		
		// sprawdza wszystkie referencje wychodzacych wezlow
		for (Node outnode : nodelist.get(1).getOutNodes()){
			outnode.removeInNode(nodelist.get(1));
			outnode.connectInNode(node);			
		}
		
		if (!nodelist.get(0).getMarker().equals(Marker.UNTOUCHED)){
			node.setMarker(nodelist.get(0).getMarker());
		}else{
			if (!nodelist.get(1).getMarker().equals(Marker.UNTOUCHED)){
				node.setMarker(nodelist.get(1).getMarker());
			}else
				node.setMarker(Marker.UNTOUCHED);	
		}
		
		return node;
	}

}
