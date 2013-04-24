package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * A layout containing the pictograms associated to the station.
 * @author Jesper Riemer Andersen
 *
 */
public class AssociatedPictogramsLayout extends LinearLayout implements PictogramReceiver {
    
    private Station station;
    private ArrayList<PictogramButton> pictogramButtons = new ArrayList<PictogramButton>();
    
    public AssociatedPictogramsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void bindStation(Station station) {
        this.station = station;
    }
    
    public void addPictogram(long pictogramId) {
        PictogramButton pictogramButton = new PictogramButton(this.getContext());
        pictogramButton.setPictogram(pictogramId);
        pictogramButton.setRemovable(true);
        
        this.pictogramButtons.add(pictogramButton);
        super.addView(pictogramButton);
        
        this.bindPictograms(); //Update the station with the new pictogram
    }
    
    private void bindPictograms() {
        this.station.pictograms.clear();
        
        for (PictogramButton pictogramButton : this.pictogramButtons) {
            this.station.pictograms.add(pictogramButton.getPictogram());
        }
    }
    
    @Override
    public void removeView(View view) {
        super.removeView(view);
        this.pictogramButtons.remove(view);
    }
    
    @Override
    public void receivePictograms(long[] pictogramIds, int requestCode) {
        for (long id : pictogramIds) {
            this.addPictogram(id);
        }
    }
}
