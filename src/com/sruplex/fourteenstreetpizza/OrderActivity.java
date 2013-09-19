package com.sruplex.fourteenstreetpizza;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;


public class OrderActivity extends Activity {
	ListView list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_order);
		setContentView(R.layout.empty_list);

		String sss[] = new String[3];
		sss[0] = "Yo Bitch";
		sss[1] = "sdfds";
		sss[2] = "sdfsd";
		
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View Header = inflater.inflate(R.layout.activity_order, null);
		
		MergeAdapter myMergeAdapter = new MergeAdapter();
		myMergeAdapter.addView(Header);

		myMergeAdapter.addAdapter(new RowAdapter(getApplicationContext(), sss));
		myMergeAdapter.addAdapter(new RowAdapter(getApplicationContext(), sss));
		myMergeAdapter.addAdapter(new RowAdapter(getApplicationContext(), sss));
		
		list = (ListView) findViewById (R.id.list);
		list.setAdapter(myMergeAdapter);		
		
		
		// Initialize Objects
//		orderlist = (ListView) findViewById (R.id.orderlist);
//		orderlist.setAdapter(new RowAdapter(getApplicationContext(), sss));
		
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

}