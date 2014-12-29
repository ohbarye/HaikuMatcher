package utils;

import java.util.List;

import org.atilika.kuromoji.Token;
import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class HaikuUtilTest {

    @Test
    public void 十八音が俳句でないと判定されるか() {
    	String test = "雨降って完全にやる気でてこない";
    	List<Token> tokens = TokenizeUtil.tokenizeForHaiku(test);
        assertThat(HaikuUtil.inHaikuRange(tokens)).isFalse();
    }

    @Test
    public void 十七音が俳句だと判定されるか() {
    	String test = "雨降って完全やる気でてこない";
    	List<Token> tokens = TokenizeUtil.tokenizeForHaiku(test);
        assertThat(HaikuUtil.inHaikuRange(tokens)).isTrue();
    }
    
    @Test
	public void URLを文字列と判定させないか() {
    	String test = "久々に聴いたら良かった。 London Elektricity - Love The Silence feat Elsa Esmeralda OFFICIAL VIDEO - YouTube - http://t.co/TfO0RmnXc8";
    	List<Token> tokens = TokenizeUtil.tokenizeForHaiku(test);
        assertThat(HaikuUtil.inHaikuRange(tokens)).isFalse();
	}


}
