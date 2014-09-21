package dwai.textmessagebrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import dwai.textmessagebrowser.exceptions.TextMessageNotRecievedException;

/**
 * Created by Stefan on 9/20/2014.
 */
public class FullTextMessage {

    private boolean reachedEOF = false;

    ArrayList<String> texts = new ArrayList<String>();
    private final int EVERYTHING_WORKED = -1;

    private int getWrongIndex(){
        for(int i = 0; i < texts.size()-1;i++){
            int currentMessage = Integer.parseInt(texts.get(i));
            int secondMessage = Integer.parseInt(texts.get(i+1));
            if(secondMessage - currentMessage != 0){
                return currentMessage;
            }

        }
        return EVERYTHING_WORKED;
    }

    public void addText(String value,boolean lastFile) throws Exception {
        reachedEOF = lastFile;
        int currentTextMessageNum = getMessageNum(value);

       for(int i = 1; i < texts.size();i++){
           int cTextNum = getMessageNum(texts.get(i));
           int prevtiousTextNum = getMessageNum(texts.get(i-1));
           //Extreme cases
           if(i == 1){
               if(currentTextMessageNum < prevtiousTextNum){
                   texts.add(0,value);
               }
           }
           if(i == texts.size()){
                if(currentTextMessageNum > currentTextMessageNum){
                    texts.add(i,value);
                }
           }

           if(currentTextMessageNum < cTextNum && currentTextMessageNum > prevtiousTextNum){
               // In between the two
               texts.add(i-1,value);
           }

        }
    }
    private int getMessageNum(String text){
       int messageNum = Integer.parseInt(text.substring(0, text.indexOf("%")));
       return messageNum;
    }

    public String getAllMessages() throws TextMessageNotRecievedException {
        int wrongIndex = 0;
        if((wrongIndex = getWrongIndex()) != EVERYTHING_WORKED){
            throw new TextMessageNotRecievedException("Exception thrown because " + wrongIndex + " has not been recieved.");
        }

        String combinedHTML = "";
        for(String s : texts){
            combinedHTML = getContentFromMessage(s);
        }
        return combinedHTML;


    }

    private String getContentFromMessage(String message){
        int indexOfFirstPercent = message.indexOf("%");
        int indexOfLastPercent = message.lastIndexOf("%");

        if(indexOfFirstPercent == indexOfLastPercent){
            //There is no EOF
            return message.substring(indexOfFirstPercent+1);
        }
        //substring is exclusive
        return message.substring(indexOfFirstPercent+1,indexOfLastPercent);

    }






}
