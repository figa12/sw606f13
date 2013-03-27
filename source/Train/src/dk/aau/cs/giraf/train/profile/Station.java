package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.pictogram.PictoFactory;

public class Station {

	String ID;
	Pictogram category;
	List<Pictogram> pictograms = new ArrayList<Pictogram>();
	
	public Station(String ID) {
		this.ID = ID;
	}
	
}
