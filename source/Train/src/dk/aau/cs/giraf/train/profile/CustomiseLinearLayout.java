package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomiseLinearLayout extends LinearLayout {
    
    private ArrayList<Station> stations = new ArrayList<Station>();
    
    public CustomiseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void addStation(Station station) {
        this.stations.add(station);
        
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customiseItem = layoutInflater.inflate(R.layout.station_list_item, null);
        
        PictogramButton categoryPictogramButton = (PictogramButton) customiseItem.findViewById(R.id.list_category);
        categoryPictogramButton.bindStation(station);
        
        ImageView deleteButton = (ImageView) customiseItem.findViewById(R.id.deleteRowButton);
        deleteButton.setOnClickListener(new RemoveClickListener(station));
        
        this.addView(customiseItem);
    }
    
    public void removeStation(int index) {
        if(index > this.stations.size() - 1) {
            return;
        }
        this.removeViewAt(index);
        this.stations.remove(index);
    }
    
    public void removeStation(Station station) {
        this.removeStation(this.stations.indexOf(station));
    }
    
    private final class RemoveClickListener implements OnClickListener {
        
        private Station station;
        
        public RemoveClickListener(Station station) {
            this.station = station;
        }
        
        @Override
        public void onClick(View view) {
            CustomiseLinearLayout.this.removeStation(this.station);
        }
    }
}
