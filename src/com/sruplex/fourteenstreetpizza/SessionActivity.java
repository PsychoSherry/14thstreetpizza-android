package com.sruplex.fourteenstreetpizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.Session;


public class SessionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
        
	}
	
	public void logout(View view) {
		MyMenuOptions.logout(SessionActivity.this);
	}
	 
    
    // Custom Class to Handle Drawer Menu Options
    public static class MyMenuOptions {
    	public static void home() { }
    	public static void logout(Activity myactivity) {
            Session session = Session.getActiveSession();
            if (!session.isClosed())
                session.closeAndClearTokenInformation();
            
            APIclient.unAuthorizeAPI();
            Intent intent = new Intent(myactivity.getApplicationContext(), MainActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            myactivity.getApplicationContext().startActivity(intent);
            myactivity.finish();
            MainActivity.logged = false;
    	}
    	
    	public static void newOrder() {  }
    }
}