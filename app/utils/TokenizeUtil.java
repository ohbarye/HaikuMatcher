package utils;

import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import com.ibm.icu.text.Transliterator;

public class TokenizeUtil {
	
	public static List<Token> tokenizeForHaiku(String sentence) {
		Tokenizer tokenizer = Tokenizer.builder().build();
        return tokenizer.tokenize(scrape(sentence));
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String toFullWidth(String sentence) {
		Transliterator transliterator = Transliterator.getInstance("Halfwidth-Fullwidth");
		return transliterator.transliterate(sentence);
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String onlyReadableChar(String sentence) {
		return sentence.replaceAll("[^ぁ-んァ-ヶ０-９一-龠々ー]", "");
	}
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String scrapeUrl(String sentence) {
		return sentence.replaceAll("https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+", "");
	}
	
	/**
	 * 
	 * @param sentence
	 * @return
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
	 * 
	 * @param sentence
	 * @return
	 */
	public static String scrape(String sentence) {
		return onlyReadableChar(toFullWidth(scrapeUrl(scrapeHashTag(scrapeMentionTo(sentence)))));
	}
}
