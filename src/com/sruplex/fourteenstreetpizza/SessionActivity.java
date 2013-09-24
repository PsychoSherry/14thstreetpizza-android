package com.sruplex.fourteenstreetpizza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
	
	public void PanicMode(View view){
		final LayoutInflater factory = LayoutInflater.from(SessionActivity.this);
        View panicview = factory.inflate(R.layout.panicmode, null);

        ImageButton   btn_cancel = (ImageButton) panicview.findViewById (R.id.panic_cancel);
        ImageButton   btn_done   = (ImageButton) panicview.findViewById (R.id.panic_done);
        
        final AlertDialog alertview = new AlertDialog.Builder(SessionActivity.this)
  	  		.setView(panicview)
        	.setCancelable(false)
        	.create();

        btn_cancel.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) { alertview.dismiss(); }
        });
        
        btn_done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				alertview.dismiss();
				
		    	new AsyncTask<Void, Void, Void>() {
		    		final LayoutInflater factory = LayoutInflater.from(SessionActivity.this);
		            private AlertDialog pdialog;
					@Override
						protected Void doInBackground(Void... unused) {
						SystemClock.sleep(3000);
						return null;
					}
					protected void onPreExecute(){
		                View pview = factory.inflate(R.layout.dialog_loader, null);
		                pdialog = new AlertDialog.Builder(SessionActivity.this)
		        	  	  .setView(pview)
		                  .setCancelable(false)
		                  .create();
		                pdialog.show();
		                pdialog.getWindow().setLayout(350, 110);
						
					}
					protected void onPostExecute(Void unused){
							pdialog.dismiss();
					}
				}.execute();
			}
        });

        alertview.show(); 
		
	}

	public void OpenOrderPage(View view) {
		Intent orderpage = new Intent(getApplicationContext(), OrderActivity.class);
        startActivity(orderpage);
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
    	
    }
    

}