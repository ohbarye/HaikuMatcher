package utils;

import java.util.List;

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
        return length >= 17 && length < 20;
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public static int getLength(Token token) {
		if (token.isKnown()) {
			return scrapeContractedSound(token.getReading()).length();
		} else {
			return scrapeContractedSound(token.getSurfaceForm()).length();
		}
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public static String scrapeContractedSound(String str) {
		return str.replaceAll("[ゃゅょャュョ]", "");
		}

	public static boolean cannotBeHead(Token token) {
		return token.getPartOfSpeech().contains("助詞,")
				|| token.getPartOfSpeech().contains("助動詞,")
				|| token.getPartOfSpeech().contains("非自立,");
	}

	/**
	 * 
	 * @param tokens
	 * @return
	 */
	public static boolean isHaikuFormat(List<Token> tokens) {
		
		boolean completedKamigo = false;
		boolean completedNakago = false;
		boolean completedShimogo = false;
		
		int sum = 0;
		for (Token token : tokens) {
			if (sum == 0 && token.isKnown() && cannotBeHead(token)) {
				break;
			}
			
			sum += getLength(token);
			
			// 形態素の集合が5or6音になるか
			if (!completedKamigo &&
					(sum == 5 || sum == 6)) {
				completedKamigo = true;
				sum = 0;
			}
			// 形態素の集合が7or8音になるか
			if (completedKamigo && !completedNakago &&
					(sum == 7 || sum == 8)) {
				completedNakago = true;
				sum = 0;
			}
			// 形態素の集合が5or6音になるか
			if (completedKamigo && completedNakago &&
					!completedShimogo &&
					(sum == 5 || sum == 6)) {
				completedShimogo = true;
				sum = 0;
			}			
		}
		
		return completedKamigo && completedNakago && completedShimogo;
	}
	
}
