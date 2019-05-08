
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FileProcessor {
	String[] featureName = null;

	public FileProcessor() {
		super();
	}

	public String[] getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String[] featureName) {
		this.featureName = featureName;
	}

	public LinkedHashMap<String, HashMap<Integer, Integer>> readFile(String file) {
		LinkedHashMap<String, HashMap<Integer, Integer>> data = new LinkedHashMap<String, HashMap<Integer, Integer>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			if (line != null) {
				featureName = line.split(",");
				for (int i = 0; i < featureName.length; i++) {
					data.put(featureName[i], new HashMap<Integer, Integer>());
				}
			}
			line = br.readLine();
			int index = 0;
			while (line != null) {
				String arr[] = line.split(",");
				for (int i = 0; i < arr.length; i++) {
					data.get(featureName[i]).put(index, Integer.parseInt(arr[i]));
				}
				index++;
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public ArrayList<int[]> getData(String file) {
		BufferedReader br = null;
		ArrayList<String> header = new ArrayList<>();
		ArrayList<int[]> val = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			String[] st = line.split(",");
			for (String string : st) {
				header.add(string);
			}

			line = br.readLine();
			while (line != null) {
				st = line.split(",");
				int[] val1 = new int[header.size()];
				int i = 0;
				for (String string : st) {
					val1[i] = Integer.parseInt(string);
					i++;
				}
				val.add(val1);
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return val;
	}
}
