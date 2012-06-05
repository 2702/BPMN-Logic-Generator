package graph;

import java.util.ArrayList;

//graf skierowany. nie przechowuje krawedzi
public class Graph {
	ArrayList<Node> nodes;

	public Graph() {
		nodes = new ArrayList<Node>();
	}

	// TODO powinien rzucac wyjatek
	public Node findNodeById(String id) {
		Node foundNode = null;
		for (Node node : nodes) {
			if (node.getId().equals(id)) {
				foundNode = node;
			}
		}
		return foundNode;
	}

	// TODO wyjatek przy istniejacym id
	public void addNode(String id, String formula) {
		Node node = new Node(id, formula);
		nodes.add(node);
	}

	// polaczenie niesymetryczne
	public void addConnection(String idFrom, String idWhere) {
		this.findNodeById(idFrom).connectWithOtherNode(
				this.findNodeById(idWhere));
	}

	public String toString() {
		return nodes.toString();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public void removeNode(Node node) {
		nodes.remove(node);
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public void cleanupEdges() {
		for (Node node : nodes) {
			node.cleanupEdges();
		}
	}
}
