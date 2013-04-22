package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AssociatedPictogramsLayout extends LinearLayout {
    
    private ArrayList<PictogramButton> pictograms = new ArrayList<PictogramButton>();
    
    public AssociatedPictogramsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        
        this.addPictogram();
    }
    
    public void addPictogram() {
        this.pictograms.add(new PictogramButton(this.getContext()));
        
        
    }
}
