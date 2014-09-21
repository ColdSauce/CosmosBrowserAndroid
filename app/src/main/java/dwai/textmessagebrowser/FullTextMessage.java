package dwai.textmessagebrowser;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dwai.textmessagebrowser.exceptions.TextMessageNotRecievedException;

/**
 * Created by Stefan on 9/20/2014.
 */
public class FullTextMessage {

    private ArrayList<String> texts = new ArrayList<String>();
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

    private boolean basicIsComplete(int EOFIndex){
        return (EOFIndex - texts.size() == 0);


    }

    public String addText(String value) throws Exception {
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
               if(texts.get(texts.size()).charAt(texts.get(texts.size()).length()) == '%'){
                   //End of file
                   //So basically how this works is the SMS Listener in the MainActivity calls this method when a new text message is sent.
                   //
                   if(basicIsComplete(currentTextMessageNum)){
                       return getDecompressedMessages();
                   }



               }
           }

           if(currentTextMessageNum < cTextNum && currentTextMessageNum > prevtiousTextNum){
               // In between the two
               texts.add(i-1,value);
           }

        }
        return "NOT LAST";

    }
    private int getMessageNum(String text){
       int messageNum = Integer.parseInt(text.substring(0, text.indexOf("%")));
       return messageNum;
    }

    private String getAllMessages() throws TextMessageNotRecievedException {
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


    private String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

    public String getDecompressedMessages(){
        String allData = getAllMessages();
        byte[] data = Base64.decode(allData, Base64.DEFAULT);
        String realHTML = "";
        try{
            realHTML = decompress(data);
    }
        catch(Exception e){
            e.printStackTrace();
        }

        return realHTML;




    }
    public static void closeQuietly(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
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
