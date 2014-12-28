package models;

import java.util.ArrayList;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class Haiku {
	
    private Haiku formatSentenceInHaiku(String sentence) {
        Haiku haiku = new Haiku();
        
        Tokenizer tokenizer = Tokenizer.builder().build();
        List<Token> tokens = tokenizer.tokenize(sentence);

        // 作業用リスト
        List<String> morphemes = new ArrayList<>();

        for (Token token : tokens) {
            int tokenLength = token.getReading().length();
            
            if (haiku.progress == Haiku.Part.KAMIGO) {
                // 規定の文字数に達しない場合
                if (getCurrentLength(morphemes) + tokenLength < Haiku.Part.KAMIGO.getLength()) {
                    
                }
                else if (getCurrentLength(morphemes) + tokenLength >= Haiku.Part.KAMIGO.getLength()) {
                    
                }
                
                
            } else if (haiku.progress == Haiku.Part.NAKASHICHI) {
                
            } else if (haiku.progress == Haiku.Part.SHIMOGO) {
                
            }
        }

        return null;
    }
    
    public long getCurrentLength(List<String> morphemes) {
        return morphemes.stream()
                .mapToInt(s -> s.length())
                .sum();
    }
    
    Haiku() {
        this.progress = Part.KAMIGO;
    }

    public static enum Part {
        KAMIGO {
            @Override
            public short getLength() {return 5;}
        }
       ,NAKASHICHI {
            @Override
           public short getLength() {return 7;}
        }
       ,SHIMOGO {
            @Override
           public short getLength() {return 5;}
       };
       public abstract short getLength();
    }

    // 上五
    public String kamigo;
    // 中七
    public String nakashichi;
    // 下五
    public String shimogo;

    // 進捗(考案中の句)
    public Part progress;

    @Override
    public String toString() {
        return
                kamigo + System.getProperty("line.seperator")
                + nakashichi + System.getProperty("line.seperator")
                + shimogo ;
    }    

}