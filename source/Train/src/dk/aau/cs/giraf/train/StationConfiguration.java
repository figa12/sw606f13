package dk.aau.cs.giraf.train;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds information about a single station.
 * It contains the id of a pictogram used for the category and an array of ids for the associated pictograms.
 * @author Nicklas Andersen
 *
 */
public class StationConfiguration implements Parcelable {
    private long category = -1L;
    private ArrayList<Long> acceptPictograms = new ArrayList<Long>(); 
    
    public StationConfiguration() {
        
    }
    
    public StationConfiguration(long CategoryPictogramId) {
        this.category = CategoryPictogramId;
    }
    
    public StationConfiguration(StationConfiguration stationConfiguration) {
        this.category = stationConfiguration.getCategory();
        this.acceptPictograms = new ArrayList<Long>(stationConfiguration.getAcceptPictograms());
    }
    
    public void addAcceptPictogram(long id) {
        acceptPictograms.add(id);
    }
    
    public void removeAccepPictogram(long id) {
        this.acceptPictograms.remove(id);
    }
    
    public void clearAcceptPictograms() {
        this.acceptPictograms.clear();
    }
    
    public ArrayList<Long> getAcceptPictograms() {
        return this.acceptPictograms;
    }
    
    public void setCategory(long category) {
        this.category = category;
    }
    
    public long getCategory() {
        return this.category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.category);
        out.writeList(this.acceptPictograms);
    }
    
    public static final Parcelable.Creator<StationConfiguration> CREATOR = new Parcelable.Creator<StationConfiguration>() {
        @Override
        public StationConfiguration createFromParcel(Parcel in) {
            return new StationConfiguration(in);
        }
        
        @Override
        public StationConfiguration[] newArray(int size) {
            return new StationConfiguration[size];
        }
    };
    
    private StationConfiguration(Parcel in) {
        this.category = in.readLong();
        in.readList(this.acceptPictograms, null);
    }
    
    /**
     * Write the station to a string.<br />
     * Example format: category,pictogram,pictogram
     * @return String representation of the station.
     * @throws IOException
     */
    public String writeStation() throws IOException {
    	StringWriter sWriter = new StringWriter(1024);
    	
    	sWriter.write(String.valueOf(this.category));
    	
    	for(Long pictogram : acceptPictograms) {
    		sWriter.append(",");
    		sWriter.write(String.valueOf(pictogram));
    	}
    	
    	String result = sWriter.toString();
    	sWriter.close();
    	
    	return result;
    }
}
