package dk.aau.cs.giraf.train.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import dk.aau.cs.giraf.train.R;

public class RowButton extends FrameLayout {

	public RowButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			View view = (View) getRootView();
			CustomiseListView list = (CustomiseListView) view.findViewById(R.id.customiseListView);
			Station station = new Station("Newly added station");
			list.addStation(station);

			return true;
		} else {
			return false;
		}
	}
}