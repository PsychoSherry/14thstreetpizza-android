package com.sruplex.fourteenstreetpizza;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class OrderActivity extends Activity {
	final static int BASEHEIGHT = 100;
	static ListView orderlist = null;
	static TextView totalcost = null;
	

	private static LocationManager locMan;
    private static MapFragment mapFragment;
	private static Marker userMarker;
	private static GoogleMap theMap;
	private static LatLng lastLatLng;
	
    private static int iconUser;
    
	public  static Marker addmarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		// Reset Order Values
		OrderValues.Title       = new ArrayList<String>();
		OrderValues.Description = new ArrayList<String>();
		OrderValues.Quantity    = new ArrayList<Integer>();
		OrderValues.Price       = new ArrayList<Integer>();
		OrderValues.Image       = new ArrayList<Integer>();

//		OrderValues.arr_deals     = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_sauces    = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_drinks    = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_flavors   = getResources().getStringArray(R.array.order_deals);
//		OrderValues.arr_sidelines = getResources().getStringArray(R.array.order_deals);
		
		// Initialize Objects
		orderlist = (ListView) findViewById (R.id.orderlist);
		totalcost = (TextView) findViewById (R.id.ordertotalcost);

		
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

		UpdateOrderCostings(context);
	}
	
	public static void UpdateOrderCostings(Context context){
		Object temp_prc[] = OrderValues.Price.toArray();
		Object temp_qty[] = OrderValues.Quantity.toArray();
		
		int costings = 0;
		for(int i=0; i < temp_prc.length; i++)
			costings += ((Integer) temp_prc[i]).intValue() * ((Integer) temp_qty[i]).intValue();
		
		OrderValues.GrandTotal = costings;
		
		totalcost.setText("Rs. " + String.valueOf(costings) + "/-");
	}

	public void OrderAdd_Pizza(View arg0){
		OrderOptions.NewPizza(OrderActivity.this);
	}
	
	public void OrderAdd_Menu(View arg0){
		
	}
	
	public void OrderAdd_Deal(View arg0){
		OrderOptions.NewDeal(OrderActivity.this);
	}
	
	public void OrderComplete(View arg0){
		OrderOptions.CompleteOrder(OrderActivity.this);
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
    	static boolean dual;
    	static String pizzaname;
    	static String pizzastyle;
    	static int pizzaimg;
    	static View dialogmapview;
    	static AlertDialog.Builder mapalertbuilder;
        static AlertDialog mapalertfucker;
        
    	public static void AddOrder(String title, String description, Integer image, Integer price){
    		OrderValues.Title.add(title);
    		OrderValues.Image.add(image);
    		OrderValues.Price.add(price);
    		OrderValues.Quantity.add(1);
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
    	
    	public static void NewPizza(final Context context) {
    		final LayoutInflater factory = LayoutInflater.from(context);
            View pizzaselectview = factory.inflate(R.layout.order_add_pizza, null);

            ImageButton btn_cancel    = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_cancel);
            ImageButton btn_slice     = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_slice);
            ImageButton btn_half      = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_half);
            ImageButton btn_halfsplit = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_halfsplit);
            ImageButton btn_halfhalf  = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_halfhalf);
            ImageButton btn_full      = (ImageButton) pizzaselectview.findViewById (R.id.pizza_select_full);
          
            final AlertDialog alertview = new AlertDialog.Builder(context)
      	  		.setView(pizzaselectview)
            	.setCancelable(false)
            	.create();

            btn_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					alertview.dismiss();
				}
            });
            btn_slice.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dual = false;
					pizzastyle = "Slice";
					pizzaimg = R.drawable.pizza_slice;
					alertview.dismiss();
					NewPizza_Options(context);
				}
            });
            btn_half.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dual = false;
					pizzastyle = "Half";
					pizzaimg = R.drawable.pizza_half;
					alertview.dismiss();
					NewPizza_Options(context);
				}
            });
            btn_halfsplit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dual = true;
					pizzastyle = "Split the Half";
					pizzaimg = R.drawable.pizza_split_half;
					alertview.dismiss();
					NewPizza_Options(context);
				}
            });
            btn_halfhalf.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dual = true;
					pizzastyle = "Half and Half";
					pizzaimg = R.drawable.pizza_half_half;
					alertview.dismiss();
					NewPizza_Options(context);
				}
            });
            btn_full.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dual = false;
					pizzastyle = "Full";
					pizzaimg = R.drawable.pizza_full;
					alertview.dismiss();
					NewPizza_Options(context);
				}
            });
      	  
            alertview.show();    		
    	}
    	
    	public static void NewPizza_Options(final Context context) {
    		if (dual){
        		final LayoutInflater factory = LayoutInflater.from(context);
                View view = factory.inflate(R.layout.order_pizza_selection_2, null);
                
                ImageView pizza_img = (ImageView) view.findViewById (R.id.pizza_selection_dual_img);
                TextView  pizza_tit = (TextView)  view.findViewById (R.id.pizza_selection_dual_title);
                
                ImageButton btn_cancel = (ImageButton) view.findViewById (R.id.pizza_selection_dual_cancel);
                ImageButton btn_done   = (ImageButton) view.findViewById (R.id.pizza_selection_dual_done);

                final Spinner sauce1  = (Spinner) view.findViewById (R.id.pizza_selection_sauce_1);
                final Spinner sauce2  = (Spinner) view.findViewById (R.id.pizza_selection_sauce_2);
                final Spinner flavor1 = (Spinner) view.findViewById (R.id.pizza_selection_flavor_1);
                final Spinner flavor2 = (Spinner) view.findViewById (R.id.pizza_selection_flavor_2);

                pizza_tit.setText(pizzastyle);
                pizza_img.setBackgroundResource(pizzaimg);
                
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
    						"Pizza: " + pizzastyle,
    						flavor1.getSelectedItem().toString() + ", " + sauce1.getSelectedItem().toString() + "; " + 
    								flavor2.getSelectedItem().toString() + ", " + sauce2.getSelectedItem().toString(),
    						pizzaimg,
    						GetPizzaPrice(pizzastyle)
    					);
    					
    					alertview.dismiss();
    					RefreshOrderList(context);
    				}
                });
          	  
                alertview.show(); 
    		} else {
        		final LayoutInflater factory = LayoutInflater.from(context);
                View view = factory.inflate(R.layout.order_pizza_selection_1, null);
                
                ImageView pizza_img = (ImageView) view.findViewById (R.id.pizza_selection_img);
                TextView  pizza_tit = (TextView)  view.findViewById (R.id.pizza_selection_title);
                
                ImageButton btn_cancel = (ImageButton) view.findViewById (R.id.pizza_selection_cancel);
                ImageButton btn_done   = (ImageButton) view.findViewById (R.id.pizza_selection_done);

                final Spinner sauce    = (Spinner) view.findViewById (R.id.pizza_selection_sauce);
                final Spinner flavor   = (Spinner) view.findViewById (R.id.pizza_selection_flavor);

                pizza_tit.setText(pizzastyle);
                pizza_img.setBackgroundResource(pizzaimg);
                
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
    						"Pizza: " + pizzastyle,
    						flavor.getSelectedItem().toString() + ", " + sauce.getSelectedItem().toString(),
    						pizzaimg,
    						GetPizzaPrice(pizzastyle)
    					);
    					
    					alertview.dismiss();
    					RefreshOrderList(context);
    				}
                });
          	  
                alertview.show(); 
    		}
    	}
    	
    	public static void CompleteOrder(final Context context) {
    		final LayoutInflater factory = LayoutInflater.from(context);
    		if (dialogmapview == null)
    			dialogmapview = factory.inflate(R.layout.complete_order_map, null);
            
            ImageButton btn_cancel = (ImageButton) dialogmapview.findViewById (R.id.complete_map_cancel);
            ImageButton btn_done   = (ImageButton) dialogmapview.findViewById (R.id.complete_map_done);
          /*
            Activity fragactivity = (Activity) context;
            MyMapFragment mymapfragment = new MyMapFragment();
            FragmentManager fragmentManager = fragactivity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, mymapfragment).commit();*/
            
            if (mapalertbuilder == null)   {         
	            mapalertbuilder = new AlertDialog.Builder(context)
	      	  		.setView(dialogmapview)
	            	.setCancelable(true);

	            mapalertfucker = mapalertbuilder.create();
            }
            
            Activity fragactivity = (Activity) context;
            mapFragment = ((MapFragment)fragactivity.getFragmentManager().findFragmentById(R.id.the_map));
            theMap = mapFragment.getMap();/*
            theMap.getUiSettings().setCompassEnabled(false);
            theMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);*/
            
        	theMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
    			@Override
    			public void onMapClick(LatLng point) {
    			   	if ((addmarker != null))
    		    		addmarker.remove();
    			   	
    			   	OrderValues.Location_Selected = point;
    				addmarker = theMap.addMarker(new MarkerOptions()
    					.position(point)
    					.title("Deliver Here")
    					.snippet("Drag to Re-Position")
    				);
    				addmarker.setDraggable(true);
    				addmarker.showInfoWindow();
    				AnimateCameraTo(point, 500);
    			}
        	});
            
            final AlertDialog mapalertview = mapalertfucker;
            
            btn_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					mapalertview.dismiss();
				}
            });
            
            btn_done.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if (addmarker != null){
						mapalertview.dismiss();
						CompleteOrder_Details(context);
					}
				}
            });
         
            mapalertview.show();
    	}
    	
    	public static void CompleteOrder_Details(final Context context){
    		final LayoutInflater factory = LayoutInflater.from(context);
            View view = factory.inflate(R.layout.complete_order_details, null);
            
            ImageButton btn_cancel = (ImageButton) view.findViewById (R.id.details_cancel);
            ImageButton btn_done   = (ImageButton) view.findViewById (R.id.details_submit);
          
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
					alertview.dismiss();
					
			    	new AsyncTask<Void, Void, Void>() {
			    		final LayoutInflater factory = LayoutInflater.from(context);
			            private AlertDialog pdialog;
						@Override
							protected Void doInBackground(Void... unused) {
							SystemClock.sleep(2000);
							return null;
						}
						protected void onPreExecute(){
			                View pview = factory.inflate(R.layout.dialog_loader, null);
			                pdialog = new AlertDialog.Builder(context)
			        	  	  .setView(pview)
			                  .setCancelable(false)
			                  .create();
			                pdialog.show();
			                pdialog.getWindow().setLayout(350, 110);
							
						}
						protected void onPostExecute(Void unused){
								pdialog.dismiss();
								Activity thisactivity = (Activity) context;
								thisactivity.finish();
						}
					}.execute();
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
    	
    	public static Integer GetPizzaPrice(String pizzaname) {
    		if (pizzaname.equals("Slice"))
    			return 299;
    		else if (pizzaname.equals("Half"))
    			return 999;
    		else if (pizzaname.equals("Split the Half"))
    			return 999;
    		else if (pizzaname.equals("Half and Half"))
    			return 1799;
    		else if (pizzaname.equals("Full"))
    			return 1799;
    		
    		return 0;
    	}
    	
        private static void updatePlaces(Context context) {
        	if (userMarker!=null)
        		userMarker.remove();
        	
            locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            
            if (lastLoc != null) {
            	lastLatLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            	Log.v("14SP", lastLoc.toString());
            	Log.v("14SP", lastLatLng.toString());
            
    	        userMarker = theMap.addMarker(new MarkerOptions()
    		        .position(lastLatLng)
    		        .title("You are here")
    		        .icon(BitmapDescriptorFactory.fromResource(iconUser))
    		        .snippet("Your last recorded location"));
    	        AnimateCameraTo(lastLatLng, 3000);
            }
        }
        
        private static void AnimateCameraTo(LatLng position, int speed){
            CameraPosition cameraPosition = new CameraPosition.Builder()
    	    	.target(position)
    	    	.zoom(16)
    	    	.build();
            theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),speed,null);
        }
    	
    }

}