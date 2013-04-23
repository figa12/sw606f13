package dk.aau.cs.giraf.train.profile;

import java.util.Random;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * A layout containing a pictogram. Click the button to choose a pictogram.
 * @author Jesper Riemer Andersen
 * //TODO
 */
public class PictogramButton extends LinearLayout {
    
    private FrameLayout pictogramContainer;
    private long pictogramId = -1L;
    private ImageButton removeButton;
    
	public PictogramButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBackgroundResource(R.drawable.shape_white);
		
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pictogramLayout = layoutInflater.inflate(R.layout.pictogram_layout, null);
		super.addView(pictogramLayout);
		
		this.pictogramContainer = (FrameLayout) pictogramLayout.findViewById(R.id.pictogramContainer);
		
		this.removeButton = (ImageButton) pictogramLayout.findViewById(R.id.removeButton);
		this.removeButton.setVisibility(ImageButton.INVISIBLE);
		this.removeButton.setOnClickListener(new RemoveClickListener());
		
		super.setOnClickListener(new SelectPictogramClickListener());
	}
	
	public void setIsRemovable(boolean isRemovable) {
	    if(isRemovable) {
	        this.removeButton.setVisibility(ImageButton.VISIBLE);
	    }
	}
	
	public void setPictogram(long pictogramId) {
	    this.pictogramId = pictogramId;
	    this.pictogramContainer.removeAllViews();
	    
	    if(pictogramId == -1L) {
	        return;
	    }
	    
	    Pictogram pictogram = PictoFactory.INSTANCE.getPictogram(getContext(), pictogramId);
        pictogram.renderImage();
        pictogram.renderText();
        
        this.pictogramContainer.addView(pictogram);
	}
	
	public long getPictogramId() {
	    return this.pictogramId;
	}
	
	private final class SelectPictogramClickListener implements OnClickListener {
	    private Random rand = new Random();
	    
        @Override
        public void onClick(View view) {
            int min = 1;
            int max = 5;
            PictogramButton.this.setPictogram(rand.nextInt(max - min + 1) + min);
        }
    }
	
	private final class RemoveClickListener implements OnClickListener {
	    
	    @Override
	    public void onClick(View view) {
	        if(PictogramButton.this.removeButton.getVisibility() == ImageButton.VISIBLE) {
	            ((ViewGroup) PictogramButton.this.getParent()).removeView(PictogramButton.this);
	        }
	    }
	}
}
