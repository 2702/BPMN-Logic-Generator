package graph;
//SEQUENCE - oznacza ze dany wezel ma tylko jedno wyjscie i moze byc polaczony z nastepnym
public enum Marker {
	MERGE, UNMARKED, SPLIT, SEQUENCE, // podstawowe 
	DEAD_DIAMOND_SPLIT, DEAD_DIAMOND_BRANCH, DEAD_DIAMOND_MERGE, // pewnie do wywalenia
	EXCLUSIVE_CHOICE, PARALLEL_SPLIT, MULTI_CHOICE, 
	
	
}
