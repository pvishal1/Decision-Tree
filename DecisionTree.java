
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class DecisionTree {

	String[] features;
	Node treeNode, nodeDup;

	public DecisionTree() {
		super();
		treeNode = new Node();
	}

	public DecisionTree(String[] list) {
		this.features = list;
	}

	public Node getTreeNode() {
		return treeNode;
	}

	public void setTreeNode(Node treeNode) {
		this.treeNode = treeNode;
	}

	public Node createTree(LinkedHashMap<String, HashMap<Integer, Integer>> data, Node root, boolean entropy) {
		root = new Node();
		root = createNode(data, root, entropy);
		if (root.getHeuristics() == 0) {
			root.setLeaf(true);
			if (root.getSet().get(features[features.length - 1]).values().stream().filter(l -> l == 0).count() > root
					.getSet().get(features[features.length - 1]).values().stream().filter(l -> l == 1).count()) {
				root.setDecision(0);
			} else {
				root.setDecision(1);
			}
		} else {
			setTheBestAttribute(root, entropy);

			if (root.getZeroSet() != null) {
				// System.out.println(root.getZeroSet().size());
				root.setZeroChild(createTree(root.getZeroSet(), root.getZeroChild(), entropy));
				root.getZeroChild().setParent(root);
			} else {
				root.setLeaf(true);
			}
			if (root.getOneSet() != null) {
				root.setOneChild(createTree(root.getOneSet(), root.getOneChild(), entropy));
				root.getOneChild().setParent(root);
			} else {
				root.setLeaf(true);
			}
		}
		return root;
	}

	private Node createNode(LinkedHashMap<String, HashMap<Integer, Integer>> data, Node node, boolean entropyIn) {
		node.setSet(data);
		// System.out.println(set.get(features[features.length - 1]).values().size());
		double value = 0;
		if (entropyIn) {
			value = calculateEntropy(data.get(features[features.length - 1]).values());
		} else {
			value = calculateVarianceImpurity(data.get(features[features.length - 1]).values());
		}
		node.setHeuristics(value);
		return node;
	}

	private void setTheBestAttribute(Node root, boolean entropyIn) {
		double maxGain = 0.0;
		String selectedAttribute = null;
		for (int i = 0; i < (features.length - 1); i++) {
			String feature = features[i];
			LinkedHashMap<String, HashMap<Integer, Integer>> zeroSet = getSet(root.getSet(), feature, 1);
			LinkedHashMap<String, HashMap<Integer, Integer>> oneSet = getSet(root.getSet(), feature, 0);

			double gain = calculateGain(root, feature, root.getSet(), zeroSet, oneSet, entropyIn);
			if (gain > maxGain) {
				maxGain = gain;
				selectedAttribute = feature;
				root.setOneSet(oneSet);
				root.setZeroSet(zeroSet);
			}
		}
		root.setFeature(selectedAttribute);
	}

	private LinkedHashMap<String, HashMap<Integer, Integer>> getSet(
			LinkedHashMap<String, HashMap<Integer, Integer>> data, String attribute, int value) {

		LinkedHashMap<String, HashMap<Integer, Integer>> cData = copy(data);
		HashMap<Integer, Integer> obs = cData.get(attribute);
		ArrayList<Integer> removeKeys = new ArrayList<Integer>();

		for (Integer i : obs.keySet()) {
			if (obs.get(i) == value) {
				removeKeys.add(i);
			}
		}

		for (int i = 0; i < features.length; i++) {
			for (Integer j : removeKeys) {
				cData.get(features[i]).remove(j);
			}
		}
		return cData;
	}

	private double calculateGain(Node root, String attribute, LinkedHashMap<String, HashMap<Integer, Integer>> set,
			LinkedHashMap<String, HashMap<Integer, Integer>> zeroSet,
			LinkedHashMap<String, HashMap<Integer, Integer>> oneSet, boolean entropyIn) {
		double gain = 0.0;
		double h1 = 0.0, h2 = 0.0;
		double countZero = set.get(attribute).values().stream().filter(l -> l == 0).count();
		double countOne = set.get(attribute).values().stream().filter(l -> l == 1).count();
		double total = countOne + countZero;

		if (entropyIn) {
			h1 = calculateEntropy(zeroSet.get(features[features.length - 1]).values());
			h2 = calculateEntropy(oneSet.get(features[features.length - 1]).values());
		} else {
			h1 = calculateVarianceImpurity(zeroSet.get(features[features.length - 1]).values());
			h2 = calculateVarianceImpurity(oneSet.get(features[features.length - 1]).values());
		}
		gain = root.getHeuristics() - ((double) (countZero / total) * h1) - ((double) (countOne / total) * h2);
		return gain;
	}

	private double calculateEntropy(Collection<Integer> values) {
		double countZero = values.stream().filter(l -> l == 0).count();
		double countOne = values.stream().filter(l -> l == 1).count();
		double total = countOne + countZero;

		if ((countZero == total) || (countOne == total)) {
			return 0;
		} else {
			double probabilityZero = countZero / total;
			double probabilityOne = countOne / total;
			double entropy = -((probabilityZero * log2(probabilityZero)) + (probabilityOne * log2(probabilityOne)));
			return entropy;
		}
	}

	private double log2(double value) {
		return (Math.log10(value) / Math.log10(2));
	}

	private double calculateVarianceImpurity(Collection<Integer> values) {
		double countZero = values.stream().filter(l -> l == 0).count();
		double countOne = values.stream().filter(l -> l == 1).count();
		double total = countOne + countZero;
		
		if ((countZero == total) || (countOne == total)) {
			return 0;
		} else {
			return ((countZero / total) * (countOne / total));
		}
	}

	private LinkedHashMap<String, HashMap<Integer, Integer>> copy(
			LinkedHashMap<String, HashMap<Integer, Integer>> original) {
		LinkedHashMap<String, HashMap<Integer, Integer>> copy = null;
		
		if (original != null) {
			copy = new LinkedHashMap<String, HashMap<Integer, Integer>>();
			for (Entry<String, HashMap<Integer, Integer>> entry : original.entrySet()) {
				copy.put(entry.getKey(), new HashMap<Integer, Integer>(entry.getValue()));
			}
		}
		return copy;
	}

	public String printTree(Node root, int depth) {
		String result = "";
		for (int i = 0; i < depth; i++) {
			result = result.concat("| ");
		}
		result = result.concat(root.getFeature() + " = 0 :");
		if (root.getZeroChild() != null) {
			if (!root.getZeroChild().isLeaf()) {
				result = result.concat("\n" + printTree(root.getZeroChild(), depth + 1));
			} else {
				result = result.concat(" " + root.getZeroChild().getDecision() + "\n");
			}
		}
		for (int i = 0; i < depth; i++) {
			result = result.concat("| ");
		}
		result = result.concat(root.getFeature() + " = 1 :");
		if (root.getOneChild() != null) {
			if (!root.getOneChild().isLeaf()) {
				result = result.concat("\n" + printTree(root.getOneChild(), depth + 1));
			} else {
				result = result.concat(" " + root.getOneChild().getDecision() + "\n");
			}
		}
		return result.toString();
	}

	public double calculateAccuracy(Node root, ArrayList<int[]> data) {
		double accuracy = 0;
		double dataSize = data.size();

		for (int i = 0; i < dataSize; i++) {
			if (isPredicted(root, data.get(i))) {
				accuracy++;
			}
		}
		return ((accuracy / dataSize) * 100);
	}

	private boolean isPredicted(Node root, int[] observationsIn) {
		if (root.isLeaf()) {
			int actualDec = observationsIn[observationsIn.length - 1];
			int predictedDec = root.getDecision();
			return (actualDec == predictedDec);
		} else {
			int value = observationsIn[Arrays.asList(features).indexOf(root.getFeature())];
			if (value == 1) {
				return isPredicted(root.getOneChild(), observationsIn);
			} else {
				return isPredicted(root.getZeroChild(), observationsIn);
			}
		}
	}
	
	public int prune(Node root, ArrayList<int[]> validationData) {
		int d=-1;
		boolean fZero = false, fOne = false;
		if (root.getZeroChild() != null) {
			if (!root.getZeroChild().isLeaf())
				prune(root.getZeroChild(), validationData);
			else
				fZero = true;
		}
		if (root.getOneChild() != null) {
			if (!root.getOneChild().isLeaf())
				prune(root.getOneChild(), validationData);
			else
				fOne = true;
		}
		if(d == 0) {
			root.setLeaf(true);
			root.setDecision(d);
			fZero = true;
			fOne = true;
		} else if(d == 1) {
			root.setLeaf(true);
			root.setDecision(d);
			fZero = true;
			fOne = true;
		}
		if(fZero && fOne) {
			Node n2 = root;
			long zeroCount = n2.getZeroSet().get(features[features.length - 1]).values().stream()
					.filter(l -> l == 0).count();
			zeroCount += n2.getOneSet().get(features[features.length - 1]).values().stream()
					.filter(l -> l == 0).count();
			long oneCount = n2.getZeroSet().get(features[features.length - 1]).values().stream()
					.filter(l -> l == 1).count();
			oneCount += n2.getOneSet().get(features[features.length - 1]).values().stream()
					.filter(l -> l == 1).count();
			n2.setDecision((int) (oneCount > zeroCount ? 1 : 0));
			n2.setLeaf(true);
			if(calculateAccuracy(nodeDup, validationData) > calculateAccuracy(treeNode, validationData)) {
				d = (int) (oneCount > zeroCount ? 1 : 0);
			} else {
				n2.setLeaf(false);
			}
		}
		return d;
	}
	
	public Node clone(Node root) {
		Node copy = null;
		if (root != null) {
			copy = new Node(root);

			copy.setSet(copy(root.getSet()));
			copy.setZeroSet(copy(root.getZeroSet()));
			copy.setOneSet(copy(root.getOneSet()));

			copy.setZeroChild(clone(root.getZeroChild()));
			copy.setOneChild(clone(root.getOneChild()));
		}
		return copy;
	}
}
