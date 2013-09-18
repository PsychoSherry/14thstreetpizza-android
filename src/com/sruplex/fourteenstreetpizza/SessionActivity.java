package com.sruplex.fourteenstreetpizza;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Session;


public class SessionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
		
		final TextView  username  = (TextView)  findViewById (R.id.username);
		final ImageView userimage = (ImageView) findViewById (R.id.userimage);

		username.setText(APIclient.facebook_name);
		if (APIclient.facebook_picture == null) {
            new AsyncTask<Void, Void, Void>() {
				@Override
					protected Void doInBackground(Void... unused) {
                    APIclient.DownloadUserImage();
					return null;
				}
				protected void onPostExecute(Void unused) {
					if (APIclient.facebook_picture != null)
						userimage.setImageBitmap(APIclient.facebook_picture);
				}
            }.execute();
		} else
			userimage.setImageBitmap(APIclient.facebook_picture);
        
	}

	public void OpenOrderPage(View view) {
		
	}
	
	public void OpenAboutPage(View view) {
		Intent aboutpage = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(aboutpage);
	}
	
	public void OpenContactPage(View view) {
		Intent contactpage = new Intent(getApplicationContext(), ContactActivity.class);
        startActivity(contactpage);	
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