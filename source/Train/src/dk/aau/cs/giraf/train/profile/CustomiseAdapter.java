package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.HashMap;

import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CustomiseAdapter extends ArrayAdapter<Station> {
	
	private CustomiseListView listView;

	public CustomiseAdapter(Context context,	int textViewResourceId, ArrayList<Station> items, CustomiseListView listView) {
		super(context, textViewResourceId, items);
		
		this.listView = listView;
		this.setNotifyOnChange(false);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    //Inflate the view every time, because getView behaves weird with the Pictogram view
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.customise_list, null);
        
        ImageView deleteButton = (ImageView) convertView.findViewById(R.id.deleteRowButton);
        deleteButton.setOnClickListener(new ClickListener(position));
        
		Station station = super.getItem(position);
		
        /* Find station */
        PictogramButton categoryButton = (PictogramButton) convertView.findViewById(R.id.list_category);

        if (station.category != null) {
            PictogramButton tempParent = (PictogramButton) station.category.getParent();
            if (tempParent != null) {
                tempParent.removeView(station.category);
            }
            categoryButton.addView(station.category);
        }
		return convertView;	
		
	}	
	
	private final class ClickListener implements OnClickListener {
		
		private int position;
		
		public ClickListener(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(View view) {
			listView.removeStation(this.position);
		}
	}
	
	
	

}
