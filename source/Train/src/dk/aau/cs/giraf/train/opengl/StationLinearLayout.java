package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.Pictogram;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/***
 * StationLinearlayout a custom class with methods aiding to simplify the code.
 * This class respresents the linearlayouts of the station and has methods to support
 * the use of these layouts.
 * @author Jacob
 *
 */
public class StationLinearLayout extends GameActivityLinearLayout{ 
	
	public StationLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<PictoFrameLayout> getPictoframes() {
		ArrayList<PictoFrameLayout> pictoFrames = new ArrayList<PictoFrameLayout>();
		for (int i = 0; i < this.getChildCount(); i++) {
			pictoFrames.add(((PictoFrameLayout)this.getChildAt(i)));
		}
		return pictoFrames;
	}
	
	public ArrayList<Pictogram> getPictograms(){
		ArrayList<Pictogram> pictograms = new ArrayList<Pictogram>();
		for (PictoFrameLayout pictoFrame : this.getPictoframes()) {
			if(pictoFrame.getChildCount() > 0){
				if(pictoFrame.getChildAt(0).getClass() == Pictogram.class){
					pictograms.add((Pictogram)pictoFrame.getChildAt(0));
				}
			}
		}
		return pictograms;
	}
	
}
