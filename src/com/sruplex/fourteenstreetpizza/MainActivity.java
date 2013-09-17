package com.sruplex.fourteenstreetpizza;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
    public static String user_ID = null;
    public static Boolean logged = false;
    private TextView mytext;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        buttonLoginLogout = (Button)findViewById(R.id.button1);
        mytext = (TextView)findViewById(R.id.textView1);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
    }
    
    private void updateView() {
        final Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            mytext.setText("You're Logged in to facebook");
            Log.v("fb",session.getAccessToken());
            buttonLoginLogout.setText("logout");
//            buttonLoginLogout.setVisibility(Button.GONE);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { Logout(); }
            });
            
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    // If the response is successful
                    if (session == Session.getActiveSession()) {
                        if (user != null) {
                            user_ID = user.getId();
                            CreateCustomServerSession(session);
                        } else {
                        	Log.v("niggers", "requested user is null");
                        	mytext.setText("failed");
                        }
                    }   
                }   
            }); 
            Request.executeBatchAsync(request);

            
        } else {
            mytext.setText("Failed");
            buttonLoginLogout.setText("login");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { Login(); }
            });
        }
    }
    
    private void CreateCustomServerSession(Session session){
        final ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
        if (!APIclient.isAuthorized()) {                
            progress.setVisibility(ProgressBar.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("fb_userid", user_ID);
            params.put("fb_token", session.getAccessToken());
            
            APIclient.AuthorizeAPI(params, new APIResponseHandler() {
            	@Override
            	public void onSuccess() {
            		mytext.setText("success");
            	}
            	@Override
            	public void onFailure() {
            		mytext.setText("failed");
            	}
            	@Override
            	public void onFinish() {
//            		updateView();
            		
                    new AsyncTask<Void, Void, Void>() {
    					@Override
     					protected Void doInBackground(Void... unused) {
    						SystemClock.sleep(2500);
    						return null;
    					}
    					protected void onPostExecute(Void unused) {
                    		progress.setVisibility(ProgressBar.GONE);
                    		if (APIclient.isAuthorized() && !logged)
                    			StartSessionActivity();
    					}
                    }.execute();
            	}
            });
        } else StartSessionActivity();
    }
    
    private void StartSessionActivity() {
		logged = true;
		Intent intent = new Intent(MainActivity.this, SessionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        getApplicationContext().startActivity(intent);
        finish();
    }
    
    private void Login() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    public static void Logout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            APIclient.unAuthorizeAPI();
            logged = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
