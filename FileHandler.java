import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class FileHandler {

	/**
	 * Imports a unigram file, normalises the probabilities, and inserts each row into
	 * a hashtree using the format key = word, value = normalised probability
	 * @param fileName - location of the unigram file
	 * @return hashtable
	 */
	public Hashtable<String, Double> importUnigram(String fileName) {
		Hashtable<String, Double> table = new Hashtable<String, Double>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				String key = strLineArray[0];
				double prob = Double.parseDouble(strLineArray[1]);
				double value = Math.pow(10, prob);
				
				table.put(key, value);
			}
			
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return table;
	}
	
	/**
	 * Imports a bigram file, normalises the probabilities, and inserts each row into
	 * a hashtree using the format key = (word1,word2), value = normalised probability
	 * @param fileName - location of the bigram file
	 * @return hashtable
	 */
	public Hashtable<String, Double> importBigram(String fileName) {
		Hashtable<String, Double> table = new Hashtable<String, Double>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			ArrayList<String[]> tempList = new ArrayList<String[]>();
			String strLine;
			String w1 = "";

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				if(w1.equals("")) {
					w1 = strLineArray[0];
					tempList.add(strLineArray);
				} else if(!w1.equalsIgnoreCase(strLineArray[0])) {
					//changed to different i j
					//process templist
					double sum = 0;
					for(String[] s: tempList) {
						sum += Math.pow(10, Double.parseDouble(s[2]));
					}
					
					for(String[] s: tempList) {
						String word1 = s[0]; //word 1
						String word2 = s[1]; //word 2
						
						String key = word1+","+word2; //e.g. key = "134,55"
						double prob = Double.parseDouble(s[2]);
						double value = (Math.pow(10, prob)/sum); //probability
						
						table.put(key, value);
					}
					
					//reset sum,list
					sum = 0;
					tempList.clear();
					//mark new start
					w1 = strLineArray[0];
					//add line
					tempList.add(strLineArray);
				} else if(w1.equalsIgnoreCase(strLineArray[0])) {
					tempList.add(strLineArray);
				}
					
			}
			
			//process anything still in tempList
			double sum = 0;
			for(String[] s: tempList) {
				sum += Math.pow(10, Double.parseDouble(s[2]));
			}
			
			for(String[] s: tempList) {
				String word1 = s[0]; //word 1
				String word2 = s[1]; //word 2
				
				String key = word1+","+word2; //e.g. key = "134,55"
				double prob = Double.parseDouble(s[2]);
				double value = (Math.pow(10, prob)/sum); //probability
				
				table.put(key, value);
			}
			
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return table;
	}
	
	/**
	 * Imports a trigram file, normalises the probabilities, and inserts each row into
	 * a hashtree using the format key = (word1,word2,word3), value = normalised probability
	 * @param fileName - location of the trigram file
	 * @return hashtable
	 */
	public Hashtable<String, Double> importTrigram(String fileName) {
		Hashtable<String, Double> table = new Hashtable<String, Double>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			ArrayList<String[]> tempList = new ArrayList<String[]>();
			String strLine;
			String w1 = "";
			String w2 = "";

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				if(w1.equals("") && w2.equals("")) {
					w1 = strLineArray[0];
					w2 = strLineArray[1];
					tempList.add(strLineArray);
				} else if(!w1.equalsIgnoreCase(strLineArray[0]) || !w2.equalsIgnoreCase(strLineArray[1])) {
					//changed to different i j
					//process templist
					double sum = 0;
					for(String[] s: tempList) {
						sum += Math.pow(10, Double.parseDouble(s[3]));
					}
					
					for(String[] s: tempList) {
						String word1 = s[0]; //word 1
						String word2 = s[1]; //word 2
						String word3 = s[2]; //word 3
						
						String key = word1+","+word2+","+word3; //e.g. key = "134,55,23"
						double prob = Double.parseDouble(s[3]);
						double value = (Math.pow(10, prob)/sum); //probability
						
						table.put(key, value);
					}
					
					//reset sum,list
					sum = 0;
					tempList.clear();
					//mark new start
					w1 = strLineArray[0];
					w2 = strLineArray[1];
					//add line
					tempList.add(strLineArray);
				} else if(w1.equalsIgnoreCase(strLineArray[0]) && w2.equalsIgnoreCase(strLineArray[1])) {
					tempList.add(strLineArray);
				}
					
			}
			
			//process anything still in tempList
			double sum = 0;
			for(String[] s: tempList) {
				sum += Math.pow(10, Double.parseDouble(s[3]));
			}
			
			for(String[] s: tempList) {
				String word1 = s[0]; //word 1
				String word2 = s[1]; //word 2
				String word3 = s[2]; //word 3
				
				String key = word1+","+word2+","+word3; //e.g. key = "134,55,23"
				double prob = Double.parseDouble(s[3]);
				double value = (Math.pow(10, prob)/sum); //probability
				
				table.put(key, value);
			}
			
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return table;
	}
	
	/**
	 * Returns all words in trigram that can result from x-2, x-1
	 * @param fileName - location of trigram file
	 * @param x-2
	 * @param x-1
	 * @return list of words
	 */
	public ArrayList<String> findTri(String fileName, String x_2, String x_1) {
		ArrayList<String> list = new ArrayList<String>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				String w1 = strLineArray[0];
				String w2 = strLineArray[1];
				
				if(x_2.equalsIgnoreCase(w1) && x_1.equalsIgnoreCase(w2)) {
					
					list.add(strLineArray[2]);
				}
			}
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Returns all words in bigram that can result from x-1
	 * @param fileName - location of bigram file
	 * @param x-1
	 * @return list of words
	 */
	public ArrayList<String> findBi(String fileName, String x_1) {
		ArrayList<String> list = new ArrayList<String>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				String w1 = strLineArray[0];
				
				if(x_1.equalsIgnoreCase(w1)) {
					
					list.add(strLineArray[1]);
				}
			}
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Returns all words in unigram
	 * @param fileName - location of unigram file
	 * @return list of words
	 */
	public ArrayList<String> findUni(String fileName) {
		ArrayList<String> list = new ArrayList<String>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			while ((strLine = br.readLine()) != null)   {
				String[] strLineArray = strLine.split("\\s+");
				list.add(strLineArray[0]);
			}
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Returns a hashtable of the vocab file, with format key = number, value = word
	 * @param fileName - location of the vocab file
	 * @return hashtable
	 */
	public Hashtable<String, String> importVocab(String fileName) {
		Hashtable<String, String> vocabTable = new Hashtable<String, String>();
		
		try{
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			while ((strLine = br.readLine()) != null)   {
				
				String[] strLineArray = strLine.split("\\s+");
				
				String key = strLineArray[0]; //number
				String value = strLineArray[1]; //word
				
				vocabTable.put(key, value);				
			}
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return vocabTable;
	}
	
}
