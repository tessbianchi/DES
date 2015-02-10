import java.lang.String;




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
	static int [] intitialPerm;
	static int [] expansionPerm;
	static int [] pBox;
	static int numberOfSboxes;


	static int x;
	static int y; 

	/*
	REPORT RIGIDNESS HERE
	-automatically set to encrypt
	-Reading through the paramaters file will probably be pretty exact
	-Make sure you space after each flag 
	-Won't encrypt correctly if plaintext has spaces


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
	char [] tempchar = new char[blockSize];
	int l;
	for(int i = 0; i != blockSize; ++i){
		l = intitialPerm[i];
		tempchar[i]= p.charAt(l);
	}
	temp = String.valueOf(tempchar);
	return temp;
} 

/*
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

	key = inititalpermutationKey(key); //!!!
	key_right = key.substring(0,keysize/2);
	key_left = key.substring(keysize/2,keysize);
	for(int i = 0; i < len; i = i+blockSize){
		if((i + blockSize) > len){//if this is the last one and it is too short
				int l = len - i;
				block_of_text = ptxt.substring(i,len);
				for(int k = l; k!= blockSize; ++k){
					block_of_text = "0" + block_of_text;
				}
				block_of_text = inititalpermute(block_of_text);
			}else{
				block_of_text = ptxt.substring(i,i+blockSize);
				block_of_text = inititalpermute(block_of_text);
		}
		right = block_of_text.substring(0,blockSize/2);
		left = block_of_text.substring(blockSize/2),blockSize;
		for(int j = 0; j!= numOfRounds; ++j){
			right_key = leftShift(right_key);
			left_key = leftShift(left_key);
			key = secondPermutationKey(right_key,left_key);
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


	key = inititalpermutationKey(key); //!!!
	String [] keys = new String[numOfRounds];
	for(int c = 0; i != numOfRounds; ++c){
		key_right = key.substring(0,keysize/2);
		key_left = key.substring(keysize/2,keysize);
		right_key = leftShift(right_key);
		left_key = leftShift(left_key);
		key = right_key + left_key;
		keys[c] = key;
	}

	for(int i = 0; i < len; i = i+blockSize){
		if((i + blockSize) > len){//if this is the last one and it is too short
				int l = len - i;
				block_of_text = ptxt.substring(i,len);
				for(int k = l; k!= blockSize; ++k){
					block_of_text = "0" + block_of_text;
				}
				block_of_text = inititalpermute(block_of_text);
			}else{
				block_of_text = ptxt.substring(i,i+blockSize);
				block_of_text = inititalpermute(block_of_text);
		}

		right = block_of_text.substring(0,blockSize/2);
		left = block_of_text.substring(blockSize/2),blockSize;
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

*/

public static void main(String[] args){
	numOfRounds = 2;
blockSize = 3;
//pc1_key = new int[effkeysize];
//pc2_key = new int[roundkeysize];
//rotationsched_key = new int[numOfRounds];
intitialPerm = new int[blockSize];
//expansionPerm = new int[blockSize];
//pBox = new int[blockSize/2];


intitialPerm[0] = 1;
intitialPerm[1] = 2;
intitialPerm[2] = 0;

String boop = "001";
String s = inititalpermute(boop);
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
	/*
	if(encrypt){
		encrypt();
	}else{
		decrypt();
	}
	*/





	}
}