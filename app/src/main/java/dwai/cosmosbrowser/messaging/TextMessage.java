package dwai.cosmosbrowser.messaging;

import android.util.Log;

import dwai.cosmosbrowser.MainBrowserScreen;

/**
 * Created by Stefan on 10/19/2014.
 * A buffer to store the SMS Text Message data.
 */
public class TextMessage {
    private final String TAG = "TextMessage";
    private int howManyAdded = 0;
    public String[] textBuffer = null;

    /**
     * Constructs a new TextMessage object that allows for the use of a String buffer to represent a text message.
     * @param sizeOfParts size of the buffer (in terms of parts)
     */
    public TextMessage(int sizeOfParts){
        if(sizeOfParts < 0){
            Log.e(TAG, "******* ERROR SIZE OF PARTS IS NEGATIVE");
            return;
        }
        textBuffer = new String[sizeOfParts];
    }

    public void addPart(int index, String part){
        if(index < 0 || index > textBuffer.length || part == null){
            Log.e(TAG, "******* ERROR EITHER PART WAS NULL OR INDEX WAS OUT OF BOUNDS");
            return;
        }

        textBuffer[index] = part;
        howManyAdded++;
        if(howManyAdded == textBuffer.length){

        }
    }

    /**
     * returns the text message to the activity in question.
     */
    private void flush(){

    }



}
