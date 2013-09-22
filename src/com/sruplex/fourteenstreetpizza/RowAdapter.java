package com.sruplex.fourteenstreetpizza;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RowAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] order_data;
	
	public RowAdapter(Context context, String[] order_data) {
		super(context, R.layout.row_order, order_data);
		this.context = context;
		this.order_data = order_data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView;
		rowView = inflater.inflate(R.layout.row_order, parent, false);

		Spinner     row_qty   = (Spinner)     rowView.findViewById (R.id.order_row_qty);
		TextView    row_title = (TextView)    rowView.findViewById (R.id.order_row_title);
		TextView    row_info  = (TextView)    rowView.findViewById (R.id.order_row_info);
		ImageView   row_img   = (ImageView)   rowView.findViewById (R.id.order_row_img);
		ImageButton row_cncl  = (ImageButton) rowView.findViewById (R.id.order_row_cancel);
		
		row_cncl.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				OrderValues.Title.remove(position);
				OrderValues.Description.remove(position);
				OrderValues.Quantity.remove(position);
				OrderValues.Price.remove(position);
				OrderValues.Image.remove(position);
				
				OrderActivity.RefreshOrderList(context);
			}
		});
		
		row_qty.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int selection, long arg3) {
				OrderValues.Quantity.set(position, Integer.valueOf(parent.getItemAtPosition(selection).toString()));
				OrderActivity.UpdateOrderCostings(context);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		row_title.setText(order_data[position]);
		row_info.setText(OrderValues.Description.get(position));
		row_img.setBackgroundResource(OrderValues.Image.get(position));
	
		return rowView;
	}


}