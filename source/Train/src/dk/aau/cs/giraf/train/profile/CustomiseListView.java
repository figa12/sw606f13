package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
/**
 * 
 * @author figa
 *
 */
public class CustomiseListView extends ListView {
	
	ArrayList<Station> stations = new ArrayList<Station>();
	CustomiseAdapter adapter;

	public CustomiseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		Station station1 = new Station("STATION 1");
		Station station2 = new Station("STATION 2");
		Station station3 = new Station("STATION 3");
		
		stations.add(station1);
		stations.add(station2);
		stations.add(station3);
		
		this.adapter = new CustomiseAdapter(this.getContext(), android.R.layout.simple_list_item_1, stations, this);
		this.setAdapter(adapter);
	}
	
	public void addStation(Station station) {
		this.setCategories();
		this.stations.add(station);
		this.adapter = new CustomiseAdapter(this.getContext(), android.R.layout.simple_list_item_1, stations, this);
		this.setAdapter(adapter);
		
		//adapter.notifyDataSetChanged();
	}
	
	public void removeStation(int position) {
		this.setCategories();
		this.stations.remove(position);
		this.adapter = new CustomiseAdapter(this.getContext(), android.R.layout.simple_list_item_1, stations, this);
		this.setAdapter(adapter);
	}
	
	public void setCategories() { 
		
		for (int i=0; i<this.getChildCount(); i++) {
			LinearLayout listItem = (LinearLayout) this.getChildAt(i);
			LinearLayout categoryLayout = (LinearLayout) listItem.getChildAt(0);
			PictogramButton button = (PictogramButton) categoryLayout.getChildAt(0);
			
			if (button.getChildAt(0) != null) {
				Pictogram category = (Pictogram) button.getChildAt(0);
				Station station = this.stations.get(i);
				station.category = category;
				this.stations.set(i, station);
			}
		}
	}
	
}
