package compressor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CompressionAlgorithm {
	//Prints out ASCII for string, it is just for comparison 
	private static void printASCII(String string)
	{
		System.out.println("STRING: " + string);
		//https://www.programiz.com/java-programming/examples/ascii-value-character//	
		String whole = "";
		Integer size = 0;
		for (int i=0;i<string.length();i=i+1) {
			whole = whole + (int)string.charAt(i) + " ";
			size = size+1;
		}
		System.out.println("OLD "+size+"::: "+ whole );
	}
		
	public static String compress(String string) {		
		printASCII(string);
		Integer size = 0;		
		Map<String,Integer> compressedDict = new HashMap<String,Integer>();			
		int currentDictSpot = 256;
		String output = "";
		String fullCharString = "";
		String InitialCharString = "";
		
		for (int i=0;i<string.length();i=i+1) {
			fullCharString = "" + string.charAt(i);
			int ASCIISpot = (int)string.charAt(i);
			int distance = 1;
			int addToIVar = 0;
			
			if (i+1 == string.length()){			
				//Final spot
				output = output + ASCIISpot + " ";
				size = size +1;
				//System.out.println(output);
			} else {
				while (true) {
					if (i+distance!=string.length()) {
						fullCharString = fullCharString + string.charAt(i+distance);
						InitialCharString  = ""+ string.charAt(i) + string.charAt(i+1);
						if (compressedDict.containsKey(fullCharString)) {
							//There is a match from before
							ASCIISpot = compressedDict.get(fullCharString);							
							addToIVar = addToIVar+1;
						} else {
							//Regular Case, no matches found
							
							//VV ALSO IMPORTANT, stops thing from adding extra dictonary entries
							if (fullCharString.equals(InitialCharString) ==true ){
								compressedDict.put(fullCharString, currentDictSpot);
							}
							size = size +1;
							output = output + ASCIISpot + " ";
							//System.out.println(output);	
							i = i+addToIVar;
							///System.out.println(fullCharString + currentDictSpot);
							break;
						}						
					} else	{
						//No more characters, stop looking for matches
						compressedDict.put(fullCharString, currentDictSpot);
						output = output + ASCIISpot + " ";
						//System.out.println(output);					
						break;
					}
				}
			}			
			currentDictSpot = currentDictSpot+1;
		}	
		System.out.println("NEW "+ size + "::: " +output);
		return output;
	}

	public static String decompress(String strings) {
		//https://www.geeksforgeeks.org/split-string-java-examples///	
		//https://beginnersbook.com/2013/12/how-to-convert-string-to-int-in-java/
		String[] newStr = strings.split(" "); 
		Integer size = 0;
		
		//Save number first so that code can scan for things to break down	
		Map<Integer,String[]> compressedDict = new HashMap<Integer,String[]>();	
		int currentDictSpot = 256;
		String output = "";
		
		for (int i=0;i<newStr.length;i=i+1) {	
			
			String[] outputAdder;
			String[] newDictValue;
			String addOn = "";
			
			//**First step is to break down large number//
			if (Integer.parseInt(newStr[i])>255) {				
				newDictValue = new String[compressedDict.get(Integer.parseInt(newStr[i])).length+1];				
				for (int b=0;b<compressedDict.get(Integer.parseInt(newStr[i])).length;b=b+1) {
					newDictValue[b]=compressedDict.get(Integer.parseInt(newStr[i]))[b];
				}
				
				outputAdder = compressedDict.get(Integer.parseInt(newStr[i]));
			} else {
				outputAdder = new String[1];
				outputAdder[0]=newStr[i];
				
				newDictValue = new String[2];
				newDictValue[0]=newStr[i];
			}
						
						
			//Begin loop
			if (i+1 == newStr.length){			
				//Final spot
				
				for (int b=0;b<outputAdder.length;b=b+1) {
					output = output + outputAdder[b] + " ";
					size = size +1;
				}
				break;
			} else {				
				//Check if next integer is a large number
				if (Integer.parseInt(newStr[i+1])>255) {
					if (compressedDict.containsKey(Integer.parseInt(newStr[i+1]))) {
						addOn = compressedDict.get(Integer.parseInt(newStr[i+1]))[0];
					}else{
						//IMPORTANT VVVV, if three consecutive chars are the same ("sss"), it will break it.
						//If there is nothing in the dictonary, we can assume that it is repeating.
						addOn = newDictValue[0];
					}
				} else {
					addOn = newStr[i+1];					
				}
				
				newDictValue[newDictValue.length-1]=addOn;
				compressedDict.put(currentDictSpot,newDictValue);	
				
				//copy newDictValue to output
				for (int b=0;b<outputAdder.length;b=b+1) {
					output = output + outputAdder[b]+ " ";
					size = size + 1;
				}
//				System.out.println(output);
			}
			currentDictSpot = currentDictSpot+1;		
		}
		System.out.println("UNC "+ size + "::: " +output);
		return output;
	}
	
	
	 public static void main(String[] args) {
		 String string = compress("thisisthe");	 
		 //Convert to Binary (to save);
		 string = decompress(string);	
		 
		 String string2 = compress("Last Friday I saw a spotted striped blue worm shake hands with a legless lizard.\n"
		 		+ "Pair your designer cowboy hat with scuba gear for a memorable occasion.\n"
		 		+ "Please wait outside of the house.");
		 string2 = decompress(string2);	

		 
	 }
}