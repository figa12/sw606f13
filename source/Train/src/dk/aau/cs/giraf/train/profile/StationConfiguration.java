package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

public class StationConfiguration {
    private long category = -1L;
    private ArrayList<Long> acceptPictograms = new ArrayList<Long>(); 
    
    public StationConfiguration() {
        //TODO ?
    }
    
    public StationConfiguration(long CategoryPictogramId) {
        this.category = CategoryPictogramId;
    }
    
    public void addAcceptPictogram(long id) {
        acceptPictograms.add(id);
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
    
    public long getAcceptPictogram(int id) {
        return this.acceptPictograms.get(id);
    }
}
