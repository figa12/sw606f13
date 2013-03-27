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
import android.widget.ListView;

public class CustomiseView extends ListView {

	public CustomiseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		Station station1 = new Station("STATION 1");
		Station station2 = new Station("STATION 2");
		ArrayList<Station> stations = new ArrayList<Station>();
		stations.add(station1);
		stations.add(station2);
		
		CustomiseAdapter adapter = new CustomiseAdapter(this.getContext(), android.R.layout.simple_list_item_1, stations);
		setAdapter(adapter);
	}
	
}
