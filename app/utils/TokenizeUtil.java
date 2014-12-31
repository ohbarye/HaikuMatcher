package utils;

import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import com.ibm.icu.text.Transliterator;

/**
 * Tokenizer 関連のユーティリティークラス
 * @author ohbarye
 */
public class TokenizeUtil {
	
	/**
	 * 俳句かどうか判定する為に整形した文字列を tokenize する。
	 * @param sentence
	 * @return tokens
	 */
	public static List<Token> tokenizeForHaiku(String sentence) {
		Tokenizer tokenizer = Tokenizer.builder().build();
        return tokenizer.tokenize(scrape(sentence));
	}

	/**
	 * 半角文字を全角文字に変換する。
	 * @param sentence
	 * @return 変換後の全角文字列
	 */
	public static String toFullWidth(String sentence) {
		Transliterator transliterator = Transliterator.getInstance("Halfwidth-Fullwidth");
		return transliterator.transliterate(sentence);
	}

	/**
	 * 俳句の構成要素となる単語を形成できない文字を除去する。
	 * @param sentence
	 * @return 記号等を除去した文字列
	 */
	public static String onlyReadableChar(String sentence) {
		return sentence.replaceAll("[^ぁ-んァ-ヶ０-９一-龠々ー]", "");
	}
	
	/**
	 * URLを除去する。
	 * @param sentence
	 * @return URLを除去した文字列
	 */
	public static String scrapeUrl(String sentence) {
		return sentence.replaceAll("https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+", "");
	}
	
	/**
	 * ツイートの宛先（@xxxx）を除去する。
	 * @param sentence
	 * @return ツイートの宛先を除去した文字列
	 */
	public static String scrapeMentionTo(String sentence) {
		return sentence.replaceAll("@[\\x20-\\x7E]+", "");
	}
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String scrapeHashTag(String sentence) {
		return sentence.replaceAll("#.+[ 　]", "");
	}
	
	/**
	 * 俳句に不要な文字列を除去する。
	 * @param sentence
	 * @return 俳句に不要な文字列を除去した文字列
	 */
	public static String scrape(String sentence) {
		return onlyReadableChar(toFullWidth(scrapeUrl(scrapeHashTag(scrapeMentionTo(sentence)))));
	}
}
