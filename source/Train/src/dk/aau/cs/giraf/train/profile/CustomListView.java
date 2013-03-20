package dk.aau.cs.giraf.train.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomListView extends ListView {
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void loadChildren() {
		String myStringArray[] = {"abc", "bcd", "cde", "def", "efg"};
		ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, myStringArray);
		setAdapter(adapter);
	}
}
