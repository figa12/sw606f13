package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class CustomListView extends ListView {
	public Guardian guardian;
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setOnItemClickListener(messageClickedHandler);
	}
	
	public void loadChildren() {
		ArrayList<Child> children = guardian.publishList();
		
		ChildAdapter adapter = new ChildAdapter(this.getContext(), android.R.layout.simple_list_item_1, children);
		
		//String myStringArray[] = {"abc", "bcd", "cde", "def", "efg"};
		//ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, myStringArray);
		setAdapter(adapter);
	}
	
	private OnItemClickListener messageClickedHandler = new OnItemClickListener() {
		
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	    	Toast.makeText(getContext(), "Yay", Toast.LENGTH_SHORT).show();
	    }
	};	
	
}
