import java.util.List;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;

public class MARFS {

	public static void main(String[] args) {
//     User modifies the following
		String filename = "lung_cancer.data";
		String decisionAttribute = "d";  		//the data value
		String positiveNumber = "1";		//positive value. Can be number or string
//		String negativeNumber = "R";		//negative value. Can be number or string
		double minSupPos = 0.3;  //minsup for positive group
		double minSupNeg = 0.3;  //minsup for negative group
		double minConf = 0.5;
		boolean printStatus = false; //if true prints out the itemset count, pattern, support
		boolean saveOutput = true;
		
		
		//pos/neg filenames
		String posDataFileSuffix = "_pos";
		String negDataFileSuffix = "_neg";
//	   End of user changes		
		
		
		String[] fileSplit = filename.split("\\.");
		String posData = fileSplit[0] + posDataFileSuffix + "." + fileSplit[1];
		String negData = fileSplit[0] + negDataFileSuffix + "." + fileSplit[1];
		String modifiedData = fileSplit[0] + "_modified" + "." + fileSplit[1];
		String outDataAp = fileSplit[0] + "_outputApriori" + "." + fileSplit[1];
		String outDataFP = fileSplit[0] + "_outputFPGrowth" + "." + fileSplit[1];
	
		ModifyDataSets.fileModified(filename, decisionAttribute, positiveNumber, posData, negData, modifiedData);
		
		Algorithms.Apriori(minSupPos, minSupNeg, minConf, posData, negData, printStatus, saveOutput, outDataAp);
		
		Algorithms.FPGrowth(minSupPos, minSupNeg, minConf, posData, negData, printStatus, saveOutput, outDataFP);
		
		
	}
		

}
