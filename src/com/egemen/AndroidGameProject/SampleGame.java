package com.egemen.AndroidGameProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.egemen.examplegameproject.Assets;
import com.egemen.framework.Screen;
import com.egemen.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {

	//This is our Activity (the thing that runs when we run the program)
	// We are reading maps from .txt
	public static String map;
	boolean firstTimeCreate = true;

	@Override
	public Screen getInitScreen() {
		//We use getInitScreen function to check if this is the first time we run this class (to load resources)

		if (firstTimeCreate) {
			Assets.load(this);
			firstTimeCreate = false;
		}

		InputStream is = getResources().openRawResource(R.raw.map1);
		map = convertStreamToString(is);

		return new SplashLoadingScreen(this);
	}
	
	@Override
	public void onBackPressed(){
		//this behaviour changes depending on the current screen
		getCurrentScreen().backButton();
	}
	
	private String convertStreamToString(InputStream is) {
		//This is just some fancy stuff to transform our txt file into a string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try{
			while((line = reader.readLine()) != null){
				sb.append((line + "\n"));
			}
		} catch (IOException e){
			Log.w("LOG", e.getMessage());
		} finally {
			try {
				is.close();
			} catch(IOException e){
				Log.w("LOG", e.getMessage());
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Assets.theme.play();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Assets.theme.pause();
	}
}
