package dk.aau.cs.giraf.train.profile;

import java.util.Random;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class PictogramButton extends FrameLayout {
    
    private Station station;
    
	public PictogramButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PictogramButton(Context context) { 
		super(context);
	}
	
	public void setPictogram(long pictogramId) {
	    Pictogram pictogram = PictoFactory.INSTANCE.getPictogram(getContext(), pictogramId);
        pictogram.renderImage();
        pictogram.renderText();
        
        PictogramButton.this.removeAllViews();
        PictogramButton.this.addView(pictogram);
        
        this.station.category = pictogram; //Save the pictogram inside the station
	}
	
	public void bindStation(Station station) {
	    this.station = station;
	    
	    this.setOnClickListener(new AddClickListener());
	}
	
	private final class AddClickListener implements OnClickListener {
        
        @Override
        public void onClick(View view) {
            Random rand = new Random();
            int min = 1;
            int max = 5;
            long pictoID = rand.nextInt(max - min + 1) + min;
            
            Pictogram pictogram = PictoFactory.INSTANCE.getPictogram(getContext(), pictoID);
            pictogram.renderImage();
            pictogram.renderText();
            PictogramButton.this.removeAllViews();
            PictogramButton.this.addView(pictogram);
            
            PictogramButton.this.station.category = pictogram;
        }
    }
}
