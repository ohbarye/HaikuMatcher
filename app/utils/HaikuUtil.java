package utils;

import java.util.List;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Token;

public class HaikuUtil {
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static boolean isHaiku(String sentence) {
		// 宛先を除いた文字列にアルファベットがあれば諦める
		if (sentence.replaceAll("@[\\x20-\\x7E]+", "").matches(".*[a-zA-Z].*")) {
			return false;
		}
		
        List<Token> tokens = TokenizeUtil.tokenizeForHaiku(sentence);

        if (!inHaikuRange(tokens) || !isHaikuFormat(tokens)) {
        	return false;
        }

		return true;
	}
	
	/**
	 * 俳句の文字数かどうか判定する。
	 * @param sentence
	 * @return
	 */
	public static boolean inHaikuRange(List<Token> tokens) {
        int length =  tokens.stream().mapToInt(s -> getLength(s)).sum();
        return length == 17;
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public static int getLength(Token token) {
		if (token.isKnown()) {
			return token.getReading().length();
		} else {
			return token.getSurfaceForm().length();
		}
	}

	/**
	 * 
	 * @param tokens
	 * @return
	 */
	public static boolean isHaikuFormat(List<Token> tokens) {
		
		List<Integer> lengthList = tokens.stream()
				.map(s -> getLength(s))
				.collect(Collectors.toList());
		
		boolean completedKamigo = false;
		boolean completedNakago = false;
		boolean completedShimogo = false;
		
		int sum = 0;
		for (Integer count : lengthList) {
			sum += count;
			
			// 形態素の集合が5音になるか
			if (!completedKamigo && sum == 5) {
				completedKamigo = true;
			}
			// 形態素の集合が10音になるか
			if (!completedNakago && sum == 12) {
				completedNakago = true;
			}
			// 形態素の集合が17音になるか
			if (!completedShimogo && sum == 17) {
				completedShimogo = true;
			}			
		}
		
		return completedKamigo && completedNakago && completedShimogo;
	}

}
