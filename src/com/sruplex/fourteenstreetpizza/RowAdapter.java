package com.sruplex.fourteenstreetpizza;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RowAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] order_data;
	
	public RowAdapter(Context context, String[] order_data) {
		super(context, R.layout.row_order, order_data);
		this.context = context;
		this.order_data = order_data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView;
		rowView = inflater.inflate(R.layout.row_order, parent, false);

		TextView row_title = (TextView) rowView.findViewById(R.id.order_row_title);
		TextView row_info  = (TextView) rowView.findViewById(R.id.order_row_info);
		
		row_title.setText(order_data[position]);
		row_info.setText("yo bitches");
	
		return rowView;
	}


}