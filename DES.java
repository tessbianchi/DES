import java.lang.String;




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
	static String key = "";
	static int blockSize;
	static int keysize;
	static int effkeysize;
	static int roundkeysize;
	static int numOfRounds;
	static int [] pc1_key = new int[effkeysize];
	static int [] pc2_key = new int[roundkeysize];
	static int [] rotationsched_key = new int[numOfRounds];
	static int [] intitialPerm = new int[blockSize];
	static int [] expansionPerm = new int[blockSize];
	static int [] pBox = new int[blockSize/2];
	static int numberOfSboxes;

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

} 
public static void encrypt(){
	if(hexadec){
		ptxt = hexToBinary(ptxt);
		key = hexToBinary(key);
	}
	int len = ptxt.length();
	String ans; 
	String temp = "";
	String s;
	String right = "";
	String left "";
	for(int j = 0; j!= numOfRounds; ++j){
		for(int i = 0; i < len; i = i+blockSize){
			if((i + blockSize) > len){//if this is the last one and it is too short
				int l = len - i;
				s = ptxt.substring(i,len);
				for(int j = l; j!= blockSize; ++j){
					s = "0" + s;
				}
				temp = inititalpermute(s);
			}else{
				s = ptxt.substring(i,i+blockSize);
				temp = inititalpermute(s);
			}
		}
	}


}




public static void main(String[] args){

numOfRounds = 2;
blockSize = 2;
ptxt = "00100000"

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

String hi = BinTohex("01100110");
System.out.println(hi);



	}
}