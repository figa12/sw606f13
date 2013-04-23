package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

/**
 * The ChildrenListView class is a {@link ListView} that lists children with the {@link ChildAdapter}.
 * @see Child
 * @see Guardian
 * @see ChildAdapter
 * @author Nicklas Andersen
 */
public class ChildrenListView extends ListView {
    
	public Guardian guardian = Guardian.getInstance();
	private ChildAdapter adapter;
	
	public ChildrenListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setOnItemClickListener(messageClickedHandler);
	}
	
	/**
	 * loadChildren() fills the list with children associated to the current guardian.
	 * @see Guardian
	 * @see ChildAdapter
	 * @see Child
	 */
	public void loadChildren() {
		ArrayList<Child> children = guardian.publishList();
		
		this.adapter = new ChildAdapter(this.getContext(), R.drawable.list, children);
		
		this.setAdapter(adapter);
	}
	
	private OnItemClickListener messageClickedHandler = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	ChildrenListView.this.adapter.setSelectedPosition(position);
	    }
	};
}
