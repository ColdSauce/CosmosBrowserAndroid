package dwai.textmessagebrowser;

import java.util.HashMap;

/**
 * Created by Stefan on 9/20/2014.
 */
public class FullTextMessage {
    private HashMap<Integer, String> texts = new HashMap<Integer, String>();


    public void addText(int index,String value) throws TextMessageNotFoundException {
        texts.put(index,value);
    }






}
