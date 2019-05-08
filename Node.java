
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Node {
	double heuristics;
	String feature;
	boolean isLeaf;
	Node zeroChild, oneChild, parent;
	int decision;
	LinkedHashMap<String, HashMap<Integer, Integer>> set;
	LinkedHashMap<String, HashMap<Integer, Integer>> zeroSet;
	LinkedHashMap<String, HashMap<Integer, Integer>> oneSet;

	public Node() {
		super();
		this.set = null;
		this.zeroSet = null;
		this.oneSet = null;
		this.feature = null;
		this.zeroChild = null;
		this.oneChild = null;
		this.parent = null;
		this.isLeaf = false;
	}

	public Node(Node nodeIn) {
		this.setDecision(nodeIn.getDecision());
		this.setHeuristics(nodeIn.getHeuristics());
		this.setFeature(nodeIn.getFeature());
		this.setLeaf(nodeIn.isLeaf());
		this.setParent(nodeIn.getParent());
	}

	public double getHeuristics() {
		return heuristics;
	}

	public void setHeuristics(double entropy) {
		this.heuristics = entropy;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Node getZeroChild() {
		return zeroChild;
	}

	public void setZeroChild(Node zeroChild) {
		this.zeroChild = zeroChild;
	}

	public Node getOneChild() {
		return oneChild;
	}

	public void setOneChild(Node oneChild) {
		this.oneChild = oneChild;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getDecision() {
		return decision;
	}

	public void setDecision(int decision) {
		this.decision = decision;
	}

	public LinkedHashMap<String, HashMap<Integer, Integer>> getSet() {
		return set;
	}

	public void setSet(LinkedHashMap<String, HashMap<Integer, Integer>> set) {
		this.set = set;
	}

	public LinkedHashMap<String, HashMap<Integer, Integer>> getZeroSet() {
		return zeroSet;
	}

	public void setZeroSet(LinkedHashMap<String, HashMap<Integer, Integer>> zeroSet) {
		this.zeroSet = zeroSet;
	}

	public LinkedHashMap<String, HashMap<Integer, Integer>> getOneSet() {
		return oneSet;
	}

	public void setOneSet(LinkedHashMap<String, HashMap<Integer, Integer>> oneSet) {
		this.oneSet = oneSet;
	}

}
