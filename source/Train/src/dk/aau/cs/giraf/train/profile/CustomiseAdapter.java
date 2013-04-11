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
	
	private ArrayList<Station> items;

	public CustomiseAdapter(Context context,	int textViewResourceId, ArrayList<Station> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
	
	private static class ViewHolder {
        int position;
}
	
	private HashMap holders = new HashMap();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.customise_list, null);
			
			holder = new ViewHolder();
			holder.position = position;
			
			final ImageView deleteButton = (ImageView)
					convertView.findViewById(R.id.deleteRowasdf);
			
			deleteButton.setOnClickListener(new ClickListener());
			
			convertView.setTag(holder);
	        deleteButton.setTag(holder);
	        
	        
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.position = position;
		holders.put(holder.position, holder);
		
		
		
		Station station = items.get(position);
		//if (fl.getChildAt(0) != null) {layout
		//s.category = (Pictogram) fl.getChildAt(0);
		//}
		
		if (station != null) {
			/* Find station */
			PictogramButton categoryButton = (PictogramButton) convertView.findViewById(R.id.list_category);
			TextView testTextView = (TextView) convertView.findViewById(R.id.testytest);
			
			//DeleteRowButton dr = (DeleteRowButton) v.findViewById(R.id.deleteRowasdf);
			
			if (testTextView != null) {
				testTextView.setText(station.ID);
			}
			
			
			if (categoryButton.getChildAt(0) != null && categoryButton != null) {
				categoryButton.removeAllViews();
				Pictogram tempCategory = station.category;
				PictogramButton tempParent = (PictogramButton) tempCategory.getParent();
				if (tempParent != null) {
					tempParent.removeAllViews();
				}
				categoryButton.addView(tempCategory);
			}
			
		}
		return convertView;	
		
	}
	
	public void setCategories(ViewGroup parent) {
		CustomiseListView list = (CustomiseListView) parent.getParent();
		int totalChildren = list.getChildCount();
		for (int i=0; i<totalChildren; i++) {
			LinearLayout listItem = (LinearLayout) list.getChildAt(i);
			LinearLayout categoryLayout = (LinearLayout) listItem.getChildAt(0);
			PictogramButton button = (PictogramButton) categoryLayout.getChildAt(0);
			
			if (button.getChildAt(0) != null) {
				Pictogram category = (Pictogram) button.getChildAt(0);
				Station station = this.items.get(i);
				station.category = category;
				this.items.set(i, station);
				notifyDataSetChanged();
			}
		}
	}
	
	
	private final class ClickListener implements OnClickListener {
		
		
		@Override
		public void onClick(View view) {
			
			View parent = (View) view.getParent();
			ViewGroup grandparent = (ViewGroup) parent.getParent();
			CustomiseAdapter.this.setCategories(grandparent);
			
			
			 //LinearLayout firstParentView = (LinearLayout) v.getParent();
			 //LinearLayout secondParentView = (LinearLayout) firstParentView.getParent();
			 
			 //LinearLayout tempview = (LinearLayout) secondParentView.getChildAt(0);
			 
			 //for(Station item : items) {
				//final PictogramButton itemCategory = (PictogramButton) secondParentView.findViewById(R.id.list_category);
				//holder.pictogram = itemCategory;
				//item.category = (Pictogram) itemCategory.getChildAt(0);
				
			 //}
			
		    ViewHolder deleteHolder = (ViewHolder) view.getTag();
		    
		    int pos = deleteHolder.position;
		    CustomiseAdapter.this.holders.remove(pos);


		    ViewHolder currentHolder;

		    // Shift 'position' of list items
		    // down since 'pos' was deleted
		    for(int i=pos+1; i<getCount(); i++){
		        currentHolder = (ViewHolder) CustomiseAdapter.this.holders.get(i);
		        currentHolder.position = i-1;
		    }
		    CustomiseAdapter.this.items.remove(pos);
		    notifyDataSetChanged();
		}
	}
	
	
	

}
