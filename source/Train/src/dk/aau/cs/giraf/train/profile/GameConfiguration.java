package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import dk.aau.cs.giraf.TimerLib.*;
import dk.aau.cs.giraf.pictogram.*;
import dk.aau.cs.giraf.train.Data;

public class GameConfiguration {

    private long   guardianID;
	private String gameName;
	private long   childID;
	public long   gameID;
	private ArrayList<StationConfiguration> stations = new ArrayList<StationConfiguration>();
	
	public GameConfiguration(String gameName, long gameID, long childID) {
		this.gameName = gameName;
		this.childID = childID;
		this.gameID = gameID;
		this.guardianID = Data.currentGuardianID;
	}
    
	public void addStation(StationConfiguration station) {
		this.stations.add(station);
	}
	
	public void setStations(ArrayList<StationConfiguration> stations) {
		this.stations = stations;
	}
	
	public ArrayList<StationConfiguration> getStations(){
		return this.stations;
	}
	
	public StationConfiguration getStation(int value){
		return this.stations.get(value);
	}
	
	public int getNumberOfPictogramsOfStations(){
		int numberOfPictograms = 0;
		for (StationConfiguration station : this.stations) {
			numberOfPictograms += station.getAcceptPictograms().size();
		}
		return numberOfPictograms;
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
