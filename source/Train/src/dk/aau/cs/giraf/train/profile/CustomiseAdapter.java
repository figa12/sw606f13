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
import android.widget.TextView;

public class CustomiseAdapter extends ArrayAdapter<Station> {
	
	private ArrayList<Station> items;

	public CustomiseAdapter(Context context,	int textViewResourceId, ArrayList<Station> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
	
	private static class ViewHolder {
        int position;
        Pictogram pictogram;
}
	
	private HashMap mHolders = new HashMap();
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View v = convertView;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.customise_list, null);
			holder = new ViewHolder();
			holder.position = position;
			
			final ImageView deleteButton = (ImageView)
					v.findViewById(R.id.deleteRowasdf);
			
			deleteButton.setOnClickListener(new ClickListener());
			
			v.setTag(holder);
	        deleteButton.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.position = position;
		mHolders.put(holder.position, holder);
		
		Station s = items.get(position);
		if (s != null) {
			/* Find station */
			TextView tv = (TextView) v.findViewById(R.id.testytest);
			PictogramButton fl = (PictogramButton) v.findViewById(R.id.list_category);
			//DeleteRowButton dr = (DeleteRowButton) v.findViewById(R.id.deleteRowasdf);
			
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
	
	
	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
		    ViewHolder deleteHolder = (ViewHolder) v.getTag();
		    int pos = deleteHolder.position;
		    mHolders.remove(pos);


		    ViewHolder currentHolder;

		    // Shift 'position' of remaining languages 
		    // down since 'pos' was deleted
		    for(int i=pos+1; i<getCount(); i++){
		        currentHolder = (ViewHolder) mHolders.get(i);
		        currentHolder.position = i-1;
		    }
		    items.remove(pos);
		    notifyDataSetChanged();
		}
	}
	
	
	

}
