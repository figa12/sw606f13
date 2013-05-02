package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.TimerLib.*;
import dk.aau.cs.giraf.pictogram.*;
import dk.aau.cs.giraf.train.Data;

public class GameConfiguration implements Parcelable {

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
	
	public String getGameName() {
	    return this.gameName;
	}
	
	public int getNumberOfPictogramsOfStations(){
		int numberOfPictograms = 0;
		for (StationConfiguration station : this.stations) {
			numberOfPictograms += station.getAcceptPictograms().size();
		}
		return numberOfPictograms;
	}
	
	public ArrayList<Long> getIdOfAllPictograms(){
		ArrayList<Long> pictogramIds =new ArrayList<Long>();
		
		for (StationConfiguration station : this.stations) {
			pictogramIds.addAll(station.getAcceptPictograms());
		}
		return pictogramIds;
	}
	
	public HashMap<String, String> getHashMap() {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("gameName", this.gameName);
		map.put("childID", String.valueOf(this.childID));
		map.put("stations", String.valueOf(this.stations));
		map.put("guardianID", String.valueOf(this.guardianID));
		
		return map;
	}
	
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.guardianID);
        out.writeString(this.gameName);
        out.writeLong(this.childID);
        out.writeLong(this.gameID);
        out.writeList(this.stations);
    }
    
    public static final Parcelable.Creator<GameConfiguration> CREATOR = new Parcelable.Creator<GameConfiguration>() {
        @Override
        public GameConfiguration createFromParcel(Parcel in) {
            return new GameConfiguration(in);
        }
        
        @Override
        public GameConfiguration[] newArray(int size) {
            return new GameConfiguration[size];
        }
    };
    
    private GameConfiguration(Parcel in) {
        this.guardianID = in.readLong();
        this.gameName = in.readString();
        this.childID = in.readLong();
        this.gameID = in.readLong();
        in.readList(this.stations, StationConfiguration.class.getClassLoader());
    }
}
