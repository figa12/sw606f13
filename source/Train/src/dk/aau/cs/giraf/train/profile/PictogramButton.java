package dk.aau.cs.giraf.train.profile;

import java.util.List;
import java.util.Random;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PictogramButton extends FrameLayout {

	public PictogramButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PictogramButton(Context context) { 
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			Random rand = new Random();
			int min = 1;
			int max = 5;
			int randomNum = rand.nextInt(max - min + 1) + min;
			long pictoID = (long) randomNum;
			
			//List<Pictogram> allPictograms = PictoFactory.INSTANCE.getAllPictograms(getContext());
			Pictogram p = PictoFactory.INSTANCE.getPictogram(getContext(), pictoID);
			p.renderImage();
			if (this.getChildAt(0) != null) {
			removeAllViews();
			}
			addView(p);

		}
		return true;
	}
}
