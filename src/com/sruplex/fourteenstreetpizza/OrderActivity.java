package com.sruplex.fourteenstreetpizza;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;


public class OrderActivity extends Activity {
	ListView orderlist = null;
	int baseheight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		String sss[] = new String[3];
		sss[0] = "Yo Bitch";
		sss[1] = "sdfds";
		sss[2] = "sdfsd";
		
		// Initialize Objects
		orderlist = (ListView) findViewById (R.id.orderlist);
//		baseheight = orderlist.getLayoutParams().height;
		baseheight = 100;

		orderlist.setLayoutParams(new LayoutParams(orderlist.getLayoutParams().width, (baseheight * sss.length) + 30));
		orderlist.setAdapter(new RowAdapter(getApplicationContext(), sss));
		
		// Modify the ActionBar
        int myapi = android.os.Build.VERSION.SDK_INT;
        if (myapi >= 11) { 
            ActionBar actionBar = getActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
        }        
        
	}

	public void OrderAdd_Pizza(View view){
		
	}
	
	public void OrderAdd_Deal(View view){
		
	}
	
	public void OrderAdd_Menu(View view){
		
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
	        return true;
	    }
	
	    return super.onOptionsItemSelected(item);
}

}