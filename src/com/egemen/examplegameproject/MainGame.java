package com.egemen.examplegameproject;

import com.egemen.framework.Screen;
import com.egemen.framework.implementation.AndroidGame;


public class MainGame extends AndroidGame{

	@Override
	public Screen getInitScreen() {
		return new LoadingScreen(this);
	}
	
	@Override
	public void onBackPressed(){
		this.getCurrentScreen().backButton();
	}
}
