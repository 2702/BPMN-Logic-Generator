package graph;
// klasa dbajaca o unikatowosc krawedzi. napisana dla czystej wygody
public class Edge {
	static int GlobalID = 0;
	int ID;
	public Edge(){
		ID = GlobalID;
		GlobalID++;
	}
}
