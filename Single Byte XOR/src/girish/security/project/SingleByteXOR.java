/**
 * @author Girish Raman
 * From http://cryptopals.com/
 * 
 * This program takes in a HEX string, that has been XOR'd against a single character. 
 * It finds the key, and decrypts the message.
 */

package girish.security.project;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SingleByteXOR {

	public static void main(String[] a) {
		String hexCipherText = "06e8f90a3118381c5414157d1434050210363e30500511a00a3d56e10438";
		Map<String, Double> finalScores = new LinkedHashMap<String, Double>();

		// For each character A-Z, XOR the cipher with an equal length repeating
		// character string.
		// Convert the XOR output to ASCII, and send the ASCII string to
		// computeScore to (surprise!) compute its score.
		for (int i = 0x41; i <= 0x5a; ++i) {
			String key = formRepeatingCharacterString(i);
			String hexPlainText = computeXOR(hexCipherText, key);
			String output = computeHexToASCII(hexPlainText);
			finalScores.put(String.valueOf((char) i), computeScore(output.toString()));
		}

		// Do the same for a-z.
		for (int i = 0x61; i <= 0x7a; ++i) {
			String key = formRepeatingCharacterString(i);
			String hexPlainText = computeXOR(hexCipherText, key);
			String output = computeHexToASCII(hexPlainText);
			finalScores.put(String.valueOf((char) i), computeScore(output));
		}

		/*
		//Iterate through the HashMap and display the keyCharacter, the corresponding plainText and its score.
		Iterator<?> it = finalScores.entrySet().iterator();
		double score = 0;
		String keyCharacter = null;
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			score = (double) pair.getValue();
			
			keyCharacter = (String) pair.getKey();
			String key = formRepeatingCharacterString(keyCharacter);
			String hexPlainText = computeXOR(hexCipherText, key);
			String output = computeHexToASCII(hexPlainText);

			System.out.println(keyCharacter + "\n" + score + "\n" + output + "\n\n");
			it.remove();
		}*/
		
		//Iterate through the HashMap and display the keyCharacter, the corresponding plainText and its score.
		Iterator<?> it = finalScores.entrySet().iterator();
		double maxScore = 0;
		String keyCharacter = null;
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			maxScore = (maxScore > (double) pair.getValue() ? maxScore : (double) pair.getValue());
			keyCharacter = (maxScore > (double) pair.getValue() ? keyCharacter : (String) pair.getKey());
		}
		String key = formRepeatingCharacterString(keyCharacter);
		String hexPlainText = computeXOR(hexCipherText, key);
		String originalPlainText = computeHexToASCII(hexPlainText);

		System.out.println(keyCharacter + "\n" + maxScore + "\n" + originalPlainText + "\n\n");
		it.remove();
	
	}
	
	/*
	 * Given a HEX character, this function forms a String by repeating it 30 times.
	 */
	static String formRepeatingCharacterString(int i) {
		String key = "";
		for (int j = 0; j < 30; ++j) {
			key = key + Integer.toHexString(i);
		}
		return key;
	}
	
	static String formRepeatingCharacterString(String keyCharacter) {
		String key = "";
		for (int j = 0; j < 30; ++j) {
			key = key + Integer.toHexString((int) keyCharacter.charAt(0));
		}
		return key;
	}

	/*
	 * This function computes the ASCII equivalent of a HEX String.
	 */
	static String computeHexToASCII(String hexPlainText){
		StringBuilder output = new StringBuilder();
		for (int k = 0; k < hexPlainText.length(); k += 2) {
			String str = hexPlainText.substring(k, k + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

	/*
	 * This function computes character by character XOR for the two input HEX
	 * strings returns an output string in HEX.
	 */
	static String computeXOR(String first, String second) {
		String result = "";
		StringBuilder stringBuilder = new StringBuilder(result);

		// I'm going character by character here. ^ does the XOR.
		for (int i = 0; i < first.length(); ++i) {
			int cc = Integer.parseInt(String.valueOf(first.charAt(i)), 16)
					^ Integer.parseInt(String.valueOf(second.charAt(i)), 16);
			stringBuilder.append(String.valueOf(Integer.toHexString(cc)));
			result = stringBuilder.toString();
		}
		return result;
	}

	/*
	 * ETAOIN SHRDLU - The most frequent English alphabet list. I'm giving E a
	 * score of 13, decreasing up to 1 for U. I'm not scoring any other
	 * alphabets. (And for this case, it turns out you don't have to!)
	 * 
	 * This function returns the final score of the input string, after scoring
	 * each character in it, based on this scheme.
	 */
	static double computeScore(String s) {
		double score = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
				score += 13;
			} else if (s.charAt(i) == 't' || s.charAt(i) == 'T') {
				score += 12;
			} else if (s.charAt(i) == 'a' || s.charAt(i) == 'A') {
				score += 11;
			} else if (s.charAt(i) == 'o' || s.charAt(i) == 'O') {
				score += 10;
			} else if (s.charAt(i) == 'i' || s.charAt(i) == 'I') {
				score += 9;
			} else if (s.charAt(i) == 'n' || s.charAt(i) == 'N') {
				score += 8;
			} else if (s.charAt(i) == ' ') {
				score += 7;
			} else if (s.charAt(i) == 's' || s.charAt(i) == 'S') {
				score += 6;
			} else if (s.charAt(i) == 'h' || s.charAt(i) == 'H') {
				score += 5;
			} else if (s.charAt(i) == 'r' || s.charAt(i) == 'R') {
				score += 4;
			} else if (s.charAt(i) == 'd' || s.charAt(i) == 'D') {
				score += 3;
			} else if (s.charAt(i) == 'l' || s.charAt(i) == 'L') {
				score += 2;
			} else if (s.charAt(i) == 'u' || s.charAt(i) == 'U') {
				score += 1;
			}
		}
		return score;
	}
}