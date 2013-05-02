package dk.aau.cs.giraf.train.profile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GameListView extends ListView {
    
    private GameAdapter adapter;
    private ArrayList<GameConfiguration> gameConfigurations;
    
    public GameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                GameListView.this.adapter.setSelectedPosition(position);
            }
        });
    }
    
    public ArrayList<GameConfiguration> getGameConfigurations() {
    	return this.gameConfigurations;
    }
    
    public void addGameConfiguration(GameConfiguration gameConfiguration) {
    	this.adapter.add(gameConfiguration);
    }
    
    public void loadGames() {
        this.gameConfigurations = loadAllConfigurations();
        
        this.adapter = new GameAdapter(super.getContext(), R.drawable.list_item, gameConfigurations);
        super.setAdapter(this.adapter);
    }
    
    public ArrayList<GameConfiguration> loadAllConfigurations() {
		FileInputStream fis = null;
		StringWriter sWriter = new StringWriter(1024);
		
		try {
			fis = getContext().openFileInput(ProfileActivity.SAVEFILE_PATH);

			int content;
			while ((content = fis.read()) != -1) {
				// convert to char and append to string
				sWriter.append((char) content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sWriter.close();
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		ArrayList<GameConfiguration> gameConfigurations = splitConfigurations(sWriter.toString());
		
		return gameConfigurations;
	}
	
	private ArrayList<GameConfiguration> splitConfigurations(String data) {
		ArrayList<GameConfiguration> gameConfigurations = new ArrayList<GameConfiguration>();
		String[] configurations = data.split("\n");
		
		// For each configuration
		for (int i = 0; i < configurations.length; i++) {
			
			String[] parts = configurations[i].split(";");
			String[] game = parts[0].split(",");
			
			long gameID = Long.valueOf(game[0]).longValue();
			long guardianID = Long.valueOf(game[1]).longValue();
			long childID = Long.valueOf(game[2]).longValue();
			String gameName = game[3];
			ArrayList<StationConfiguration> stations = new ArrayList<StationConfiguration>();
			
			// For each station
			for (int k = 1; k < parts.length; k++) {
				StationConfiguration station = new StationConfiguration();
				String[] stationParts = parts[k].split(",");
				
				station.setCategory(Long.valueOf(stationParts[0]).longValue());
				
				// For each accept pictogram of station
				for (int n = 1; n < stationParts.length; n++) {
					station.addAcceptPictogram(Long.valueOf(stationParts[n]).longValue());
				}
				
			}
			
			GameConfiguration gameConf = new GameConfiguration(gameName, gameID, childID, guardianID);
			gameConf.setStations(stations);
			
			gameConfigurations.add(gameConf);
		}
		
		return gameConfigurations;
	}
}
