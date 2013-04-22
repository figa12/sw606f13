package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AssociatedPictogramsLayout extends LinearLayout {
    
    private Station station;
    private ArrayList<PictogramButton> pictogramButtons = new ArrayList<PictogramButton>();
    private LinearLayout associatedPictogramLayout;
    
    public AssociatedPictogramsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void bindStation(Station station) {
        this.station = station;
    }
    
    public void addPictogram() {
        this.addPictogram(new PictogramButton(super.getContext()));
    }
    
    public void addPictogram(PictogramButton pictogramButton) {
        this.pictogramButtons.add(pictogramButton);
        super.addView(pictogramButton);
    }
    
    public void removePictogram(int index) {
        if(index > this.pictogramButtons.size() - 1) {
            return;
        }
        super.removeViewAt(index);
        this.pictogramButtons.remove(index);
    }
    
    public void removePictogram(PictogramButton pictogramButton) {
        this.removePictogram(this.pictogramButtons.indexOf(pictogramButton));
    }
}
