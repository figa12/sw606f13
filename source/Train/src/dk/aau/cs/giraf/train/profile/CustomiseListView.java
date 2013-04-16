package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.PictoFactory;
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
		
		this.adapter = new CustomiseAdapter(this.getContext(), android.R.layout.simple_list_item_1, stations);
		setAdapter(adapter);
	}
	
	public void addStation(Station station) {
		this.stations.add(station);
		adapter.notifyDataSetChanged();
	}
	
}
