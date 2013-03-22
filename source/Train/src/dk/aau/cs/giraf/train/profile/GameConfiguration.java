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
	
	public GameConfiguration(Context context, String gameName, int gameID, int childID) {
		this.gameName = gameName;
		this.childID = childID;
		this.gameID = gameID;
		this.context = context;
	}
    
	public void addStation(Station station) {
		this.stations.add(station);
	}
	
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	public class Station {
		
		Category category;
		
		public Station(Category category) {
			this.category = category;
		}
		
	}
	
	public class Category {
		Pictogram pictogramCategory;
		ArrayList<Pictogram> pictograms = new ArrayList<Pictogram>(); 
		
		public Category(Pictogram pictogramCategory) {
			this.pictogramCategory = pictogramCategory;
		}
		
		public void addPictogram() {
			pictograms.add(PictoFactory.INSTANCE.getPictogram(context, 0));
		}
		
		public Pictogram getCategory() {
			return pictogramCategory;
		}
	}
	
}
