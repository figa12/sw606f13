package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import dk.aau.cs.giraf.TimerLib.*;
import dk.aau.cs.giraf.pictogram.*;
import dk.aau.cs.giraf.train.Data;

public class GameConfiguration {

	public long guardianID;
	public String 	gameName;
	public int		childID;
	public int		gameID;
	ArrayList<Station> stations = new ArrayList<Station>();
	
	public GameConfiguration(String gameName, int gameID, int childID) {
		this.gameName = gameName;
		this.childID = childID;
		this.gameID = gameID;
		this.guardianID = Data.currentGuardianID;
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

	public class Station {
		
		long category;
		ArrayList<Long> acceptPictograms = new ArrayList<Long>(); 
		
		public Station(long CategoryPictogramId) {
			this.category = CategoryPictogramId;
		}
		
		public void addAcceptPictogram(long id) {
			acceptPictograms.add(id);
		}
		
		public ArrayList<Long> getAcceptPictograms(){
			return this.acceptPictograms;
		}
		
		public void setCategory(long category){
			this.category = category;
		}
		
		public long getCategory(){
			return this.category;
		}
		
		public long getAcceptPictogram(int id){
			return this.acceptPictograms.get(id);
		}
	}
	
	public HashMap<String, String> getHashMap() {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("gameName", this.gameName);
		map.put("childID", String.valueOf(this.childID));
		map.put("stations", String.valueOf(this.stations));
		map.put("guardianID", String.valueOf(this.guardianID));
		
		return map;
	}
}
