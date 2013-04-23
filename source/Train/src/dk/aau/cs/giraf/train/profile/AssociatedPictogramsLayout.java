package dk.aau.cs.giraf.train.profile;

import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AssociatedPictogramsLayout extends LinearLayout {
    
    private Station station;
    
    public AssociatedPictogramsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //ImageButton addStationButton = (ImageButton) super.findViewById(R.id.addPictogramButton);
        //addStationButton.setOnClickListener(new AddStationClickListener());
    }
    
    public void bindStation(Station station) {
        this.station = station;
    }
    
    public void addPictogram() {
        
    }
    
    public void removePictogram() {
        
    }
    
    private class AddStationClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            AssociatedPictogramsLayout.this.addPictogram();
        }
    }
}
