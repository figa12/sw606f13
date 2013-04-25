package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.SubProfile;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;
import android.content.Context;

public class DB {
	Context context;
	Helper helper;
	App app;
	private long appId;

	public DB(Context context) {
		this.appId = getAppId();
		this.context = context;
		this.helper = new Helper(context);

	}
	
	private ArrayList<GameConfiguration> loadGameConfigurations(long childId) {
		ArrayList<GameConfiguration> gameConfigurations = new ArrayList<GameConfiguration>();
		
		App app = this.helper.appsHelper.getAppByIds(this.appId, childId);
		
		if(app.getSettings() != null){
			Setting<String, String, String> settings = app.getSettings();
			Set<String> keys = settings.keySet();

			for (String key : keys) {
				GameConfiguration game = getSubProfile(settings.get(key));
				mSubs.add(sub);
			}	
		}
		return gameConfigurations;
	}
	
	private GameConfiguration getGameConfiguration(HashMap<String, String> map) {
		String name = String.valueOf(map.get("gameName"));
		int gameID = Integer.valueOf((String)map.get("GameID"));
		int childID = Integer.valueOf((String)map.get("childID"));
		GameConfiguration game = new GameConfiguration(name, gameID, childID);
		
		ArrayList<Station> stations = new ArrayList<Station>();
		game.stations = stations.
	}

	public boolean saveChild(Child child, GameConfiguration game) {

		HashMap<String, String> map = game.getHashMap();

		App app = helper.appsHelper.getAppByIds(this.appId, child.getProfileId());
		
		Setting<String, String, String> settings = app.getSettings();
		if(settings == null){
			settings = new Setting<String, String, String>();
		}
		
		// Insert the hashmap with the gameConfiguration ID as key
		settings.put(String.valueOf(game.gameID), map);
		app.setSettings(settings);
		
		Profile newProfile = helper.profilesHelper.getProfileById(child.getProfileId());
		helper.appsHelper.modifyAppByProfile(app, newProfile);

		return true;
	}

	/**
	 * Get the app specified by the ProfileActivity, if non is avalible a default one is created
	 * @return The id of the app specified by the ProfileActivity.
	 */
	private long getAppId() {
		// Find the app which has the same package name as this one
		for (App app : helper.appsHelper.getApps()) {
			String contextName = context.getPackageName();
			if (app.getaPackage().equalsIgnoreCase(contextName)) {
				this.app = app;
				break;
			}
		}

		if (this.app == null) {
			// If no app has been found, generate one and insert it in Oasis
			this.app = new App("Train", "1", "", context.getPackageName(), "ProfileActivity");
			this.app.setId(helper.appsHelper.insertApp(this.app));
		}
		
		return this.app.getId();
		
	}

}