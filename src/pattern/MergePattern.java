package pattern;

import graph.Marker;
import graph.Node;

import java.util.ArrayList;

public class MergePattern extends AbstractPattern {

	@Override
	public ArrayList<Node> mark(Node node) {
		//TODO a co jesli jest split wiekszy niz na 2?
				if (this.isWellMarked(node) && node.getInNodes().size()==2){
					for (Node innernode : node.getInNodes()){
						if (!this.isWellMarked(innernode))
							return null;
					}
					// po sprawdzonych warunkach ustalamy markery
					// dla glownego jako korzen merge'a
					// nastepnie wrzucamy to wszystko do listy do zwrocenia
					ArrayList<Node> outlist = new ArrayList<Node>();
					node.setMarker(Marker.MERGE_ROOT);
					outlist.add(node);
					for (Node innernode :node.getInNodes()){
						outlist.add(innernode);
						if (innernode.getMarker().equals(Marker.MERGE_ROOT))
							innernode.setMarker(Marker.MERGE_TEMP_ROOT);
						else 
							innernode.setMarker(Marker.MERGE);
					}
					return outlist;
				}
				else
					return null;
	}

	@Override
	// zakladamy ze matchable jest root merge'a
	protected boolean isWellMarked(Node node) {
		if (node.getMarker().equals(Marker.MERGE) 
				   || node.getMarker().equals(Marker.UNTOUCHED) 
				   || node.getMarker().equals(Marker.MERGE_ROOT)
				   || node.getMarker().equals(Marker.MERGE_TEMP_ROOT))
			   return true;
		   else
			   return false;
	}

	//Simple-Merge(f1,f2,f3):
		//	f1|f2 => <>f3
		//	[]~(f3&(f1|f2))
		@Override
		protected String generateNewFormula(ArrayList<Node> nodeList){
			//TODO zmienic z hardcode na konfigurowalne znaczki i przemyslec nawiasy!!!!
			return "(" +nodeList.get(1).getFormula() + ") | (" + nodeList.get(2).getFormula() + ") => <> (" + nodeList.get(0).getFormula()+")";
		}

	@Override
	public boolean isTurnable(ArrayList<Node> nodelist) {
		if (nodelist.get(0).getMarker().equals(Marker.MERGE_ROOT)
				|| nodelist.get(0).getMarker().equals(Marker.MERGE_TEMP_ROOT)
				&& nodelist.get(1).getMarker().equals(Marker.MERGE) 
				&& nodelist.get(2).getMarker().equals(Marker.MERGE))
			return true;
		else
			return false;
	}

	@Override
	public Node generateNewNode(ArrayList<Node> nodelist) {
		Node node = new Node(nodelist.get(0).getId(), this.generateNewFormula(nodelist));
		// moze tu byc blad
		// najpierw sprawdza wszystkie referencje wychodzacych wezlow
		for (Node outnode : nodelist.get(0).getOutNodes()){
			outnode.removeInNode(nodelist.get(0));
			outnode.connectInNode(node);
		}
		// potem te same wiazania w druga strona
		node.getOutNodes().addAll(nodelist.get(0).getOutNodes());
		
		// sprawdza wszystkie referencje wychodzacych wezlow
		for (Node innode : nodelist.get(1).getInNodes()){
			innode.removeOutNode(nodelist.get(1));
			innode.connectWithOtherNode(node);			
		}
		node.getInNodes().addAll(nodelist.get(1).getInNodes());
		// copy paste
		for (Node innode : nodelist.get(2).getInNodes()){
			innode.removeOutNode(nodelist.get(2));
			innode.connectWithOtherNode(node);			
		}
		node.getInNodes().addAll(nodelist.get(2).getInNodes());
		
		
		if (nodelist.get(0).getMarker().equals(Marker.MERGE_TEMP_ROOT))
			node.setMarker(Marker.MERGE);
		else
			node.setMarker(Marker.UNTOUCHED);
		return node;
	}



}
