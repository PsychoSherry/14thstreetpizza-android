package com.sruplex.fourteenstreetpizza;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ContactActivity extends Activity {
	EditText name;
	EditText email;
	EditText number;
	EditText message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		// Initialize Objects
		name    = (EditText) findViewById (R.id.contact_name);
		email   = (EditText) findViewById (R.id.contact_email);
		number  = (EditText) findViewById (R.id.contact_number);
		message = (EditText) findViewById (R.id.contact_message);
		
		// Modify the ActionBar
        int myapi = android.os.Build.VERSION.SDK_INT;
        if (myapi >= 11) { 
            ActionBar actionBar = getActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
        }        
        
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
	        return true;
	    }
	
	    return super.onOptionsItemSelected(item);
    }
    
    public void SubmitContactForm(View view){	
    	new AsyncTask<Void, Void, Void>() {
    		final LayoutInflater factory = LayoutInflater.from(ContactActivity.this);
            private AlertDialog pdialog;
			@Override
				protected Void doInBackground(Void... unused) {
				SystemClock.sleep(2000);
				return null;
			}
			protected void onPreExecute(){
                View pview = factory.inflate(R.layout.dialog_loader, null);
                pdialog = new AlertDialog.Builder(ContactActivity.this)
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

}