import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ModifyDataSets {
	
		public static ArrayList<String> attArray = new ArrayList<String>();
		private static ArrayList<ArrayList<String>> originalDataNumbers = new ArrayList<ArrayList<String>>();
		private static ArrayList<ArrayList<String>> changedDataNumbers = new ArrayList<ArrayList<String>>();
		private static String posnegString;
		private static int numAtt = 1;
		private static int posNumber = 0;
		private static boolean isPos = false;
		private static int attNameCount = 0;
		public static int totalCount;
		public static int totalCountPos;
		public static int totalCountNeg;
				
		
		// Reads file and converts strings/chars to numbers for the apriori algorithm.
		public static void fileModified(String filename, String att, String posnum, String posData, String negData, String modifiedData) {
			
			
			try{
				String newFile = modifiedData;
				BufferedReader read = new BufferedReader(new FileReader(filename));
				BufferedWriter write1 = new BufferedWriter(new FileWriter(newFile));
				BufferedWriter writepos = new BufferedWriter(new FileWriter(posData));
				BufferedWriter writeneg = new BufferedWriter(new FileWriter(negData));
				
				String line;
				String lineHolder;
				int countAttPlace = 0;
				totalCount = 0;
				totalCountPos = 0;
				totalCountNeg = 0;
				
				while((line = read.readLine()) != null){
					if (line.isEmpty()){
						continue;
					} else if (line.charAt(0)=='@'){
						checkLineInfo(line, att, posnum, countAttPlace);
						countAttPlace++;
					} else {
						lineHolder = modifyData(line, attNameCount);
						write1.write(lineHolder);
						if (isPos){
							writepos.write(posnegString);
							totalCountPos++;
						} else {
							writeneg.write(posnegString);
							totalCountNeg++;
						}
						totalCount++;
					}
				}//end while
				write1.close();
				writepos.close();
				writeneg.close();
				System.out.println(attArray.toString());
				System.out.println(totalCount);
//				System.out.println(originalDataNumbers.toString());
//				System.out.println(changedDataNumbers.toString());
//				System.out.println(attSize.toString());
				
			} catch (IOException e){
				System.out.println("There was a problem: " + e);
			    e.printStackTrace();
			}
		}//end fileModified

		private static String modifyData(String line, int attNameInt) {
			
			String newStr = "";
			int beginIndex = 0;				//beginning of substring
			int attIndex = 0;				//the current attribute in the line
			isPos = false;
			boolean realValue = false;
			posnegString = "";
			
			for (int index = 0; index<line.length(); index++){
				if (line.charAt(index)==','){
					String temp = line.substring(beginIndex, index);
					String posTemp = "0";
					for (int arrIndex = 0; arrIndex<originalDataNumbers.get(attIndex).size(); arrIndex++){
						if (temp.equals(originalDataNumbers.get(attIndex).get(arrIndex))){
							newStr = newStr + changedDataNumbers.get(attIndex).get(arrIndex) + " ";
							posTemp = changedDataNumbers.get(attIndex).get(arrIndex);
							realValue = true;
							if (attNameInt != attIndex) {
								posnegString = posnegString + changedDataNumbers.get(attIndex).get(arrIndex) + " ";
							}
						} 
						if (attNameInt == attIndex && posNumber == Integer.valueOf(posTemp)){  
							isPos = true;
						}
					}
					if (!realValue){
						newStr = newStr + "0" + " ";
					}
					beginIndex = index + 1;
					attIndex ++;
					realValue = false;
				} else if (index == line.length()-1){
					String temp = line.substring(beginIndex, index+1);
					String posTemp = "0";
					for (int arrIndex = 0; arrIndex<originalDataNumbers.get(attIndex).size(); arrIndex++){
						if (temp.equals(originalDataNumbers.get(attIndex).get(arrIndex))){
							newStr = newStr + changedDataNumbers.get(attIndex).get(arrIndex) + "\n";
							posTemp = changedDataNumbers.get(attIndex).get(arrIndex);
							realValue = true;
							if (attNameInt != attIndex) {
								posnegString = posnegString + changedDataNumbers.get(attIndex).get(arrIndex) + " ";
							} 
						}
						if (attNameInt == attIndex && posNumber == Integer.valueOf(posTemp)){
							isPos = true;
						}
					}
					
					if (!realValue){
						newStr = newStr + "0" + "\n";
					}
					posnegString = posnegString + "\n";
					realValue = false;
				}
			}
			return newStr;
			
		}

		private static void checkLineInfo(String line, String att, String posnum, int countAttPlace){
			
			//Splitting the string
			String splitString = line.split("@")[1];
			String [] nextSplit = splitString.split(":",2);
			
			if (Character.toLowerCase(nextSplit[0].charAt(0)) !='d'){	//Check attribute parameters that are not data
				attArray.add( "\n" + nextSplit[0] + ":");
				if(att.equalsIgnoreCase(nextSplit[0].split("\\s")[1])) {
					attNameCount = countAttPlace;
				}
				createNumbers(nextSplit[1]);				
			}
			if (originalDataNumbers.size()-1 == attNameCount){
				for (int index = 0; index<originalDataNumbers.get(attNameCount).size(); index++){
					if (originalDataNumbers.get(attNameCount).get(index).equalsIgnoreCase(posnum)){
						posNumber = Integer.valueOf(changedDataNumbers.get(attNameCount).get(index));
					}
				}
			}
		
		}//end checkLineInfo


		private static void createNumbers(String str) {
			
//			int size = 0;
			ArrayList<String> arrStrOld = new ArrayList<String>();
			ArrayList<String> arrStrNew = new ArrayList<String>();
			int startNum = 0;
			
			for (int numIndex = 0; numIndex<str.length(); numIndex++){
				if (str.charAt(numIndex)==','){
					String temp = str.substring(startNum, numIndex);
					arrStrOld.add(temp);
					arrStrNew.add(String.valueOf(numAtt));
					attArray.add(str.substring(startNum,numIndex) + "=" + numAtt);
					startNum = numIndex+1;
					numAtt++;
//					size++;
				} else if (numIndex == str.length()-1){
					arrStrOld.add(str.substring(startNum,numIndex+1));
					arrStrNew.add(String.valueOf(numAtt));
					attArray.add(str.substring(startNum,numIndex+1) + "=" + String.valueOf(numAtt));
					numAtt++;
//					size++;
				}//end if
			}//end for
			
//			attSize.add(size);
//			
			originalDataNumbers.add(arrStrOld);
			changedDataNumbers.add(arrStrNew);
			
		}//end createNumbers

		
		
}
