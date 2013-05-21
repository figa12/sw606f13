package dk.aau.cs.giraf.train;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;

/**
 * The ChildrenListView class is a {@link ListView} that lists children with the {@link ChildAdapter}.
 * @see Child
 * @see Guardian
 * @see ChildAdapter
 * @author Nicklas Andersen
 */
public class ChildrenListView extends ListView {
    
	private ChildAdapter adapter;
	
	public ChildrenListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		super.setOnItemClickListener(new OnItemClickListener() {
            @SuppressWarnings("rawtypes")
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ChildrenListView.this.adapter.setSelectedPosition(position);
            }
        });
	}
	
	/**
	 * loadChildren() fills the list with children associated to the current guardian.
	 * @see Guardian
	 * @see ChildAdapter
	 * @see Child
	 */
	public void loadChildren(Guardian guardian) {
		ArrayList<Child> children = guardian.publishList();
		
		//We want to remove the children with the names "Last Used" and "Predefined Profiles".
		//This is a terrible solution to remove them, but java does not have built-in support for this.
		for (int i = 0; i < children.size(); i++) {
		    if (children.get(i).name == "Last Used" || children.get(i).name == "Predefined Profiles") {
		        children.remove(i);
		        i--; //Removed an item, then go back one index
		    }
		}
		
		this.adapter = new ChildAdapter(super.getContext(), R.drawable.list_item, children);
		super.setAdapter(this.adapter);
		if(this.adapter.getCount() != 0) {
		    this.adapter.setSelectedPosition(0);
		}
	}
	
	public Child getSelectedChild() {
		return this.adapter.getSelectedChild();
	}
}
