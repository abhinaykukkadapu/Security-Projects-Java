package sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
	public static void main(String[] args) throws NoSuchAlgorithmException{
		String input = "hello this is shankar";
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] result = md.digest(input.getBytes());
		StringBuffer s = new StringBuffer();
		for(int i=0;i<result.length;i++)
			s.append(Integer.toString((result[i] & 0x0f)+0x0f , 16));
		System.out.println("SHA 1output:"+s.toString());
	}

}