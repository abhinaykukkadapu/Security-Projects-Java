package girish.security.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESinECB {

	static String hexLookUp = "0123456789abcdef",
			base64LookUp = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	public static void main(String[] args) {
		convertBase64FileToHex();
		System.out.println(decryptAES());
	}

	/**
	 * This function reads the base64 content in file 7.txt and decodes it and
	 * stores it in the hex format in the file 7hex.txt
	 */
	private static void convertBase64FileToHex() {
		String inFilename = "7.txt";
		String outFilename = "7hex.txt";
		File outFile = new File(outFilename);
		try {
			outFile.createNewFile();
			FileReader fr = new FileReader(inFilename);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fileWriter = new FileWriter(outFile);
			String s;

			// Read each line of the file as a String and pass it to the
			// function convertBase64ToHex() to get back the decoded String
			while ((s = br.readLine()) != null) {
				s = convertBase64ToHex(s);
				fileWriter.write(s);
			}
			fileWriter.close();
			br.close();
			fr.close();
		} catch (IOException e) {
		}
	}

	/**
	 * This function decodes a base64 string into its hex format.
	 * 
	 * @param base64
	 *            The base64 encoded String input that is to be decoded.
	 * @return Base64 decoded String (in hex format)
	 */
	public static String convertBase64ToHex(String base64) {
		String hex = "";
		for (int i = 0; i < base64.length(); ++i) {
			// Take first 6 bits (from the first base64 digit) and call
			// convert() with the first 4 bits.
			String firstDigit = String.format("%06d",
					Integer.parseInt(Integer.toBinaryString(base64LookUp.indexOf(base64.charAt(i++)))));
			hex = hex + convert(firstDigit.substring(0, 4));

			// Take the remaining 2 bits (from the first base64 digit) and the
			// first 2 bits of the second base64 digit and call convert()
			String secondDigit = String.format("%06d",
					Integer.parseInt(Integer.toBinaryString(base64LookUp.indexOf(base64.charAt(i)))));
			hex = hex + convert(firstDigit.substring(4, 6) + secondDigit.substring(0, 2));

			hex = hex + convert(secondDigit.substring(2, 6));
		}
		return hex;
	}

	/**
	 * This function converts the 4 bit sequence to the corresponding hex value.
	 * Each hex digit is appended to the string global instance 'hex'.
	 * 
	 * @param bits
	 *            A 4 bit sequence
	 * @return The hex version of the bit sequence sent as input, appended to
	 *         the previously converted sequences.
	 */
	static String convert(String bits) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.valueOf(hexLookUp.charAt(Integer.parseInt(bits, 2))));
		String hex = stringBuilder.toString();
		return hex;
	}

	/**
	 * This function converts the given ASCII text string into the hex
	 * representation.
	 * 
	 * @param ascii
	 *            A String in ASCII format
	 * @return A String which is the input string's corresponding hex format.
	 */
	public static String asciiToHex(String ascii) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < ascii.length(); ++i) {
			buffer.append(Integer.toHexString((int) ascii.charAt(i)));
		}
		return buffer.toString();
	}

	/**
	 * This function decrypts the AES encoded content in the file 7hex.txt with
	 * the specified key.
	 * 
	 * @reference http://stackoverflow.com/questions/13102788/is-there-any-
	 *            sample-java-code-that-does-aes-encryption-exactly-like-this-
	 *            website
	 * 
	 * @return Original Plaintext in ASCII.
	 */

	private static String decryptAES() {
		String inFilename = "7hex.txt";
		try {
			FileReader fr = new FileReader(inFilename);
			BufferedReader br = new BufferedReader(fr);

			final String keyHex = asciiToHex("YELLOW SUBMARINE");
			final String cipherHex = br.readLine();

			SecretKey key = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(DatatypeConverter.parseHexBinary(cipherHex));

			br.close();
			fr.close();

			return new String(result);
		} catch (Exception e) {
			return null;
		}
	}

}