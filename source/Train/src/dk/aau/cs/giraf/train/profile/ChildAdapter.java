package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The ChildAdapter class is an adapter for {@link ChildrenListView} that extends {@link ArrayAdapter}.
 * @see {@link Child}
 * @see {@link ChildrenListView}
 * @author Nicklas Andersen
 */
public class ChildAdapter extends ArrayAdapter<Child> {
	
	private ArrayList<Child> children;
	private int selectedPosition = 0;
	private Child selectedChild;
	
	public ChildAdapter(Context context, int textViewResourceId, ArrayList<Child> items) {
		super(context, textViewResourceId, items);
		this.children = items;
	}
	
	/**
	 * @return The selected child.
	 * @see {@link Child}
	 */
	public Child getSelectedChild() {
	    return this.selectedChild;
	}
	
	/**
	 * This method sets the position of the selected item.
	 * @param position The position of the selected item
	 */
	public void setSelectedPosition(int position) {
	    this.selectedPosition = position;
	    this.notifyDataSetChanged();
	}
	
	/**
	 * The getView method is used to inflate an listitem.
	 * @param position The index of the current list item.
	 * @param convertView The view of the current list item.
	 * @param parent The view of the list that is to be inflated.
	 * @see LayoutInflater
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.profile_list_item, null);
		}
		
		Child child = this.children.get(position);
		
        /* Find all the views */
        ImageView profilePictureImageView = (ImageView) convertView.findViewById(R.id.profilePic);
        TextView profileNameTextView = (TextView) convertView.findViewById(R.id.profileName);

        //Set the picture 
        profilePictureImageView.setImageResource(R.drawable.default_profile); //TODO: Insert pictures here

        /* If this is either last used or predefined, change the name */
        if (child.name == "Last Used") {
            profileNameTextView.setText(R.string.last_used);
        } else if (child.name == "Predefined Profiles") {
            profileNameTextView.setText(R.string.predefined);
        } else {
            profileNameTextView.setText(child.name);
        }
		
        //Is this profile selected, then highlight
        if (position == this.selectedPosition) {
            this.selectedChild = child;
            convertView.setBackgroundResource(R.drawable.list_item_selected);
        }
        else {
            convertView.setBackgroundResource(R.drawable.list_item);
        }
		return convertView;
	}
}
