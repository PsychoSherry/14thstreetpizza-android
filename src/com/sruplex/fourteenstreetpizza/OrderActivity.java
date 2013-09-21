package com.sruplex.fourteenstreetpizza;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;


public class OrderActivity extends Activity {
	final static int BASEHEIGHT = 100;
	static ListView orderlist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		String sss[] = new String[3];
		sss[0] = "Yo Bitch";
		sss[1] = "sdfds";
		sss[2] = "sdfsd";
		
		// Reset Order Values
		OrderValues.Title       = new ArrayList<String>();
		OrderValues.Description = new ArrayList<String>();
		OrderValues.Quantity    = new ArrayList<Integer>();
		OrderValues.Price       = new ArrayList<Integer>();
		OrderValues.TotalCost   = new ArrayList<Integer>();
		OrderValues.Image       = new ArrayList<Integer>();

//		OrderValues.arr_deals     = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_sauces    = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_drinks    = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_flavors   = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_sidelines = getResources().getStringArray(R.array.order_deals);
		
		// Initialize Objects
		orderlist = (ListView) findViewById (R.id.orderlist);

		
		// Modify the ActionBar
        int myapi = android.os.Build.VERSION.SDK_INT;
        if (myapi >= 11) { 
            ActionBar actionBar = getActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
        }        
        
	}
	
	public static void RefreshOrderList(Context context) {
		orderlist.setLayoutParams(new LayoutParams(orderlist.getLayoutParams().width, (BASEHEIGHT * OrderValues.Title.size()) + 30));
		orderlist.setAdapter(new RowAdapter(context, OrderValues.Title.toArray(new String[OrderValues.Title.size()])));
	}

	public void OrderAdd_Pizza(View arg0){
		
	}
	
	public void OrderAdd_Menu(View arg0){
		
	}
	
	public void OrderAdd_Deal(View arg0){
		OrderOptions.NewDeal(OrderActivity.this);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
	        return true;
	    }
	
	    return super.onOptionsItemSelected(item);
    }
    
    public static class OrderOptions {
        
    	public static void AddOrder(String title, String description, Integer image, Integer price){
    		OrderValues.Title.add(title);
    		OrderValues.Image.add(image);
    		OrderValues.Price.add(price);
    		OrderValues.Description.add(description);
    	}
    	
    	public static void NewDeal(final Context context) {
    		final LayoutInflater factory = LayoutInflater.from(context);
            View view = factory.inflate(R.layout.order_add_deal, null);
            
            ImageButton btn_cancel = (ImageButton) view.findViewById (R.id.order_deal_cancel);
            ImageButton btn_done   = (ImageButton) view.findViewById (R.id.order_deal_done);

            final Spinner deal     = (Spinner) view.findViewById (R.id.order_deal_name);
            final Spinner sauce    = (Spinner) view.findViewById (R.id.order_deal_sauce);
            final Spinner drink    = (Spinner) view.findViewById (R.id.order_deal_drink);
            final Spinner flavor   = (Spinner) view.findViewById (R.id.order_deal_flavor);
            final Spinner sideline = (Spinner) view.findViewById (R.id.order_deal_sideline);
          
            final AlertDialog alertview = new AlertDialog.Builder(context)
      	  		.setView(view)
            	.setCancelable(false)
            	.create();

            btn_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					alertview.dismiss();
				}
            });
            
            btn_done.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					AddOrder(
						deal.getSelectedItem().toString(),
						flavor.getSelectedItem().toString() + ", " + sauce.getSelectedItem().toString() + ", " + sideline.getSelectedItem().toString() + ", " + drink.getSelectedItem().toString(),
						R.drawable.deals,
						GetDealPrice(deal.getSelectedItem().toString())
					);
					
					alertview.dismiss();
					RefreshOrderList(context);
				}
            });
      	  
            alertview.show();    		
    	}
    	
    	
    	// Sastay Methods
    	public static Integer GetDealPrice(String dealname) {
    		if (dealname.equals("Slice Deal"))
    			return 399;
    		else if (dealname.equals("Double Slice Deal"))
    			return 649;
    		else if (dealname.equals("Midnight Deal 1"))
    			return 349;
    		else if (dealname.equals("Midnight Deal 2"))
    			return 1099;
    		
    		return 0;
    	}
    	
    }

}