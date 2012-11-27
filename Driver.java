import java.util.ArrayList;
import java.util.Hashtable;

public class Driver {

	//location of data files
	static String vocabFile = "vocab.txt";
	static String unigramFile = "unigram_counts.txt";
	static String bigramFile = "bigram_counts.txt";
	static String trigramFile = "trigram_counts.txt";

	FileHandler file;
	Hashtable<String, String> vocabTable;
	Hashtable<String, Double> unigramTable;
	Hashtable<String, Double> bigramTable;
	Hashtable<String, Double> trigramTable;

	public Driver() {		
		file = new FileHandler();
		vocabTable = new Hashtable<String, String>();
		unigramTable = new Hashtable<String, Double>();
		bigramTable = new Hashtable<String, Double>();
		trigramTable = new Hashtable<String, Double>();

		vocabTable = file.importVocab(vocabFile);
		unigramTable = file.importUnigram(unigramFile);
		bigramTable = file.importBigram(bigramFile);
		trigramTable = file.importTrigram(trigramFile);
	}

	public static void main(String[] args) {
		Driver d = new Driver();
		
		if(args[0].equalsIgnoreCase("-g")) {
			/**
			 * produce random sentence
			 */
			ArrayList<String> words = d.priorSample();
			for(String s: words) {
				System.out.print(d.vocabTable.get(s)+" ");
			}
		} else if(args[0].equalsIgnoreCase("-c")) {
			/**
			 * correct noisy input
			 */
			String toCorrect = "";
			//build sentence to correct from args
			for(int i=1; i<args.length; i++){
				//i=1 to skip first arg
				toCorrect += i == 1 ? args[i] : " "+args[i]; //if its the first word, then don't append " "
			}
			ArrayList<String> corrected = d.correct(toCorrect);
			for(String s: corrected) {
				System.out.print(d.vocabTable.get(s)+" ");
			}
		}
		
	}

	/**
	 * Attempts to correct noisy input sentences by using the Viterbi algorithm
	 * @param s
	 * @return
	 */
	private ArrayList<String> correct(String input) {
		String[] sentence = input.split("\\s+");
		ArrayList<String> words = new ArrayList<String>();
		
		//x0 = <s>
		words.add("153");
		
		//for each word in the input sentence
		for(int count = 0; count<sentence.length; count++) {
			String currentWord = words.get(words.size()-1);
			
			//look at bigram to get word possibilities of real word
			ArrayList<String> next = file.findBi(bigramFile, currentWord);
			
			double highest = 0; //the highest probability for a word so far
			String temp = ""; 	//the word associated with the prob
			
			//for each next word possible (for this position)
			for(String nextWord: next) {
				int editDistance = computeDistance(sentence[count], vocabTable.get(nextWord));
				double bigramProb = Math.log10(bigramTable.get(currentWord+","+nextWord));
				double editProb = (editDistance*Math.log10(0.01))-(Math.log10(factorial(editDistance)));
				
				//using logsum
				double prob = Math.pow(10, bigramProb+editProb);
				
				//check if this word is the most likely
				if(prob > highest) {
					highest = prob;
					temp = nextWord;
				}
			}
			//add corrected word to list to return
			words.add(temp);
		}
		return words;
	}
	
	/**
	 * Returns n!
	 * @param n
	 * @return n!
	 */
	private int factorial(int n) {
		  if (n == 0) 
			  return 1;
	      return n * factorial(n-1);
	}
	
	/**
	 * The Levenshtein distance between two strings is defined as the minimum number of edits needed 
	 * to transform one string into the other, with the allowable edit operations being insertion, 
	 * deletion, or substitution of a single character.
	 * Found at: http://rosettacode.org/wiki/Levenshtein_distance#Java
	 * @param s1 - String 1
	 * @param s2 - String 2
	 * @return int - the edit distance
	 */
	private int computeDistance(String s1, String s2) {
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();
	 
	    int[] costs = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        costs[s2.length()] = lastValue;
	    }
	    return costs[s2.length()];
	  }
	
	/**
	 * The aux method for priorSample()
	 * @param words
	 * @return
	 */
	private ArrayList<String> priorSampleAux(ArrayList<String> words) {
		ArrayList<String> next = new ArrayList<String>();

		if(words.get(words.size()-1).equalsIgnoreCase("152")) {
			//if the last word is </s> then end
			return words;
		}

		//find xt | xt-1, xt-2
		String xt_1 = words.get(words.size()-1);
		String xt_2 = words.get(words.size()-2);
		next = file.findTri(trigramFile, xt_2, xt_1);

		if(!next.isEmpty()) {
			double rand = Math.random();
			double sum = 0;
			for(String s: next) {
				//randomly pick the next word based on probabilities
				double prob = trigramTable.get(xt_2+","+xt_1+","+s);
				sum += prob;
				if(rand <= sum) {
					words.add(s);
					break;
				}
			}
		} else {
			//combination doesnt exist in trigram, "back off" to bigram
			next = file.findBi(bigramFile, xt_1);

			if(!next.isEmpty()) {
				double rand = Math.random();
				double sum = 0;
				for(String s: next) {
					double prob = bigramTable.get(xt_1+","+s);
					sum += prob;
					if(rand <= sum) {
						words.add(s);
						break;
					}
				}
			} else {
				//combination doesnt exist in bigram, "back off" to unigram
				next = file.findUni(unigramFile);

				if(!next.isEmpty()) {
					double rand = Math.random();
					double sum = 0;
					for(String s: next) {
						double prob = unigramTable.get(s);
						sum += prob;
						if(rand <= sum) {
							words.add(s);
							break;
						}
					}
				}
			}

		}

		return this.priorSampleAux(words);
	}

	/**
	 * Generates a sentence based on the text corpus
	 * @return ArrayList<String> words - list of words (in order), to form a generated sentence
	 */
	private ArrayList<String> priorSample() {
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> next = new ArrayList<String>();

		//add x0 = <s>
		String start = "153";
		words.add(start);

		//get x1 from bigram
		next = file.findBi(bigramFile, words.get(words.size()-1)); //gets the list of words possible after <s>

		double rand = Math.random();
		double sum = 0;
		for(String s: next) {
			//pick the next word at random (weighted by probability)
			double prob = bigramTable.get(start+","+s);
			sum += prob;
			if(rand <= sum) {
				words.add(s);
				break;
			}
		}

		return priorSampleAux(words);
	}

}
