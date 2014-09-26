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
        final int BUFFER_SIZE = compressed.length*2;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        try {
            while ((bytesRead = gis.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead));
            }
        }
        catch(Exception q){
              Log.d("COSMOS",string.toString());
             q.printStackTrace();

        }
        finally{
            gis.close();
            is.close();
        }
        Log.d("COSMOsdfsdfS", string.toString());
        return string.toString();
    }

    public String getDecompressedMessages(){
        String allData = getAllMessages();
//        byte[] compressedData = (Base64.decode("H4sIAAAAAAAAA5WTX0/CMBTFv0rTZ1lDJISQ0kQUQR+MITwQX0y3FVrpWmgvG3x77/gXMWLgZelud865/e2Wayis4KnPt4JHlYHxTnDdFFySzMoYexSrQQElOqhZj1rjFjGpVVQ8uIUBMpbuSzrOpOCsFjpZHqXa5LlyjViQw2oTKbrf1+4gw1xBj36mVroFPUqWa2sbVs1OgRpg2WUMNAYnsk5MjKeib/28ziR/WJ0Jq6pK5gb0Ok0yX7CdQ6BiaGC0Ti85/GwmmLn+3U1tCpUBUOGH6ycVk33xQAMPyhDHGZPSRJNaVUM5Lv+hchWDfdIVBpdZ6D2Lm5xuAcBOszXzHvcxpCX4SrzMyBKx4A7JgylVJFu/viNWAQlKRixrb3MCWuG7cTHhbIV/LDOgRF/h4BXGkeeA8TiYnO3qmIveKWbk5oQ9hQwhx0JaK/qTxy5p9sf5JAw7g+3k7f11My3V6qk96HwU02q0beXjdolN7z7nDH3weeyc7W8L212db7CoiktBAwAA", Base64.DEFAULT));
        byte[] compressedData = Base64.decode(allData, Base64.DEFAULT);
        String data = "";
        try{
            data = decompress(compressedData);
        }
        catch(IOException e ){
           e.printStackTrace();
        }
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
