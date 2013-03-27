package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomiseAdapter extends ArrayAdapter<Station> {
	
	private ArrayList<Station> items;

	public CustomiseAdapter(Context context,	int textViewResourceId, ArrayList<Station> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.customise_list, null);
		}
		Station s = items.get(position);
		if (s != null) {
			/* Find station */
			TextView tv = (TextView) v.findViewById(R.id.testytest);
			AddPictogramButton fl = (AddPictogramButton) v.findViewById(R.id.list_category);
			
			if (tv != null) {
				tv.setText(s.ID);
			}
			/*
			if (fl != null) {
				fl.addView(s.category);
			}
			*/
		}
		
	
		
		return v;	
		
	}

}
