package dk.aau.cs.giraf.train.profile;

import java.util.Random;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class PictogramButton extends FrameLayout {
    
	public PictogramButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PictogramButton(Context context) { 
		super(context);
	}
	
	public void bindStation(Station station) {
	    this.setOnClickListener(new AddClickListener(station));
	}
	
	private final class AddClickListener implements OnClickListener {
        
	    private Station station;
	    
	    public AddClickListener(Station station) {
	        this.station = station;
	    }
	    
        @Override
        public void onClick(View view) {
            Random rand = new Random();
            int min = 1;
            int max = 5;
            int randomNum = rand.nextInt(max - min + 1) + min;
            long pictoID = (long) randomNum;
            
            //List<Pictogram> allPictograms = PictoFactory.INSTANCE.getAllPictograms(getContext());
            Pictogram pictogram = PictoFactory.INSTANCE.getPictogram(getContext(), pictoID);
            pictogram.renderImage();
            PictogramButton.this.removeAllViews();
            PictogramButton.this.addView(pictogram);
            
            this.station.category = pictogram;
        }
    }
}
