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
	
	public ArrayList<Station> getStations(){
		return this.stations;
	}
	
	public Station getStation(int value){
		return this.stations.get(value);
	}

	public class Station {
		
		Category category;
		ArrayList<Pictogram> acceptPictograms = new ArrayList<Pictogram>(); 
		
		public Station(Category category) {
			this.category = category;
		}
		
		public void addAcceptPictogram(int id) {
			acceptPictograms.add(PictoFactory.INSTANCE.getPictogram(context, id));
		}
		
		public ArrayList<Pictogram> getAcceptPictograms(){
			return this.acceptPictograms;
		}
	}
	
	public class Category {
		Pictogram pictogramCategory;
		
		public Category(Pictogram pictogramCategory) {
			this.pictogramCategory = pictogramCategory;
		}
		
		public Pictogram getCategory() {
			return pictogramCategory;
		}
	}
	
}
