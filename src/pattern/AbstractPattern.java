package pattern;

import java.util.ArrayList;

import graph.Marker;
import graph.Node;

/*
 * algorytmy dzialaja nastepujaco
 * 1. dla kazdego wezla w grafie wywolywana jest metoda mark()
 *    jezeli dla danego wezla i jego sasiadow wzorzec jest dopasowany to jest oznaczany odpowiednimi markerami. wezly musza byc zapamietane 'na zewnatrz'
 * 2. dla kazdej listy 'na zewnatrz' sprawdzane jest czy mozna zamieniac , czy trzeba wejsc glebiej, czyli
 *   isTurnable(), a potem dispatchPattern()
 * 
 */

public abstract class AbstractPattern {
    // jakim markerem bedzie oznaczac wezly
    Marker marker;

    // jak bedzie wygladala formula po dopasowaniu wzorca

    // tu musi byc zaimplementowany algorytm dopasowania wzorca
    // oznacza markerami
    public abstract ArrayList<Node> mark(Node node);

    // okresla dla jakich markerow mozna dopasowywac
    protected abstract boolean isWellMarked(Node node);

    // zakladamy ze przejdzie test isMatchable
    protected abstract String generateNewFormula(ArrayList<Node> nodeList);

    // sprawdza czy podane wezly mozna zamieniac
    public abstract boolean isTurnable(ArrayList<Node> nodelist);

    // generuje nowy wezel a stare wywala
    protected abstract Node generateNewNode(ArrayList<Node> nodelist);

    // zamienia wezly z markerem na nowy wezel
    public void dispatchPattern(ArrayList<Node> nodelist) {
	generateNewNode(nodelist);
    }

    public Marker getMarker() {
	return marker;
    }

}
