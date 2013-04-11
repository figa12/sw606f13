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
        Pictogram pictogram;
}
	
	private HashMap mHolders = new HashMap();
	
	@Override
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
		//if (fl.getChildAt(0) != null) {
		//s.category = (Pictogram) fl.getChildAt(0);
		//}
		
		if (s != null) {
			/* Find station */
			PictogramButton fl = (PictogramButton) v.findViewById(R.id.list_category);
			TextView tv = (TextView) v.findViewById(R.id.testytest);
			
			//DeleteRowButton dr = (DeleteRowButton) v.findViewById(R.id.deleteRowasdf);
			
			if (tv != null) {
				tv.setText(s.ID);
			}
			
			
			if (fl.getChildAt(0) != null && fl != null) {
				fl.removeAllViews();
				Pictogram tempCategory = s.category;
				PictogramButton tempParent = (PictogramButton) tempCategory.getParent();
				if (tempParent != null) {
					tempParent.removeAllViews();
				}
				fl.addView(tempCategory);
				int a = 2;
			}
			
		}
		return v;	
		
	}
	
	public void setCategories(ViewGroup parent) {
		CustomiseView list = (CustomiseView) parent.getParent();
		int totalChildren = list.getChildCount();
		for (int i=0; i<totalChildren; i++) {
			LinearLayout layout = (LinearLayout) list.getChildAt(i);
			LinearLayout categoryLayout = (LinearLayout) layout.getChildAt(0);
			PictogramButton button = (PictogramButton) categoryLayout.getChildAt(0);
			if (button.getChildAt(0) != null) {
			Pictogram category = (Pictogram) button.getChildAt(0);
			Station s = items.get(i);
			s.category = category;
			items.set(i, s);
			notifyDataSetChanged();
			}
		}
	}
	
	
	private final class ClickListener implements OnClickListener {
		
		
		@Override
		public void onClick(View v) {
			
			View parent = (View) v.getParent();
			ViewGroup grandparent = (ViewGroup) parent.getParent();
			setCategories(grandparent);
			
			
			 //LinearLayout firstParentView = (LinearLayout) v.getParent();
			 //LinearLayout secondParentView = (LinearLayout) firstParentView.getParent();
			 
			 //LinearLayout tempview = (LinearLayout) secondParentView.getChildAt(0);
			 
			 //for(Station item : items) {
				//final PictogramButton itemCategory = (PictogramButton) secondParentView.findViewById(R.id.list_category);
				//holder.pictogram = itemCategory;
				//item.category = (Pictogram) itemCategory.getChildAt(0);
				
			 //}
			
		    ViewHolder deleteHolder = (ViewHolder) v.getTag();
		    
		    int pos = deleteHolder.position;
		    mHolders.remove(pos);


		    ViewHolder currentHolder;

		    // Shift 'position' of list items
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
