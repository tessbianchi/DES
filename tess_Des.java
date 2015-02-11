import java.lang.String;
import java.util.*;
import java.io.*;
import java.lang.*;

public class tess_Des{
	static String inputtext = "p.txt";//Default input
	static String outputtext = "c.txt";//Default output
	static String paramtext = "param.txt";//Default param
	static String keytext = "key.txt";
	static boolean showSteps = false;
	static boolean hexadec = false;
	static boolean encrypt = true;



	static String ptxt;//make this one giant string of ptxt
	static String ctxt;
	static String key = "";
	static int blockSize;
	static int keysize;
	static int effkeysize;
	static int roundkeysize;
	static int numOfRounds;
	static int [] pc1_key;
	static int [] pc2_key;
	static int [] rotationsched_key;
	static int [] initialPerm;
	static int [] expansionPerm;
	static int [] pBox;
	static int numberOfSboxes;
	static int [] rowSelection;
	static int [] colSelection;
	static int [][][] sBoxes;

	static int x;
	static int y; 

	/*
	REPORT RIGIDNESS HERE
	-automatically set to encrypt
	-Make sure you space after each flag 
	-Won't encrypt correctly if plaintext has spaces
	-Assumes (mostly) correct input (e.g. sboxes will be of proper size and contain numbers in the proper range)

	*/
public static String hexToBinary(String hex) {
	String temp= "";
	for(int i = 0; i != hex.length(); ++i){
	    int j = Integer.parseInt(hex.substring(i,i+1), 16);
	    String bin = Integer.toBinaryString(j);
	    int len = bin.length();
	    if(len == 1){
	    	bin = "000" + bin;
	    }else if(len == 2){
	    	bin = "00" + bin;
	    }else if(len == 3){
	    	bin = "0" + bin;
	    }
	    temp += bin;
	    System.out.println(hex.substring(i,i+1) + " " + bin);
	}
	return temp; 
}
public static String BinToHex(String bin) {//fix dis
	String tmp = "";
	for(int i = bin.length()-1; i >= 0; i = i-3){
		int j;
		if(i<3)
			j = Integer.parseInt(bin.substring(0,i+1), 2);
		else
			j = Integer.parseInt(bin.substring(i-3,i+1), 2);
		
	    String hex = Integer.toHexString(j);
	    hex += tmp;		//concatenating 
		tmp = hex;
	    //System.out.println(hex.substring(i,i+1)+ " " + bin);
	}

	//int bint= Integer.parseInt(bin,2);
	return tmp; 
}
public static String pBox(String p){
	String temp; 
	char [] tempchar = new char[blockSize/2];
	int l;
	for(int i = 0; i != blockSize/2; ++i){
		l = pBox[i];
		tempchar[i]= p.charAt(l);
	}
	temp = String.valueOf(tempchar);
	return temp;
}
public static String initialpermute(String p){
	String temp;
	char [] tempchar = new char[blockSize];
	int l;
	for(int i = 0; i != blockSize; ++i){
		l = initialPerm[i];
		tempchar[i]= p.charAt(l);
	}
	temp = String.valueOf(tempchar);
	return temp;
} 
public static String initialPermutationKey(String key){
	String temp;
	char [] tempchar = new char[keysize];
	int l;
	for(int i = 0; i != keysize; ++i){
		l = pc1_key[i];
		tempchar[i]= key.charAt(l);
	}
	temp = String.valueOf(tempchar);
	return temp;
}
public static String expansionPermutation(String right){
	String temp;
	char [] tempchar = new char[blockSize];
	int l;
	for(int i = 0; i!=blockSize; ++i){
		l = expansionPerm[i];
		tempchar[i]= right.charAt(l);
	}
	temp = String.valueOf(tempchar);
	return temp;
}
public static String getInput(String inputFile){
	try{
		System.setIn(new FileInputStream(inputFile));		
	}
	catch(FileNotFoundException ex){
		System.out.println("Input file can not be found.");
	}
	Scanner scanner = new Scanner(System.in);
	scanner.useDelimiter(" ");  
	String input = scanner.next(); 
	return input;	
}
public static void getParams(String paramFile){
	try{
		System.setIn(new FileInputStream(paramFile));		
	}
	catch(FileNotFoundException ex){
		System.out.println("Input file can not be found.");
	}
	Scanner scanner = new Scanner(System.in);
	scanner.reset(); 
	
	while(!scanner.hasNextInt())	//We are assuming that if a line contains
		scanner.nextLine();			//a parameter, it will begin with said parameter
									//as comments would be after it
	
	//block size
	blockSize = scanner.nextInt();
	while(!scanner.hasNextInt())	
		scanner.nextLine();												
	
	//key size
	keysize = scanner.nextInt();
	while(!scanner.hasNextInt()) 
		scanner.nextLine();
	
	//Effective key size
	effkeysize = scanner.nextInt();
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//round key size
	roundkeysize = scanner.nextInt();
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//number of rounds
	numOfRounds = scanner.nextInt();
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	
	pc1_key = new int[effkeysize];
	pc2_key = new int[roundkeysize];
	rotationsched_key = new int[numOfRounds];
	initialPerm = new int[blockSize];
	expansionPerm = new int[blockSize];
	pBox = new int[blockSize/2];
	
	
	//Initial Permuted Choice for Round Key Generation PC-1
	for(int i = 0; i!=effkeysize; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for PC-1 does not contain enough integers or input is incorrect format.");
		}
		pc1_key[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//Permuted Choice to set bits for Round Key PC-2
	for(int i = 0; i!=roundkeysize; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for PC-2 does not contain enough integers or input is incorrect format. (i.e. non-integer types)");
			
		}
		pc2_key[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//Left Rotation Schedule RS
	for(int i = 0; i!=numOfRounds; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for Rotation Scheduler does not contain enough integers or input is incorrect format.");
		}
		rotationsched_key[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//initial Permutation IP
	for(int i = 0; i!=blockSize; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for IP does not contain enough integers or input is incorrect format.");
		}
		initialPerm[i] = scanner.nextInt()-1;
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//expansionPerm = new int[blockSize];
	//expansion Permutation EP
	for(int i = 0; i!=blockSize; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for EP does not contain enough integers or input is incorrect format.");
		}
		expansionPerm[i] = scanner.nextInt()-1;
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//P-box transposition Permutation EP
	for(int i = 0; i!=blockSize/2; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for P-box does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		pBox[i] = scanner.nextInt()-1;
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	numberOfSboxes = scanner.nextInt();
	while(!scanner.hasNextInt())
		scanner.nextLine();
		
		
	x = roundkeysize/numberOfSboxes - blockSize/2/numberOfSboxes;
	y = blockSize/2/numberOfSboxes;
	rowSelection = new int[x];
	colSelection = new int[y];
	
	//row
	for(int i = 0; i != x; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for row does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		rowSelection[i] = scanner.nextInt()-1;
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//col
	for(int i = 0; i != y; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for col does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		colSelection[i] = scanner.nextInt()-1;
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	sBoxes = new int[numberOfSboxes][(int)Math.pow(2,x)][(int)Math.pow(2,y)];
	
	for(int i = 0; i != numberOfSboxes; ++i){
		for(int j = 0; j!= sBoxes[i].length; ++j){
			for(int k = 0; k != sBoxes[i][j].length; ++k){
				if(!scanner.hasNextInt()){
					System.out.println("Error: Input for sBox "+(i+1)+" does not contain enough integers or input is incorrect format.");
					System.out.println(scanner.next());
				}
				sBoxes[i][j][k] = scanner.nextInt();
			}
			scanner.nextLine();
		}
		if(i != numberOfSboxes - 1){
			while(!scanner.hasNextInt())
				scanner.nextLine();
		}
	}
	
	
	
	printUsefulData();
	
	//return void;	
}
public static void printUsefulData(){ // to make programming easier
		
	System.out.println("blockSize: "+ blockSize);
	System.out.println("keysize: "+ keysize);
	System.out.println("effkeysize: "+ effkeysize);
	System.out.println("roundkeysize: "+ roundkeysize);
	System.out.println("numOfRounds: "+ numOfRounds);
		
	System.out.println("pc1_key: ");
	for(int i = 0; i!=pc1_key.length; ++i)
		System.out.print(pc1_key[i]+" ");
	System.out.println();
	
	System.out.println("pc2_key: ");
	for(int i = 0; i!=pc2_key.length; ++i)
		System.out.print(pc2_key[i]+" ");
	System.out.println();
	
	System.out.println("rotationsched_key: ");
	for(int i = 0; i!=rotationsched_key.length; ++i)
		System.out.print(rotationsched_key[i]+" ");
	System.out.println();
	
	System.out.println("initialPerm: ");
	for(int i = 0; i!=initialPerm.length; ++i)
		System.out.print(initialPerm[i]+" ");
	System.out.println();
	
	System.out.println("expansionPerm: ");
	for(int i = 0; i!=expansionPerm.length; ++i)
		System.out.print(expansionPerm[i]+" ");
	System.out.println();
	
	System.out.println("pBox: ");
	for(int i = 0; i!=pBox.length; ++i)
		System.out.print(pBox[i]+" ");
	System.out.println();
	
	System.out.println("number of S-boxes: "+ numberOfSboxes);
	System.out.println();
	
	System.out.println("Row: ");
	for(int i = 0; i!=rowSelection.length; ++i)
		System.out.print(rowSelection[i]+" ");
	System.out.println();
	
	System.out.println("Col: ");
	for(int i = 0; i!=rowSelection.length; ++i)
		System.out.print(colSelection[i]+" ");
	System.out.println();
	System.out.println();
	
	for(int i = 0; i != numberOfSboxes; ++i){
		System.out.println("Sbox "+(i+1));
		for(int j = 0; j!= sBoxes[i].length; ++j){
			for(int k = 0; k != sBoxes[i][j].length; ++k){
				System.out.print(sBoxes[i][j][k]+" ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
	
	
	//static int numberOfSboxes;
}



public static String leftShift(String bin){
	String temp;
	int length = bin.length();
	char [] tempchar = new char[length];
	for(int i = 0; i != length; ++i){
		tempchar[i]= bin.charAt((i+1)%length);
	}
	temp = String.valueOf(tempchar);
	return temp;
}

public static String rightShift(String bin){
	String temp;
	int length = bin.length();
	char [] tempchar = new char[length];
	int l;
	for(int i = 0; i != length; ++i){
		l = initialPerm[i];
		tempchar[i]= bin.charAt((i-1)%length);
	}
	temp = String.valueOf(tempchar);
	return temp;
}

public static String encrypt(){
	if(hexadec){
		ptxt = hexToBinary(ptxt);
		key = hexToBinary(key);
	}
	int len = ptxt.length();
	String ans = ""; 
	String temp = "";
	String block_of_text = "" ;
	String right = "";
	String left= ""; 
	String key_right = "";
	String key_left = "";

	key = initialPermutationKey(key); //!!!
	key_right = key.substring(0,keysize/2);
	key_left = key.substring(keysize/2,keysize);
	for(int i = 0; i < len; i = i+blockSize){
		if((i + blockSize) > len){//if this is the last one and it is too short
				int l = len - i;
				block_of_text = ptxt.substring(i,len);
				for(int k = l; k!= blockSize; ++k){
					block_of_text = "0" + block_of_text;
				}
				block_of_text = initialpermute(block_of_text);
			}else{
				block_of_text = ptxt.substring(i,i+blockSize);
				block_of_text = initialpermute(block_of_text);
		}
		right = block_of_text.substring(0,blockSize/2);
		left = block_of_text.substring(blockSize/2,blockSize);
		for(int j = 0; j!= numOfRounds; ++j){
			key_right = leftShift(key_right);
			key_right = leftShift(key_right);
			key = secondPermutationKey(key_right,key_right);
			temp = right;
			right = expansionPermutaion(right);
			right = xor(right,key);
			right = sbox(right);
			right = pBox(right);
			right = xor(right,left);
			left = temp;
		}
		temp = right;
		right = left;
		left = temp;
		temp = right+left;
		temp = inverseInitialPerm(temp);
		ans += temp;
	}
return ans;

}

public static void decrypt(){
	if(hexadec){
		ptxt = hexToBinary(ptxt);
		key = hexToBinary(key);
	}
	int len = ptxt.length();
	String ans = ""; 
	String temp = "";
	String block_of_text = "" ;
	String right = "";
	String left= ""; 
	String key_right = "";
	String key_left = "";
	String tempkey = "";
	String tempkey_r = "";
	String tempkey_l = "";


	key = initialpermutationKey(key); //!!!
	String [] keys = new String[numOfRounds];
	for(int c = 0; i != numOfRounds; ++c){
		key_right = key.substring(0,keysize/2);
		key_left = key.substring(keysize/2,keysize);
		key_right = leftShift(key_right);
		key_right = leftShift(key_right);
		key = key_right + key_right;
		keys[c] = key;
	}

	for(int i = 0; i < len; i = i+blockSize){
		if((i + blockSize) > len){//if this is the last one and it is too short
				int l = len - i;
				block_of_text = ptxt.substring(i,len);
				for(int k = l; k!= blockSize; ++k){
					block_of_text = "0" + block_of_text;
				}
				block_of_text = initialpermute(block_of_text);
			}else{
				block_of_text = ptxt.substring(i,i+blockSize);
				block_of_text = initialpermute(block_of_text);
		}

		right = block_of_text.substring(0,blockSize/2);
		left = block_of_text.substring(blockSize/2,blockSize);
		for(int j = 0; j!= numOfRounds; ++j){
			tempkey = key[numOfRounds-j-1];
			tempkey_r = tempkey.substring(0,keysize/2);
			tempkey_l = tempkey.substring(keysize/2,keysize);
			tempkey = secondPermutationKey(tempkey_r,tempkey_l);
			temp = right;
			right = expansionPermutaion(right);
			right = xor(right,key);
			right = sbox(right);
			right = pBox(right);
			right = xor(right,left);
			left = temp;
		}
		temp = right;
		right = left;
		left = temp;
		temp = right+left;
		ans += temp;
	}


}

public static void main(String[] args){
	numOfRounds = 2;
	blockSize = 3;
	//pc1_key = new int[effkeysize];
	//pc2_key = new int[roundkeysize];
	//rotationsched_key = new int[numOfRounds];
	initialPerm = new int[blockSize];
	//expansionPerm = new int[blockSize];
	//pBox = new int[blockSize/2];


	initialPerm[0] = 1;
	initialPerm[1] = 2;
	initialPerm[2] = 0;

	String boop = "001";
	String s = initialpermute(boop);
	System.out.println(s);
		for(int i = 0; i!= args.length; ++i){
			int l = args[i].length();
			if(args[i].charAt(1) == 'i'){
				inputtext = args[i].substring(2,l);
			}else if(args[i].charAt(1) == 'o'){
				outputtext = args[i].substring(2,l);
			}else if(args[i].charAt(1) == 'd'){
				String temp = outputtext;
				outputtext = inputtext;
				inputtext = temp;
				encrypt = false;
			}else if(args[i].charAt(1) == 'k'){
				key = args[i].substring(2,l);
			}else if(args[i].charAt(1) == 'p'){
				paramtext = args[i].substring(2,l);
			}else if(args[i].charAt(1) == 's'){
				showSteps = true;
			}else if(args[i].charAt(1) == 'x'){
				hexadec = true;
			}

		}
	//System.out.println(BinToHex("111111111111111111111111"));
	
	//String input = getInput(inputtext);	

	getParams(paramtext);
	
	
	
	System.out.println(expansionPermutation("1010"));
	String hi = BinToHex("01100110");
	//System.out.println(input);



	}
}