package dk.aau.cs.giraf.train.profile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameLinearLayout extends LinearLayout {
    
    private ArrayList<GameConfiguration> gameConfigurations = new ArrayList<GameConfiguration>();
    
    public GameLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ArrayList<GameConfiguration> getGameConfigurations() {
        return this.gameConfigurations;
    }
    
    public void addGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfigurations.add(gameConfiguration);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gameListItem = layoutInflater.inflate(R.layout.profile_list_item, null); // Use same as profile
        
        gameListItem.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_item));
        
        /*LinearLayout linearLayout = (LinearLayout) gameListItem.findViewById(R.id.layout);
        LayoutParams params = new LayoutParams(20, 20);
        params.setMargins(0, 0, 0, 1);
        gameListItem.setLayoutParams(params);*/
        
        TextView gameNameTextView = (TextView) gameListItem.findViewById(R.id.profileName);
        gameNameTextView.setText(gameConfiguration.getGameName());

        ImageView profilePictureImageView = (ImageView) gameListItem.findViewById(R.id.profilePic);
        profilePictureImageView.setImageResource(R.drawable.default_profile);
        profilePictureImageView.setImageBitmap(BitmapFactory.decodeFile(PictoFactory.INSTANCE.getPictogram(super.getContext(),gameConfiguration.getStation(0).getCategory()).getImagePath()));
        
        super.addView(gameListItem);
    }
    
    public void removeGameConfiguration(GameConfiguration gameConfiguration) {
        this.removeGameConfiguration(this.gameConfigurations.indexOf(gameConfiguration));
    }
    
    public void removeGameConfiguration(int index) {
        this.removeViewAt(index);
        this.gameConfigurations.remove(index);
    }
    
    public void loadAllConfigurations() {
        FileInputStream fis = null;
        StringWriter sWriter = new StringWriter(1024);
        
        try {
            fis = getContext().openFileInput(ProfileActivity.SAVEFILE_PATH);

            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and append to string
                sWriter.append((char) content);
            }
        } catch(FileNotFoundException e) {
            return;
        } catch (Exception e) {
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
        
        this.splitConfigurations(sWriter.toString());
    }
    
    private void splitConfigurations(String data) {
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
                stations.add(station);
            }
            
            GameConfiguration gameConf = new GameConfiguration(gameName, gameID, childID, guardianID);
            gameConf.setStations(stations);
            
            this.addGameConfiguration(gameConf);
        }
    }
}
