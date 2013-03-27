package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildAdapter extends ArrayAdapter<Child> {
	
	private ArrayList<Child> items;
	Guardian guard = Guardian.getInstance();
	
	public ChildAdapter(Context context, int textViewResourceId,
			ArrayList<Child> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.profile_list, null);
		}
		// TODO: Insert pictures from admin
		Child c = items.get(position);
		if (c != null) {
			/* Find all the views */
			ImageView iv = (ImageView) v.findViewById(R.id.profilePic);
			TextView tv = (TextView) v.findViewById(R.id.profileName);

			/* Set the picture */
			if (iv != null) {
				//TODO: Insert pictures here
				iv.setImageResource(R.drawable.default_profile);
			}
			
			/* If this is either last used or predefined, change the name */
			if (tv != null) {
				if (c.name == "Last Used") {
					tv.setText(R.string.last_used);
				} else if (c.name == "Predefined Profiles") {
					tv.setText(R.string.predefined);
				} else {
					tv.setText(c.name);
				}
			}

		}
		
			/* Is this profile selected, then highlight */
			if(c.getProfileId() == guard.profileID){
				v.setBackgroundResource(R.drawable.list_selected);
				guard.profileFirstClick = true;
			}
		return v;
	}
	
}
