package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import android.content.Context;
import dk.aau.cs.giraf.pictogram.*;

public class GameConfiguration {
	
	public String 	gameName;
	public int		childID;
	public int		gameID;
	private Context context;
	
	ArrayList<Station> stations = new ArrayList<Station>();

	private class Station {
		
		private class Category {
			
			Pictogram category;
			ArrayList<Pictogram> pictograms = new ArrayList<Pictogram>(); 
			
			public void saveCategory() {
				category = PictoFactory.INSTANCE.getPictogram(context, 0);
			}
			
			public Pictogram getCategory() {
				return category;
			}
		}
	}
	
}
