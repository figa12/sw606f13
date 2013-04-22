package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AssociatedPictogramsLayout extends LinearLayout {
    
    private ArrayList<PictogramButton> pictogramButtons = new ArrayList<PictogramButton>();
    private LinearLayout associatedPictogramLayout;
    
    public AssociatedPictogramsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        
        
    }
    
    public void addPictogram(PictogramButton pictogramButton) {
        this.pictogramButtons.add(pictogramButton);
        
        //TODO 
    }
    
    public void removePictogram(int index) {
        if(index > this.pictogramButtons.size() - 1) {
            return;
        }
        this.removeViewAt(index);
        this.pictogramButtons.remove(index);
    }
    
    public void removePictogram(PictogramButton pictogramButton) {
        this.removePictogram(this.pictogramButtons.indexOf(pictogramButton));
    }
}
