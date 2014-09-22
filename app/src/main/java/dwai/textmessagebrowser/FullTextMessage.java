package dwai.textmessagebrowser;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    public ArrayList<String> texts = new ArrayList<String>();
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
        if(currentTextMessageNum > -1) {
            texts.add(value);
            return "NOT LAST";
        } else {
            texts.add(value);
            return getDecompressedMessages();
        }

    }
    private int getMessageNum(String text){
       Log.d("COSMOS", text);
      if(text.charAt(text.lastIndexOf("%")) == (text.charAt(text.length()-1))){
        return -1;
      } else if(text.indexOf("%") > 0) {
           Log.d("COSMOS", "TEXT:\t" + text.substring(0, text.indexOf("%")));
           int messageNum = Integer.parseInt(text.substring(0, text.indexOf("%")));
           Log.d("COSMOS", "\t.\n------------\nINT:\t" + messageNum + "\n------------");
           return messageNum;
       } else {
          return -1;
      }

    }

    private String getAllMessages() throws TextMessageNotRecievedException {

        Log.d("COSMOS", "getAllMessages() Called");

        String combinedHTML = "";
        Log.d("COSMOS", "Texts:\t"+texts);
        Collections.sort(texts);
        for(String s : texts){

            Log.d("COSMOS","S:\t"+s+"\n");
            if(s.indexOf("%") != s.length()-1)
                combinedHTML+=getContentFromMessage(s.substring(s.indexOf("%")));
            else
                combinedHTML+=getContentFromMessage(s.substring(0,s.indexOf("%")));

        }

        Log.d("COSMOS", "Combined HTML:\t"+combinedHTML);

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
        Log.d("COSMOS", string.toString());
        return string.toString();
    }

    public String getDecompressedMessages(){
        String allData = getAllMessages();
        String data = new String(Base64.decode(allData, Base64.DEFAULT));

        String realHTML = data;
//        Log.d("COSMOS", ".\nREAL HTML:\n"+data);

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
