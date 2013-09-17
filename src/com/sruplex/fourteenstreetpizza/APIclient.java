package com.sruplex.fourteenstreetpizza;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIclient {
  private static final String BASE_URL = "https://fourteenstreetpizza.herokuapp.com/api/v1";
  private static AsyncHttpClient client = new AsyncHttpClient();
  public static String api_auth_token = null;
  public static String api_error = null; 
  
  // ---- API Authorization --------------------------------
  public static boolean isAuthorized(){
	  if (api_auth_token == null)
		  return false;
	  else
		  return true;
  }
  public static void AuthorizeAPI(RequestParams params, APIResponseHandler resp){
	  if (!isAuthorized()) 
		 reAuthorizeAPI(params, resp);
  }
  public static void reAuthorizeAPI(RequestParams params, final APIResponseHandler resp){
	  api_auth_token = null;
	  post("/me/login/", params, new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(JSONObject response) {
        	  try {
        		  api_auth_token = response.getString("auth_token");
        	  } catch (JSONException e) { e.printStackTrace(); }
        	  
        	  resp.onSuccess();
          }
          @Override
          public void onFailure(Throwable e, JSONObject response) {
        	  try {
  				api_error = response.getString("error");
        		Log.v("BADMOTHERFUCKER", response.toString());
  			} catch (JSONException e1) { e1.printStackTrace(); }
        	  
        	  resp.onFailure();
          }
          @Override
          public void onStart()  { resp.onStart();  }
          @Override
          public void onFinish() { resp.onFinish(); }
      });

  }
  
  public static void unAuthorizeAPI() {
	  unAuthorizeAPI(new APIResponseHandler());
  }
  
  public static void unAuthorizeAPI(final APIResponseHandler resp) {
	  get("/me/logout/", null, new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(JSONObject response)  { resp.onSuccess();  }
          @Override
          public void onStart()  { resp.onStart();  }
          @Override
          public void onFinish() {
        	  api_auth_token = null;
        	  resp.onFinish();
          }
          @Override
          public void onFailure(Throwable e, JSONObject response) {
        	  try {
  				api_error = response.getString("error");
  			  } catch (JSONException e1) { e1.printStackTrace(); }
        	  
        	  resp.onFailure();
          }
	  });
  }
  
  public static Boolean HasAuthFailure(final Activity context, String response){
  	if (response.contains("auth_token missing")){
  		Toast.makeText(context, "Your Session is Invalid, Please Login Again", Toast.LENGTH_LONG).show();
  		new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... unused) {
					SystemClock.sleep(2200);
					return null;
				}
				protected void onPostExecute(Void unused){
					SessionActivity.MyMenuOptions.logout(context);
				}
  		}.execute();
  		return true;
  	} else return false;
  }
  //--------------------------------------------------------
  
  // API GET Calls
  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  if (params == null)
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
      client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  // API POST Calls
  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  if (params == null)
		  params = new RequestParams();
	  params.put("auth_token", api_auth_token);
	  Log.v("NIGGER", params.toString());
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
}