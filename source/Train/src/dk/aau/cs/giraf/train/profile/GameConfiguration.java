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
	
	public int getNumberOfPictogramsOfStations(){
		int numberOfPictograms = 0;
		for (Station station : this.stations) {
			numberOfPictograms += station.acceptPictograms.size();
		}
		return numberOfPictograms;
	}
	
	public void setContext(Context con){
		this.context = con;
	}
	
	public Context getContext(){
		return this.context;
	}

	public class Station {
		
		Pictogram category;
		ArrayList<Pictogram> acceptPictograms = new ArrayList<Pictogram>(); 
		
		public Station(long CategoryPictogramId) {
			this.category = PictoFactory.INSTANCE.getPictogram(context, CategoryPictogramId);
		}
		
		public void addAcceptPictogram(long id) {
			acceptPictograms.add(PictoFactory.INSTANCE.getPictogram(context, id));
		}
		
		public ArrayList<Pictogram> getAcceptPictograms(){
			return this.acceptPictograms;
		}
		
		public void setCategory(Pictogram category){
			this.category = category;
		}
		
		public Pictogram getCategory(){
			return this.category;
		}
	}
}
