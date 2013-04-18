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

public class CustomListView extends ListView {
    
	public Guardian guardian = Guardian.getInstance();
	private ChildAdapter adapter;
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setOnItemClickListener(messageClickedHandler);
	}
	
	public void loadChildren() {
		ArrayList<Child> children = guardian.publishList();
		
		this.adapter = new ChildAdapter(this.getContext(), R.drawable.list, children);
		
		this.setAdapter(adapter);
	}
	
	private OnItemClickListener messageClickedHandler = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	CustomListView.this.adapter.setSelectedPosition(position);
	    }
	};
}
