package dk.aau.cs.giraf.train.profile;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.train.Data;

/**
 * This class contains all information of a game configuration.
 * It contains the id of the child associated with it, aswell as the guardian id, and a list of stations.
 * @see StationConfiguration
 * @author figa
 *
 */
public class GameConfiguration implements Parcelable {

    private long   guardianID;
	private String gameName;
	private long   childID;
	private long   gameID;
	private ArrayList<StationConfiguration> stations = new ArrayList<StationConfiguration>();
	
	public GameConfiguration(String gameName, long gameID, long childID) {
		this.gameName = gameName;
		this.childID = childID;
		this.gameID = gameID;
		this.guardianID = Data.currentGuardianID;
	}
	
	public GameConfiguration(String gameName, long gameID, long childID, long guardianID) {
		this.gameName = gameName;
		this.childID = childID;
		this.gameID = gameID;
		this.guardianID = guardianID;
	}
	
	public GameConfiguration(GameConfiguration gameConfiguration) {
	    this(gameConfiguration.getGameName(), 1337L, gameConfiguration.getChildId());
	    this.stations = new ArrayList<StationConfiguration>(gameConfiguration.getStations());
	}
    
	public long getChildId() {
	    return this.childID;
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
    
    public String writeConfiguration() throws IOException {
    	StringWriter sWriter = new StringWriter(1024);
    	
    	sWriter.write(String.valueOf(this.gameID));
    	sWriter.append(",");
    	sWriter.write(String.valueOf(this.guardianID));
    	sWriter.append(",");
    	sWriter.write(String.valueOf(this.childID));
    	sWriter.append(",");
    	sWriter.write(this.gameName);
    	
    	for(StationConfiguration station : stations) {
    		sWriter.append(";");
    		sWriter.write(station.writeStation());
    	}
    	
    	sWriter.append("\n");
    	
    	String result = sWriter.toString();
    	sWriter.close();
    	
    	return result;
    }
    
    public boolean readConfiguration(long gameID) {
    	
    	return true;
    }
}
