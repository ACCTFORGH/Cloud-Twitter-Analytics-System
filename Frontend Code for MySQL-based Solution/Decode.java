import java.math.BigInteger;

//Decodes wrapped/encoded request
class Decode{

	static final BigInteger X = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");
	
	static int getZ(String key){
		if(key == null){
			return 0;
		}

		// parse XY
		BigInteger XY;
		try{
			XY = new BigInteger(key);
		}catch(NumberFormatException ex){
			System.err.println(ex);
			return 0;
		}

		// calculate Z
		return XY.divide(X).mod(BigInteger.valueOf(25)).intValue() + 1;
	}

	static String decode(int Z, String msg){

		// validate msg
		if(msg == null || msg.length() < 4){
			return null;
		}
		// check length
		int n = (int)Math.sqrt(msg.length());
		if(n * n != msg.length()){
			return null;
		}

		// decode
		int index = 0;
		char[] a = new char[n*n];
		for(int layer = 0; layer < (n+1)/2; layer++){
			int m = n - 2 * layer - 1;
			if(m == 0){
				char c = msg.charAt(layer * n + layer);
				if(c < 'A' || c > 'Z') return null;
				c = (char)(c - Z);
				if(c < 'A') c += 26;
				a[index++] = c;
			}
			// top
			for(int i = 0; i < m; i++){
				int row = layer; int col = layer + i;
				char c = msg.charAt(row * n + col);
				if(c < 'A' || c > 'Z') return null;
				c = (char)(c - Z);
				if(c < 'A') c += 26;
				a[index++] = c;
			}

			// right
			for(int i = 0; i < m; i++){
				int row = layer + i; int col = n - layer - 1;
				char c = msg.charAt(row * n + col);
				if(c < 'A' || c > 'Z') return null;
				c = (char)(c - Z);
				if(c < 'A') c += 26;
				a[index++] = c;
			}

			// bottom
			for(int i = 0; i < m; i++){
				int row = n - layer - 1; int col = n - layer - 1 - i;
				char c = msg.charAt(row * n + col);
				if(c < 'A' || c > 'Z') return null;
				c = (char)(c - Z);
				if(c < 'A') c += 26;
				a[index++] = c;
			}

			// left
			for(int i = 0; i < m; i++){
				int row = n - layer - 1 - i; int col = layer;
				char c = msg.charAt(row * n + col);
				if(c < 'A' || c > 'Z') return null;
				c = (char)(c - Z);
				if(c < 'A') c += 26;
				a[index++] = c;
			}
		}

		return String.valueOf(a);
	}
}