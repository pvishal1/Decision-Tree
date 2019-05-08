
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Driver {

	public static void main(String[] args) {
		if (args.length != 5) {
			System.out.println("Invalid Arguments: Please refer the README.txt for instructions");
			return;
		}

		String trainingFile = args[0];
		String validationFile = args[1];
		String testFile = args[2];
		String toPrint = args[3].split(":")[1];
		String toPrune = args[4].split(":")[1];

		FileProcessor fp = new FileProcessor();
		LinkedHashMap<String, HashMap<Integer, Integer>> trainingData = fp.readFile(trainingFile);
		
		String[] featureName = fp.getFeatureName();

		ArrayList<int[]> trainingVal = fp.getData(trainingFile);
		ArrayList<int[]> validationVal = fp.getData(validationFile);
		ArrayList<int[]> testVal = fp.getData(testFile);

		DecisionTree tree = new DecisionTree(featureName);
		tree.setTreeNode(tree.createTree(trainingData, tree.treeNode, true));
		if (toPrint.equalsIgnoreCase("yes")) {
			System.out.println("Entropy: ");
			System.out.println(tree.printTree(tree.treeNode, 0));
		}
		System.out.println("D1\nH1\tNP\tTraining " + tree.calculateAccuracy(tree.treeNode, trainingVal) + "%");
		System.out.println("H1\tNP\tValidation " + tree.calculateAccuracy(tree.treeNode, validationVal) + "%");
		System.out.println("H1\tNP\tTest " + tree.calculateAccuracy(tree.treeNode, testVal) + "%");

		if (toPrune.equalsIgnoreCase("yes")) {
			tree.nodeDup = tree.clone(tree.treeNode);
			tree.prune(tree.nodeDup, validationVal);
			tree.treeNode = tree.clone(tree.nodeDup);

			System.out.println("H1\tP\tTraining " + tree.calculateAccuracy(tree.treeNode, trainingVal) + "%");
			System.out.println("H1\tP\tValidation " + tree.calculateAccuracy(tree.treeNode, validationVal) + "%");
			System.out.println("H1\tP\tTest " + tree.calculateAccuracy(tree.treeNode, testVal) + "%");
		}

		
		
		DecisionTree tree1 = new DecisionTree(featureName);
		tree1.setTreeNode(tree1.createTree(trainingData, tree1.treeNode, false));
		if (toPrint.equalsIgnoreCase("yes")) {
			System.out.println("Information Gain Heuristic");
			System.out.println(tree1.printTree(tree1.treeNode, 0));
		}
		System.out.println("H2\tNP\tTraining " + tree.calculateAccuracy(tree.treeNode, trainingVal) + "%");
		System.out.println("H2\tNP\tValidation " + tree.calculateAccuracy(tree.treeNode, validationVal) + "%");
		System.out.println("H2\tNP\tTest " + tree.calculateAccuracy(tree.treeNode, testVal) + "%");

		if (toPrune.equalsIgnoreCase("yes")) {
			tree.nodeDup = tree.clone(tree.treeNode);
			tree.prune(tree.nodeDup, validationVal);
			tree.treeNode = tree.clone(tree.nodeDup);

			System.out.println("H2\tP\tTraining " + tree.calculateAccuracy(tree.treeNode, trainingVal) + "%");
			System.out.println("H2\tP\tValidation " + tree.calculateAccuracy(tree.treeNode, validationVal) + "%");
			System.out.println("H2\tP\tTest " + tree.calculateAccuracy(tree.treeNode, testVal) + "%");
		}

	}

}
