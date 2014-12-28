package utils;

import java.util.List;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class HaikuUtil {
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static boolean isHaiku(String sentence) {
		
		String trimmed = sentence.replaceAll("[a-zA-z 　@]", "");

		Tokenizer tokenizer = Tokenizer.builder().build();
        List<Token> tokens = tokenizer.tokenize(trimmed);

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
        int length =  tokens.stream().mapToInt(s -> s.getReading().length()).sum();
        return length == 17;
	}

	public static boolean isHaikuFormat(List<Token> tokens) {
		
		List<Integer> lengthList = tokens.stream()
				.map(s -> s.getReading().length())
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
