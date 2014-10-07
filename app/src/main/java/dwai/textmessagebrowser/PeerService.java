package dwai.textmessagebrowser;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Stefan on 9/27/2014.
 */
public class PeerService extends IntentService{
    public PeerService(){
        super("PeerService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

    }
}
