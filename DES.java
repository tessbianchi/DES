import java.lang.String;
import java.util.*;
import java.io.*;
import java.lang.*;



public class DES{
	static String inputtext = "p.txt";//Default input
	static String outputtext = "c.txt";//Default output
	static String paramtext = "param.txt";//Default param
	static String keytext = "key.txt";
	static String key = "";
	static boolean showSteps = false;
	static boolean hexadec = false;
	static boolean encrypt = true;



	static String ptxt;//make this one giant string of ptxt
	static String ctxt;
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
	
	

	/*
	REPORT RIGIDNESS HERE
	-automatically set to encrypt
	-Reading through the paramaters file will probably be pretty exact
	-Make sure you space after each flag 
	-Won't encrypt correctl if plaintext has spaces


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
public static String BinTohex(String bin) {//fix dis
	String temp= "";
	int len = bin.length();
	for(int i = 0; i < len; i = i+4){
	    int j = Integer.parseInt(bin.substring(i,i+4), 2);
	    String hex = Integer.toHexString(j);
	    temp += hex;
	}
	return temp; 
}

public static String inititalpermute(String p){
	String temp;
	return p;
	
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
		initialPerm[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	expansionPerm = new int[blockSize];
	//expansion Permutation EP
	for(int i = 0; i!=roundkeysize; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for EP does not contain enough integers or input is incorrect format.");
		}
		expansionPerm[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//P-box transposition Permutation EP
	for(int i = 0; i!=blockSize/2; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for P-box does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		pBox[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	numberOfSboxes = scanner.nextInt();
	while(!scanner.hasNextInt())
		scanner.nextLine();
		
		
	int x = roundkeysize/numberOfSboxes - blockSize/2/numberOfSboxes;
	int y = blockSize/2/numberOfSboxes;
	rowSelection = new int[x];
	colSelection = new int[y];
	
	//row
	for(int i = 0; i != x; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for row does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		rowSelection[i] = scanner.nextInt();
	}
	while(!scanner.hasNextInt())
		scanner.nextLine();
	
	//col
	for(int i = 0; i != y; ++i){
		if(!scanner.hasNextInt()){
			System.out.println("Error: Input for col does not contain enough integers or input is incorrect format.");
			//System.out.println(scanner.next());
		}
		colSelection[i] = scanner.nextInt();
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

public static void combine(String s){
	
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


public static void main(String[] args){

	numOfRounds = 2;
	blockSize = 2;
	ptxt = "00100000";

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
	String input = getInput(inputtext);	

	getParams(paramtext);
	
	
	
	
	String hi = BinTohex("01100110");
	//System.out.println(input);
	


	}
}