package pattern;

import java.util.ArrayList;

import graph.Marker;
import graph.Node;

/*
 * oznaczenia Markera
 * zakkladamy ze split to jest odwrocone drzewo o 90st.
 * SPLIT_ROOT - najbardziej zewnetrzny korzen splita
 * SPLIT_TEMP_ROOT - korzen splita inny niz najbardziej zewnetrzny
 * SPLIT - liscie 
 */
// w tym algorytmie zakładamy ze w liscie wezlow na indeksie 0 jest glowny wezel
public class SplitPattern extends AbstractPattern{

	@Override
	protected boolean isWellMarked(Node node) {
	   if (node.getMarker().equals(Marker.SPLIT) 
			   || node.getMarker().equals(Marker.UNTOUCHED) 
			   || node.getMarker().equals(Marker.SPLIT_ROOT)
			   || node.getMarker().equals(Marker.SPLIT_TEMP_ROOT))
		   return true;
	   else
		   return false;
	}
	
	
	@Override
	public ArrayList<Node> mark(Node node) {
		//TODO a co jesli jest split wiekszy niz na 2?
		if (this.isWellMarked(node) && node.getOutNodes().size()==2){
			for (Node innernode : node.getOutNodes()){
				if (!this.isWellMarked(innernode))
					return null;
			}
			// po sprawdzonych warunkach ustalamy markery
			// dla glownego jako korzen splita
			// nastepnie wrzucamy to wszystko do listy do zwrocenia
			ArrayList<Node> outlist = new ArrayList<Node>();
			node.setMarker(Marker.SPLIT_ROOT);
			outlist.add(node);
			for (Node innernode :node.getOutNodes()){
				outlist.add(innernode);
				if (innernode.getMarker().equals(Marker.SPLIT_ROOT))
					innernode.setMarker(Marker.SPLIT_TEMP_ROOT);
				else 
					innernode.setMarker(Marker.SPLIT);
			}
			return outlist;
		}
		else
			return null;
	}

	
	// z dostarczonych formul:
	//Parallel-Split(f1,f2,f3):
	// f1 => <>f2 & <>f3
	//		[]~(f1&(f2|f3))
	@Override
	protected String generateNewFormula(ArrayList<Node> nodeList){
		//TODO zmienic z hardcode na konfigurowalne znaczki
		return "(" +nodeList.get(0).getFormula() + ") => <>(" + nodeList.get(1).getFormula() + ") & <>(" + nodeList.get(2).getFormula()+")";
	}

	@Override
	public boolean isTurnable(ArrayList<Node> nodelist) {
		if (nodelist.get(0).getMarker().equals(Marker.SPLIT_ROOT)
				|| nodelist.get(0).getMarker().equals(Marker.SPLIT_TEMP_ROOT)
				&& nodelist.get(1).getMarker().equals(Marker.SPLIT) 
				&& nodelist.get(2).getMarker().equals(Marker.SPLIT))
			return true;
		else
			return false;
	}

	@Override
	public Node generateNewNode(ArrayList<Node> nodelist) {
		Node node = new Node(nodelist.get(0).getId(), this.generateNewFormula(nodelist));
		// moze tu byc blad
		// najpierw sprawdza wszystkie referencje wchodzacych wezlow
		for (Node innode : nodelist.get(0).getInNodes()){
			innode.removeOutNode(nodelist.get(0));
			innode.connectWithOtherNode(node);
		}
		// potem te same wiazania w druga strona
		node.getInNodes().addAll(nodelist.get(0).getInNodes());
		
		// sprawdza wszystkie referencje wychodzacych wezlow
		for (Node outnode : nodelist.get(1).getOutNodes()){
			outnode.removeInNode(nodelist.get(1));
			outnode.connectInNode(node);			
		}
		node.getOutNodes().addAll(nodelist.get(1).getOutNodes());
		// copy paste
		for (Node outnode : nodelist.get(2).getOutNodes()){
			outnode.removeInNode(nodelist.get(2));
			outnode.connectInNode(node);			
		}
		node.getOutNodes().addAll(nodelist.get(2).getOutNodes());
		if (nodelist.get(0).getMarker().equals(Marker.SPLIT_TEMP_ROOT))
			node.setMarker(Marker.SPLIT);
		else
			node.setMarker(Marker.UNTOUCHED);
		return node;
	}


}
