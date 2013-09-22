package com.sruplex.fourteenstreetpizza;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends Fragment {
	private LocationManager locMan;
    private MapFragment mapFragment;
	private Marker userMarker;
	private static GoogleMap theMap;
	private static LatLng lastLatLng;
	
    private static int iconUser;
    
	public  static Marker addmarker;
    public  static Boolean AddingStatus   = false;
    public  static Boolean uploadfailed   = false;
    public  static Boolean requestfailed  = false;
    
	public  MyMapFragment() { }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        
        iconUser    = R.drawable.blue_point;
        
        mapFragment = ((MapFragment)getFragmentManager().findFragmentById(R.id.the_map));
        theMap = mapFragment.getMap();
        theMap.getUiSettings().setCompassEnabled(false);
        theMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        
        updatePlaces();
        
        addmarker = theMap.addMarker(new MarkerOptions()
        	.position(userMarker.getPosition())
			.title("Deliver Here")
			.snippet("Drag to Re-Position")
        );
        
    	theMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
			@Override
			public void onMapClick(LatLng point) {
			   	if ((addmarker != null))
		    		addmarker.remove();
			   	
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
        
        return rootView;
    }
    
    private void updatePlaces() {
    	if(userMarker!=null) userMarker.remove();
    	
        locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        if (lastLoc != null) {
        	lastLatLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        
	        userMarker = theMap.addMarker(new MarkerOptions()
		        .position(lastLatLng)
		        .title("You are here")
		        .icon(BitmapDescriptorFactory.fromResource(iconUser))
		        .snippet("Your last recorded location"));
	        AnimateCameraTo(lastLatLng, 3000);
        }
    }
    
    private static void GetUserLocation(){
    	// FILL THIS SHIT WITH USELESS NONSENSE
    }
    
    private static void AnimateCameraTo(LatLng position, int speed){
        CameraPosition cameraPosition = new CameraPosition.Builder()
	    	.target(position)
	    	.zoom(16)
	    	.build();
        theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),speed,null);
    }
    
    private static String StringifyCoordinates(LatLng latlng, Boolean clean){
    	return StringifyCoordinates(latlng.latitude, latlng.longitude, clean);
    }
    
    private static String StringifyCoordinates(double latitude, double longitude, Boolean clean){
    	String lat = String.valueOf(latitude);
    	String lon = String.valueOf(longitude);
    	if (clean) {
    		lat = lat.substring(0, 9);
    		lon = lon.substring(0, 9);
    	}
    	return (lat + "," + lon);
    }
    
    private static LatLng GeolocateString(String coordinates) {
    	String[] coor = coordinates.split(",");
    	LatLng loc = new LatLng(Double.valueOf(coor[0]), Double.valueOf(coor[1]));
    	return loc;
    }
}