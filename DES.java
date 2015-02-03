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

	/*
	REPORT RIGIDNESS HERE
	-automatically set to encrypt
	-Reading through the paramaters file will probably be pretty exact
	-Make sure you space after each flag 

	*/

	public static void main(String[] args){
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

	System.out.println(inputtext);
	System.out.println(outputtext);
	if(key != ""){
		System.out.println(key);
	}else{
		System.out.println(keytext);
	}
	System.out.println(paramtext);
	System.out.println(showSteps);
	System.out.println(hexadec);
	System.out.println(encrypt);


	}
}