import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class Algorithms {

	private static List<List<Itemset>> positiveAP = null;
	private static List<List<Itemset>> negativeAP = null;
	private static List<List<Itemset>> positiveFP = null;
	private static List<List<Itemset>> negativeFP = null;
	private static int totalCount = ModifyDataSets.totalCount;
	private static int totalCountPos = ModifyDataSets.totalCountPos;
	private static int totalCountNeg = ModifyDataSets.totalCountNeg;
	public static ArrayList<String> finalResults = new ArrayList<String>();
	
	public static void Apriori(double minSupPos, double minSupNeg, double minConf, String posData, String negData, boolean printStatus, boolean saveOutput, String outData) {
		AlgoApriori minePos = new AlgoApriori(); 
		AlgoApriori mineNeg = new AlgoApriori(); 
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			PrintStream old = System.out;
			System.setOut(ps);
			BufferedWriter writer1 = new BufferedWriter(new FileWriter(outData));
			System.out.println(ModifyDataSets.attArray.toString()+"\n");
			System.out.println("*******************Apriori Positive***********************");
			Itemsets setPos = minePos.runAlgorithm(minSupPos, posData, null);
//			minePos.printStats();
			List<List<Itemset>> posPatternList = setPos.getLevels();			
			positiveAP = posPatternList;
			if(saveOutput) {
				// Print some output: goes to your special stream
				minePos.printStats();
				printInfo(setPos, posPatternList);
				
			}
			System.out.println("\n\n*******************Apriori Negative***********************");
			Itemsets setNeg = mineNeg.runAlgorithm(minSupNeg, negData, null);
//			mineNeg.printStats();
			List<List<Itemset>> negPatternList = setNeg.getLevels();
			negativeAP = negPatternList;
			if(saveOutput) {
				// Print some output: goes to your special stream
				mineNeg.printStats();
				printInfo(setNeg, negPatternList);
				// Put things back
				System.out.flush();
				System.setOut(old);
				// Show what happened
//				writer1.write(baos.toString());
				
			}
			if(printStatus) {
				printInfo(setPos, posPatternList);
				printInfo(setNeg, negPatternList);
			}
			System.out.close();
			System.setOut(ps);
			System.out.println("\n\n");
			List<List<Itemset>> combinedAP = compareList(positiveAP, negativeAP);
			checkMinConf(combinedAP,minConf);
			System.out.flush();
			System.setOut(old);
			writer1.write(baos.toString());
			writer1.write("Total count: " + ModifyDataSets.totalCount);
			writer1.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void FPGrowth(double minSupPos, double minSupNeg, double minConf, String posData, String negData, boolean printStatus, boolean saveOutput, String outData) {
		AlgoFPGrowth minePos = new AlgoFPGrowth(); 
		AlgoFPGrowth mineNeg = new AlgoFPGrowth(); 

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			PrintStream old = System.out;
			System.setOut(ps);
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(outData));
			System.out.println(ModifyDataSets.attArray.toString()+"\n");
			System.out.println("*******************FP Growth Positive***********************");
			Itemsets setPos = minePos.runAlgorithm(posData, null, minSupPos);
//			minePos.printStats();
			List<List<Itemset>> posPatternList = setPos.getLevels();			
			positiveFP = posPatternList;
			if(saveOutput) {
				// Print some output: goes to your special stream
				minePos.printStats();
				printInfo(setPos, posPatternList);
				
			}
			System.out.println("\n\n*******************FP Growth Negative***********************");
			Itemsets setNeg = mineNeg.runAlgorithm(negData, null, minSupNeg);
//			mineNeg.printStats();
			List<List<Itemset>> negPatternList = setNeg.getLevels();
			negativeFP = negPatternList;
			if(saveOutput) {
				// Print some output: goes to your special stream
				mineNeg.printStats();
				printInfo(setNeg, negPatternList);
				// Put things back
				System.out.flush();
				System.setOut(old);
				// Show what happened
//				writer1.write(baos.toString());
				
			}
			if(printStatus) {
				printInfo(setPos, posPatternList);
				printInfo(setNeg, negPatternList);
			}
			System.out.close();
			System.setOut(ps);
			System.out.println("\n\n");
			List<List<Itemset>> combinedFP = compareList(positiveFP, negativeFP);
			checkMinConf(combinedFP,minConf);
			System.out.flush();
			System.setOut(old);
			writer2.write(baos.toString());
			writer2.write("Total count: " + ModifyDataSets.totalCount);
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printInfo(Itemsets set, List<List<Itemset>> patternList) {
		System.out.println("Itemsets count: " + set.getItemsetsCount());
		int patternCount = 0;
		int levelCount = 0;
		// for each level (a level is a set of itemsets having the same number of items)
		for (List<Itemset> level : set.getLevels()) {
			// print how many items are contained in this level
			System.out.println("  L" + levelCount + " ");
			// for each itemset
			for (Itemset itemset : level) {
				// print the itemset
				System.out.print("  pattern " + patternCount + ":  ");
				itemset.print();
				// print the support of this itemset
				System.out.print("support :  " + itemset.getAbsoluteSupport());
				patternCount++;
				System.out.println("");
			}
			levelCount++;
		}
		System.out.println(" --------------------------------");
		
		for(int i = 0; i<patternList.size(); i++){
			System.out.println(patternList.get(i));
		}
	}
	
	private static void checkMinConf(List<List<Itemset>> combined, double minConf) {
		int support = 0;
		float conf = 0;
		int itemSupport = 0; 
		int posNeg = 0; //Starts with 0 for positive then switches to negative at 1;
		float newMinConf = (float)minConf;
		for(List<Itemset> list: combined) {
			if(list.size()==0) {
				if(posNeg==0) {
					support = totalCountPos;
				} else if(posNeg==1) {
					support = totalCountNeg;
				}
				posNeg=1;
			}
			for(int i = 0; i<list.size(); i++) {
				itemSupport = list.get(i).support;
				conf = ((float)itemSupport/(float)support);
//				System.out.println(list.get(i) + "\t Support: " + itemSupport + "\t Confidence: " + conf); 
				if(conf >= newMinConf) {
					System.out.println(list.get(i) + "\t Support: " + itemSupport + "\t Confidence: " + conf );
					finalResults.add(list.get(i) + "\t Support: " + itemSupport + "\t Confidence: " + conf );
				}
			}
		}
		
	}
	private static List<List<Itemset>> compareList(List<List<Itemset>> positive, List<List<Itemset>> negative) {

		for (List<Itemset> listPos: positive) {
			for (List<Itemset> listNeg: negative) {
				for(int i = 0; i<listPos.size(); i++) {
					for(int j = 0; j<listNeg.size(); j++) {
						if(listNeg.get(j)==listPos.get(i)) {
							listNeg.remove(j);
							listPos.remove(i);
						} 
					}
				}
			}
		}
		
		boolean combined = positive.addAll(negative);
		
		return positive;
		
	}
	

	
	public static List<List<Itemset>> getPostiveListApriori() {
		return positiveAP;
	}
	
	public static List<List<Itemset>> getNegativeListApriori() {
		return negativeAP;
	}
	
	public static List<List<Itemset>> getPostiveListFPGrowth() {
		return positiveFP;
	}
	
	public static List<List<Itemset>> getNegativeListFPGrowth() {
		return negativeFP;
	}

}
