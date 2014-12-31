package utils;

import java.util.List;

import org.atilika.kuromoji.Token;

/**
 * 俳句に関するユーティリティクラス
 * @author ohbarye
 */
public class HaikuUtil {
	
	/**
	 * 引数の文字列が575調かどうか判定する
	 * @param sentence
	 * @return 俳句かどうか
	 */
	public static boolean isHaiku(String sentence) {
		// 宛先を除いた文字列にアルファベットがあれば俳句でないとする（諦める）
		if (sentence.replaceAll("@[\\x20-\\x7E]+", "").matches(".*[a-zA-Z].*")) {
			return false;
		}
		
        List<Token> tokens = TokenizeUtil.tokenizeForHaiku(sentence);

        // 俳句の文字数でない、俳句の形式でない場合はfalse
        if (!inHaikuRange(tokens) || !isHaikuFormat(tokens)) {
        	return false;
        }
		return true;
	}
	
	/**
	 * 俳句の文字数かどうか判定する。
	 * @param tokens
	 * @return 俳句の文字数かどうか
	 */
	public static boolean inHaikuRange(List<Token> tokens) {
        int length =  tokens.stream().mapToInt(s -> getLength(s)).sum();
        return length >= 17 && length < 20;
	}
	
	/**
	 * 引数の Token の音数を取得する。
	 * @param token
	 * @return 読みの音数
	 */
	public static int getLength(Token token) {
		if (token.isKnown()) {
			// 既知語であれば解析した読みを採用する
			return scrapeContractedSound(token.getReading()).length();
		} else {
			// 未知語であれば表面上の文字数を採用する
			return scrapeContractedSound(token.getSurfaceForm()).length();
		}
	}

	/**
	 * 拗音を切り捨てる。
	 * @param str
	 * @return 拗音を除去した文字列
	 */
	public static String scrapeContractedSound(String str) {
		return str.replaceAll("[ゃゅょャュョ]", "");
	}

	/**
	 * 引数の Token が句の最初の単語として採用できるかどうかを判定する。
	 * @param token
	 * @return 句の最初の単語として採用できるか
	 */
	public static boolean cannotBeHead(Token token) {
		return token.getPartOfSpeech().contains("助詞,")
				|| token.getPartOfSpeech().contains("助動詞,")
				|| token.getPartOfSpeech().contains("非自立,");
	}

	/**
	 * 文章が俳句の形式かどうか判定する。
	 * @param tokens
	 * @return 文章が俳句の形式かどうか
	 */
	public static boolean isHaikuFormat(List<Token> tokens) {
		
		boolean completedKamigo = false;		// 上五
		boolean completedNakashichi = false;	// 中七
		boolean completedShimogo = false;		// 下五
		
		int count = 0;	// 文字数
		
		// 上五、中七、下五それぞれの要件を満たすかどうかで575調かどうかを判定する
		for (Token token : tokens) {
			// 句の最初の後になれない Token が来ていたら俳句でない
			if (count == 0 && token.isKnown() && cannotBeHead(token)) {
				break;
			}
			
			count += getLength(token);
			
			// 形態素の集合が5or6音になるか
			if (!completedKamigo &&
					(count == 5 || count == 6)) {
				completedKamigo = true;
				count = 0;
			}
			// 形態素の集合が7or8音になるか
			if (completedKamigo && !completedNakashichi &&
					(count == 7 || count == 8)) {
				completedNakashichi = true;
				count = 0;
			}
			// 形態素の集合が5or6音になるか
			if (completedKamigo && completedNakashichi &&
					!completedShimogo &&
					(count == 5 || count == 6)) {
				completedShimogo = true;
				count = 0;
			}			
		}
	
		return completedKamigo && completedNakashichi && completedShimogo;
	}	
}
